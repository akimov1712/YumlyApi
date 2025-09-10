package ru.topbun.models.notification

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import ru.topbun.models.user.UserTable

object NotificationTable: IntIdTable("notifications") {

    val userId = reference("user_id", UserTable)
    val type = varchar("type", 32)
    val initiatorId = reference("user_id", UserTable)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)


}