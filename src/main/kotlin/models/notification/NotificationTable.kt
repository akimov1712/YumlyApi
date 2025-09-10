package ru.topbun.models.notification

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.topbun.models.user.UserTable

object NotificationTable: IntIdTable("notifications") {

    val userId = reference("user_id", UserTable)
    val type = varchar("type", 32)
    val initiatorId = reference("user_id", UserTable)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)

    fun addNotification(userId: Int, type: NotificationType, initiatorId: Int) = transaction {
        insert {
            it[NotificationTable.userId] = userId
            it[NotificationTable.type] = type.toString()
            it[NotificationTable.initiatorId] = initiatorId
        }
    }

    fun getNotifications(userId: Int, limit: Int, offset: Int) = transaction {
        selectAll().where { NotificationTable.userId eq userId }
            .limit(limit).offset(offset.toLong())
            .map { it.toDTO() }
    }


    private fun ResultRow.toDTO() = NotificationDTO(
        id = this[id].value,
        type = NotificationType.valueOf(this[type]),
        initiator = UserTable.getUser(this[initiatorId].value),
        createdAt = this[createdAt]
    )

}