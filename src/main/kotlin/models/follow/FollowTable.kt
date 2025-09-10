package ru.topbun.models.follow

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import ru.topbun.models.user.UserTable

object FollowTable: IntIdTable("follows") {

    val followerId = reference("follower_id", UserTable)
    val followingId = reference("following_id", UserTable)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)

}