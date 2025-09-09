package ru.topbun.models.gpt.chat

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import ru.topbun.models.gpt.message.GptMessageTable
import ru.topbun.models.user.UserTable

object GptChatTable: IntIdTable("gpt_chats") {

    val userId = reference("user_id", UserTable)
    val title = varchar("title", 64)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    val updatedAt = datetime("updated_at").defaultExpression(CurrentDateTime)


    fun updateChatTitle(id: Int, newTitle: String) = transaction {
        update(
            where = { GptChatTable.id eq id }
        ) {
            it[GptChatTable.title] = newTitle
        }
        getChat(id)
    }

    fun getChat(id: Int) = transaction { selectAll().where { GptChatTable.id eq id }.first().toDTO() }

    fun addChat(userId: Int) = transaction {
        val title = "Чат ${getChatsCount(userId)}"
        insertAndGetId {
            it[GptChatTable.userId] = userId
            it[GptChatTable.title] = title
        }.value
    }

    fun getChatsCount(userId: Int) = transaction {
        selectAll().where{ GptChatTable.userId eq userId }.count()
    }

    fun getChats(userId: Int, limit: Int = 10, offset: Int = 0) = transaction {
        selectAll().where{ GptChatTable.userId eq userId }.limit(limit).offset(offset.toLong()).map {
            it.toDTO()
        }
    }

    private fun ResultRow.toDTO(): GptChatDTO {
        val id = this[id].value

        return GptChatDTO(
            id = id,
            title = this[title],
            messages = GptMessageTable.getMessages(id),
            createdAt = this[createdAt]
        )
    }

}