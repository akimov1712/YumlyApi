package ru.topbun.models.gpt.chat

import kotlinx.datetime.toKotlinLocalDateTime
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import ru.topbun.models.gpt.message.GptMessageTable
import ru.topbun.models.gpt.message.GptMessageRoleType
import ru.topbun.models.user.UserTable
import java.time.LocalDateTime

object GptChatTable: IntIdTable("gpt_chats") {

    val userId = reference("user_id", UserTable)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    val updatedAt = datetime("updated_at").defaultExpression(CurrentDateTime)


    fun getChat(id: Int) = transaction { selectAll().where { GptChatTable.id eq id }.firstOrNull()?.toDTO() }

    fun getChat(id: Int, userId: Int) = transaction {
        selectAll()
            .where { (GptChatTable.id eq id) and (GptChatTable.userId eq userId) }
            .firstOrNull()
            ?.toDTO()
    }

    fun addExchange(
        userId: Int,
        chatId: Int?,
        userText: String,
        assistantRole: GptMessageRoleType,
        assistantText: String,
    ) = transaction {
        val targetChatId = chatId ?: insertAndGetId {
            it[GptChatTable.userId] = userId
        }.value

        if (chatId != null) {
            val chatExists = selectAll()
                .where { (GptChatTable.id eq chatId) and (GptChatTable.userId eq userId) }
                .firstOrNull() != null
            if (!chatExists) return@transaction null
        }

        GptMessageTable.insertMessage(targetChatId, GptMessageRoleType.USER, userText)
        GptMessageTable.insertMessage(targetChatId, assistantRole, assistantText)
        updateUpdatedAt(targetChatId)

        selectAll().where { GptChatTable.id eq targetChatId }.first().toDTO()
    }

    fun getChats(userId: Int, limit: Int = 10, offset: Int = 0) = transaction {
        selectAll()
            .where { GptChatTable.userId eq userId }
            .orderBy(GptChatTable.updatedAt to SortOrder.DESC, GptChatTable.id to SortOrder.DESC)
            .limit(limit)
            .offset(offset.toLong())
            .map { it.toDTO() }
    }

    private fun updateUpdatedAt(chatId: Int) {
        update(
            where = { GptChatTable.id eq chatId }
        ) {
            it[updatedAt] = LocalDateTime.now().toKotlinLocalDateTime()
        }
    }

    private fun ResultRow.toDTO(): GptChatDTO {
        val id = this[id].value

        return GptChatDTO(
            id = id,
            userId = this[userId].value,
            messages = GptMessageTable.getMessages(id),
            createdAt = this[createdAt],
            updatedAt = this[updatedAt],
        )
    }

}
