package ru.topbun.models.follow

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.topbun.models.user.UserTable

object FollowTable: IntIdTable("follows") {

    val followerId = reference("follower_id", UserTable)
    val followingId = reference("following_id", UserTable)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)

    fun getListFollowersIds(followingId: Int) = transaction {
        selectAll().where{ FollowTable.followingId eq followingId }.map { it[FollowTable.followerId].value }
    }

    fun getListFollowingIds(followerId: Int) = transaction {
        selectAll().where{ FollowTable.followerId eq followerId }.map { it[FollowTable.followingId].value }
    }

    fun switchFollow(followerId: Int, followingId: Int) = transaction {
        if (isFollowed(followerId, followingId)){
            deleteFollow(followerId, followingId)
            false
        } else {
            addFollow(followerId, followingId)
            true
        }
    }

    fun isFollowed(followerId: Int, followingId: Int) = transaction {
        selectAll().where { (FollowTable.followerId eq followerId) and (FollowTable.followingId eq followingId) }.count() > 0
    }

    private fun addFollow(followerId: Int, followingId: Int) = transaction {
        insert {
            it[FollowTable.followerId] = followerId
            it[FollowTable.followingId] = followingId
        }
    }

    private fun deleteFollow(followerId: Int, followingId: Int) = transaction {
        deleteWhere { (FollowTable.followerId eq followerId) and (FollowTable.followingId eq followingId) }
    }

}