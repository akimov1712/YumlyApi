package ru.topbun.models.gpt.message

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.topbun.models.gpt.chat.GptChatTable

object GptMessageTable: IntIdTable("gpt_messages") {

    val chatId = reference("chat_id", GptChatTable)
    val role = varchar("role", 32)
    val text = text("text")
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)

    fun addMessage(chatId: Int, role: GptMessageRoleType, text: String) = transaction {
        val id = insertMessage(chatId, role, text)
        getMessage(id)
    }

    fun insertMessage(chatId: Int, role: GptMessageRoleType, text: String) = insert {
        it[GptMessageTable.chatId] = chatId
        it[GptMessageTable.role] = role.toString()
        it[GptMessageTable.text] = text
    }[GptMessageTable.id].value

    fun getMessage(id: Int) = transaction { selectAll().where{ GptMessageTable.id eq id }.first().toDTO() }

    fun getMessages(chatId: Int) = transaction {
        selectAll()
            .where { GptMessageTable.chatId eq chatId }
            .orderBy(GptMessageTable.createdAt to SortOrder.ASC, GptMessageTable.id to SortOrder.ASC)
            .map { it.toDTO() }
    }

    private fun ResultRow.toDTO() = GptMessageDTO(
        id = this[id].value,
        role = GptMessageRoleType.valueOf(this[role]),
        text = this[text],
        createdAt = this[createdAt]
    )

}
