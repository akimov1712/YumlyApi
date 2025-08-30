package ru.topbun.features.confirmAccount

import features.senderMessage.SenderMessageManager
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingCall
import kotlinx.datetime.toKotlinLocalDateTime
import models.verification.VerificationTable
import models.verification.VerificationType
import ru.topbun.features.account.entity.TokenResponse
import ru.topbun.features.confirmAccount.entity.ConfirmAccountReceive
import ru.topbun.features.confirmAccount.entity.ConfirmAccountRequestReceive
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

    suspend fun request(){
        call.wrapperException {
            val requestReceive = call.receive<ConfirmAccountRequestReceive>()
            val user = UserTable.getUser(requestReceive.email)
                ?: throw AppException(HttpStatusCode.NotFound, ErrorMessage.USER_NOT_FOUND)
            val code = VerificationTable.getOrCreateVerificationCode(user.id, VerificationType.SIGN_UP_CONFIRM)

            val sender = SenderMessageManager()
            sender.sendVerificationMessage(requestReceive.email, code.code)

            call.respond(HttpStatusCode.OK)
        }
    }

    suspend fun confirm() {
        call.wrapperException {
            val confirmAccount = call.receive<ConfirmAccountReceive>()
            val user = UserTable.getUser(confirmAccount.email)
                ?: throw AppException(HttpStatusCode.NotFound, ErrorMessage.USER_NOT_FOUND)

            val verification = VerificationTable.getVerificationCode(user.id, VerificationType.SIGN_UP_CONFIRM) ?: run {
                call.respond(HttpStatusCode.Forbidden, VerificationStatusResponse(VerificationStatusType.CODE_EXPIRED))
                return@wrapperException
            }

            if (verification.code != confirmAccount.code) {
                call.respond(HttpStatusCode.Forbidden, VerificationStatusResponse(VerificationStatusType.CODE_NO_MATCH))
                return@wrapperException
            }
            if (verification.expiresAt < LocalDateTime.now().toKotlinLocalDateTime()) {
                call.respond(HttpStatusCode.Forbidden, VerificationStatusResponse(VerificationStatusType.CODE_EXPIRED))
                return@wrapperException
            }

            UserTable.confirmAccount(user.id)
            val token = generateToken(user.email)
            call.respond(TokenResponse(token))

        }
    }
}