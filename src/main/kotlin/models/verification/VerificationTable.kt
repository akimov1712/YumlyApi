package models.verification

import kotlinx.datetime.toKotlinLocalDateTime
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import ru.topbun.models.user.UserTable
import ru.topbun.models.user.UserTable.photoUrl
import ru.topbun.models.user.UserTable.username
import ru.topbun.models.verification.VerificationDTO
import ru.topbun.utills.generateVerificationCode
import java.time.LocalDateTime

object VerificationTable : IntIdTable("verifications") {
    val userId = reference("user_id", UserTable)
    val type = varchar("type", 50)
    val code = varchar("code", 4)
    val confirmed = bool("confirmed").default(false)
    val expiresAt = datetime("expires_at")

    fun isPasswordChange(userId: Int) = transaction {
        getVerificationCode(userId, VerificationType.RESET_PASSWORD)?.confirmed ?: false
    }

    fun updateConfirmedVerificationCode(id: Int, status: Boolean) = transaction {
        update(
            where = { VerificationTable.id eq id }
        ) {
            it[VerificationTable.confirmed] = status
        }
    }

    fun getOrCreateVerificationCode(userId: Int, type: VerificationType): VerificationDTO{
        return (getVerificationCode(userId, type) ?: createVerification(userId, type)).also { println("Get or Create code: $it") }
    }

    fun getVerificationCode(userId: Int, type: VerificationType) = transaction {
        VerificationTable.selectAll().where {
            (VerificationTable.userId eq userId) and (VerificationTable.type eq type.toString())
        }.lastOrNull {
            it[expiresAt] > LocalDateTime.now().toKotlinLocalDateTime()
        }
    }?.toVerification().also { println("Get code: $it") }

    private fun getVerificationCodeFromId(id: Int): VerificationDTO{
        return transaction { VerificationTable.selectAll().where { VerificationTable.id eq id }.single() }.toVerification()
    }

    fun createVerification(userId: Int,  type: VerificationType): VerificationDTO {
        val isTest = UserTable.getUser(userId).email == "test@test.ru"
        val code = generateVerificationCode().takeIf { !isTest } ?: "0000"
        val verificationId = transaction {
            VerificationTable.insertAndGetId {
                it[VerificationTable.userId] = userId
                it[VerificationTable.code] = code
                it[VerificationTable.type] = type.toString()
                it[VerificationTable.expiresAt] = LocalDateTime.now().plusMinutes(10).toKotlinLocalDateTime()
            }.value
        }
        return getVerificationCodeFromId(verificationId)
    }

    private fun ResultRow.toVerification() = VerificationDTO(
        id = this[id].value,
        userId = this[userId].value,
        code = this[code],
        type = VerificationType.valueOf(this[type]),
        confirmed = this[confirmed],
        expiresAt = this[expiresAt],
    )

}