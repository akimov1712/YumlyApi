package ru.topbun.features.resetPassword

import features.resetPassword.entity.ResetPasswordConfirmReceive
import features.resetPassword.entity.ResetPasswordRequestReceive
import features.senderMessage.SenderMessageManager
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.datetime.toKotlinLocalDateTime
import models.verification.VerificationTable
import models.verification.VerificationType
import ru.topbun.features.confirmAccount.entity.VerificationStatusResponse
import ru.topbun.features.confirmAccount.entity.VerificationStatusType
import ru.topbun.models.user.UserTable
import ru.topbun.utills.AppException
import ru.topbun.utills.ErrorMessage
import ru.topbun.utills.toPasswordHash
import ru.topbun.utills.wrapperException
import java.time.LocalDateTime

class ResetPasswordController(
    val call: RoutingCall
) {

    suspend fun request() {
        call.wrapperException {
            val request = call.receive<ResetPasswordRequestReceive>()
            val user = UserTable.getUser(request.email)
                ?: throw AppException(HttpStatusCode.NotFound, ErrorMessage.USER_NOT_FOUND)
            val code = VerificationTable.getOrCreateVerificationCode(user.id, VerificationType.RESET_PASSWORD)

            val sender = SenderMessageManager()
            sender.sendVerificationMessage(request.email, code.code)

            call.respond(HttpStatusCode.OK)
        }
    }

    suspend fun confirm() {
        call.wrapperException {
            val confirm = call.receive<ResetPasswordConfirmReceive>()
            val user = UserTable.getUser(confirm.email)
                ?: throw AppException(HttpStatusCode.NotFound, ErrorMessage.USER_NOT_FOUND)

            val verification = VerificationTable.getVerificationCode(user.id, VerificationType.RESET_PASSWORD) ?: run {
                call.respond(HttpStatusCode.Forbidden, VerificationStatusResponse(VerificationStatusType.CODE_EXPIRED))
                return@wrapperException
            }

            if (verification.code != confirm.code) {
                call.respond(HttpStatusCode.Forbidden, VerificationStatusResponse(VerificationStatusType.CODE_NO_MATCH))
                return@wrapperException
            }
            if (verification.expiresAt < LocalDateTime.now().toKotlinLocalDateTime()) {
                call.respond(HttpStatusCode.Forbidden, VerificationStatusResponse(VerificationStatusType.CODE_EXPIRED))
                return@wrapperException
            }

            val passwordHash = confirm.newPassword.toPasswordHash()
            UserTable.updatePassword(user.id, passwordHash)

            call.respond(VerificationStatusResponse(VerificationStatusType.SUCCESS))
        }
    }

}