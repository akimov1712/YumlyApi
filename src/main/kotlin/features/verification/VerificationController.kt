package ru.topbun.features.verification

import features.senderMessage.SenderMessageManager
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingCall
import kotlinx.datetime.toKotlinLocalDateTime
import models.verification.VerificationTable
import models.verification.VerificationType
import ru.topbun.features.account.entity.TokenResponse
import ru.topbun.features.verification.entity.ConfirmVerificationReceive
import ru.topbun.features.verification.entity.VerificationStatusResponse
import ru.topbun.features.verification.entity.VerificationStatusType
import ru.topbun.features.verification.entity.RequestVerificationReceive
import ru.topbun.models.user.UserTable
import ru.topbun.models.user.UserTable.confirmAccount
import ru.topbun.utills.AppException
import ru.topbun.utills.ErrorMessage
import ru.topbun.utills.generateToken
import ru.topbun.utills.wrapperException
import java.time.LocalDateTime

class VerificationController(
    private val call: RoutingCall
) {

    suspend fun request(){
        call.wrapperException {
            val receive = call.receive<RequestVerificationReceive>()
            val user = UserTable.getUser(receive.email)
                ?: throw AppException(HttpStatusCode.NotFound, ErrorMessage.USER_NOT_FOUND)
            val verification = VerificationTable.getOrCreateVerificationCode(user.id, receive.type)

            SenderMessageManager.sendVerificationMessage(receive.email, verification)

            call.respond(HttpStatusCode.OK)
        }
    }

    suspend fun confirm() {
        call.wrapperException {
            val receive = call.receive<ConfirmVerificationReceive>()
            val user = UserTable.getUser(receive.email)
                ?: throw AppException(HttpStatusCode.NotFound, ErrorMessage.USER_NOT_FOUND)

            val verification = VerificationTable.getVerificationCode(user.id, receive.type) ?: run {
                call.respond(HttpStatusCode.Forbidden, VerificationStatusResponse(VerificationStatusType.CODE_EXPIRED))
                return@wrapperException
            }

            if (verification.code != receive.code) {
                call.respond(HttpStatusCode.Forbidden, VerificationStatusResponse(VerificationStatusType.CODE_NO_MATCH))
                return@wrapperException
            }
            if (verification.expiresAt < LocalDateTime.now().toKotlinLocalDateTime()) {
                call.respond(HttpStatusCode.Forbidden, VerificationStatusResponse(VerificationStatusType.CODE_EXPIRED))
                return@wrapperException
            }

            VerificationTable.updateConfirmedVerificationCode(verification.id, true)

            when(receive.type){
                VerificationType.SIGN_UP_CONFIRM -> {
                    UserTable.confirmAccount(user.id)
                    val token = generateToken(user.email)
                    call.respond(TokenResponse(token))
                }

                VerificationType.RESET_PASSWORD -> {
                    call.respond(VerificationStatusResponse(VerificationStatusType.SUCCESS))
                }
            }

        }
    }

}