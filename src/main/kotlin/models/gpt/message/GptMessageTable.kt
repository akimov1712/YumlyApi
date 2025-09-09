package ru.topbun.models.gpt.message

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
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
        insert {
            it[GptMessageTable.chatId] = chatId
            it[GptMessageTable.role] = role.toString()
            it[GptMessageTable.text] = text
        }
    }

    fun getMessages(chatId: Int) = transaction {
        selectAll().where { GptMessageTable.chatId eq chatId }.map { it.toDTO() }
    }

    private fun ResultRow.toDTO() = GptMessageDTO(
        id = this[id].value,
        role = GptMessageRoleType.fromString(this[role]),
        text = this[text],
        createdAt = this[createdAt]
    )

}