package ru.topbun.models.user

import models.user.UserDTO
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

object UserTable : IntIdTable("users") {

    val username = varchar("username", 24)
    val email = varchar("email", 255).uniqueIndex()
    val password = text("password")
    val photoUrl = text("photo_url").nullable()
    val isVerified = bool("is_verified").default(false)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    val updatedAt = datetime("updated_at").defaultExpression(CurrentDateTime)

    fun updatePassword(id: Int, newPassword: String) {
        transaction {
            update({ UserTable.id eq id }) {
                it[password] = newPassword
                it[updatedAt] = CurrentDateTime
            }
        }
    }

    fun containsUser(email: String): Boolean = transaction {
        selectAll().where { UserTable.email eq email }.count() > 0
    }

    fun getUser(email: String): UserDTO? = transaction {
        selectAll().where { UserTable.email eq email }
            .firstOrNull()
            ?.toUser()
    }

    fun getUser(id: Int): UserDTO? = transaction {
        selectAll().where { UserTable.id eq id }
            .firstOrNull()
            ?.toUser()
    }

    fun confirmAccount(id: Int){
        transaction {
            update({ UserTable.id eq id }) {
                it[UserTable.isVerified] = true
            }
        }
    }

    fun updateUser(
        id: Int,
        username: String,
        photoUrl: String?,
    ) = transaction {
        UserTable.update(
            where = { UserTable.id eq id }
        ) {
            it[UserTable.username] = username
            it[UserTable.photoUrl] = photoUrl
            it[UserTable.updatedAt] = CurrentDateTime
        }
        getUser(id)
    }


    fun insertUser(
        username: String,
        email: String,
        password: String,
        photoUrl: String? = null
    ) {
        transaction {
            insert {
                it[UserTable.username] = username
                it[UserTable.email] = email
                it[UserTable.password] = password
                it[UserTable.photoUrl] = photoUrl
            }
        }
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