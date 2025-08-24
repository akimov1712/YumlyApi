package ru.topbun.models.user

import models.user.UserDTO
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object UserTable: IntIdTable("users") {

    val username = varchar("username", 24)
    val email = varchar("email", 255).uniqueIndex()
    val password = text("password")
    val photoUrl = text("photo_url").nullable()
    val isVerified = bool("is_verified").default(false)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    val updatedAt = datetime("updated_at").defaultExpression(CurrentDateTime)


    fun containsUser(email: String): Boolean = getUser(email) != null

    fun getUser(email: String): UserDTO? = transaction { selectAll().where { UserTable.email eq email }.firstOrNull()?.toUser() }

    fun insertUser(user: UserDTO){
        transaction { insert {
            it[UserTable.id] = user.id
            it[UserTable.username] = user.username
            it[UserTable.email] = user.email
            it[UserTable.password] = user.password
            it[UserTable.photoUrl] = user.photoUrl
            it[UserTable.isVerified] = user.isVerified
            it[UserTable.createdAt] = user.createdAt
            it[UserTable.updatedAt] = user.updatedAt
        } }
    }

    fun ResultRow.toUser(): UserDTO {
        return UserDTO(
            id = this[id].value,
            username = this[username],
            email = this[email],
            password = this[password],
            photoUrl = this[photoUrl],
            isVerified = this[isVerified],
            createdAt = this[createdAt],
            updatedAt = this[updatedAt],
        )
    }

}