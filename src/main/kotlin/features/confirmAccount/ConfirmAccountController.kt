package ru.topbun.features.confirmAccount

import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingCall
import kotlinx.datetime.toKotlinLocalDateTime
import models.verification.VerificationTable
import models.verification.VerificationType
import ru.topbun.features.confirmAccount.entity.ConfirmAccountReceive
import ru.topbun.features.account.entity.TokenResponse
import ru.topbun.features.confirmAccount.entity.VerificationStatusResponse
import ru.topbun.features.confirmAccount.entity.VerificationStatusType
import ru.topbun.models.user.UserTable
import ru.topbun.utills.AppException
import ru.topbun.utills.ErrorMessage
import ru.topbun.utills.generateToken
import ru.topbun.utills.wrapperException
import java.time.LocalDateTime

class ConfirmAccountController(
    private val call: RoutingCall
) {
    suspend fun confirmAccount() {
        call.wrapperException {
            val confirmAccount = call.receive<ConfirmAccountReceive>()
            val user = UserTable.getUser(confirmAccount.email)
                ?: throw AppException(HttpStatusCode.NotFound, ErrorMessage.USER_NOT_FOUND)

            val verification = VerificationTable.getOrCreateVerificationCode(user.id, VerificationType.SIGN_UP_CONFIRM)

            if (verification.expiresAt < LocalDateTime.now().toKotlinLocalDateTime()) {
                val newVerification = VerificationTable.createVerification(user.id, VerificationType.SIGN_UP_CONFIRM)
                // TODO отправить на почту
                call.respond(VerificationStatusResponse(VerificationStatusType.CODE_EXPIRED))
                return@wrapperException
            }


            if (verification.code == confirmAccount.code) {
                UserTable.confirmAccount(user.id)
                val token = generateToken(user.email)
                call.respond(TokenResponse(token))
                return@wrapperException
            }

            call.respond(VerificationStatusResponse(VerificationStatusType.CODE_NO_MATCH))
        }
    }
}