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
import ru.topbun.models.FieldUpdate

object UserTable : IntIdTable("users") {

    val username = varchar("username", 24)
    val email = varchar("email", 255).uniqueIndex()
    val password = text("password")
    val photoUrl = text("photo_url").nullable()
    val isVerified = bool("is_verified").default(false)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    val updatedAt = datetime("updated_at").defaultExpression(CurrentDateTime)

    fun containsUser(email: String): Boolean = transaction {
        selectAll().where { UserTable.email eq email }.count() > 0
    }

    fun getUser(email: String): UserDTO? = transaction {
        selectAll().where { UserTable.email eq email }
            .firstOrNull()
            ?.toUser()
    }

    fun updateUser(
        id: Int,
        username: FieldUpdate<String> = FieldUpdate.Skip,
        email: FieldUpdate<String> = FieldUpdate.Skip,
        password: FieldUpdate<String> = FieldUpdate.Skip,
        photoUrl: FieldUpdate<String?> = FieldUpdate.Skip,
        isVerified: FieldUpdate<Boolean> = FieldUpdate.Skip
    ) {
        transaction {
            update({ UserTable.id eq id }) { row ->
                when(username){
                    is FieldUpdate.Set -> username.value?.let { row[UserTable.username] = it }
                    FieldUpdate.Skip -> {}
                }
                when (email) {
                    is FieldUpdate.Set -> email.value?.let { row[UserTable.email] = it }
                    FieldUpdate.Skip -> {}
                }
                when (password) {
                    is FieldUpdate.Set -> password.value?.let { row[UserTable.password] = it }
                    FieldUpdate.Skip -> {}
                }
                when (photoUrl) {
                    is FieldUpdate.Set -> row[UserTable.photoUrl] = photoUrl.value
                    FieldUpdate.Skip -> {}
                }
                when (isVerified) {
                    is FieldUpdate.Set -> isVerified.value?.let { row[UserTable.isVerified] = it }
                    FieldUpdate.Skip -> {}
                }

                row[updatedAt] = CurrentDateTime
            }
        }
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