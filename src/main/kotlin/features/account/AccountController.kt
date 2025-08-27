package features.account

import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingCall
import kotlinx.datetime.toKotlinLocalDateTime
import models.verification.VerificationTable
import models.verification.VerificationType
import ru.topbun.features.account.entity.ConfirmAccountReceive
import ru.topbun.features.account.entity.TokenResponse
import ru.topbun.features.account.entity.UpdateAccountInfoReceive
import ru.topbun.models.user.UserTable
import ru.topbun.utills.AppException
import ru.topbun.utills.ErrorMessage
import ru.topbun.utills.generateToken
import ru.topbun.utills.getUserFromToken
import ru.topbun.utills.wrapperException
import java.time.LocalDateTime

class AccountController(
    private val call: RoutingCall
) {

    suspend fun confirmAccount(){
        call.wrapperException {
            val confirmAccount = call.receive<ConfirmAccountReceive>()
            val user = UserTable.getUser(confirmAccount.email) ?: throw AppException(HttpStatusCode.NotFound, ErrorMessage.USER_NOT_FOUND)

            val actualCode = VerificationTable.getOrCreateVerificationCode(user.id, VerificationType.SIGN_UP_CONFIRM)

            when{
                (actualCode.expiresAt < LocalDateTime.now().toKotlinLocalDateTime()) -> {
                    call.respond(actualCode)
                }

                actualCode.code == confirmAccount.code && actualCode.expiresAt > LocalDateTime.now().toKotlinLocalDateTime() -> {
                    UserTable.confirmAccount(user.id)
                    val token = generateToken(user.email)
                    call.respond(TokenResponse(token))
                }

                actualCode.code != confirmAccount.code -> throw AppException(HttpStatusCode.BadRequest, ErrorMessage.INVALID_CODE)

                else -> throw AppException(HttpStatusCode.BadRequest, ErrorMessage.INVALID_CODE)
            }
        }
    }

    suspend fun updateInfo(){
        call.wrapperException {
            val user = call.getUserFromToken()
            val newInfo = call.receive<UpdateAccountInfoReceive>()
            if (newInfo.isValid()){
                val newUser = UserTable.updateUser(
                    id = user.id,
                    username = newInfo.username,
                    photoUrl = newInfo.photoUrl,
                ) ?: throw AppException(HttpStatusCode.NotFound, ErrorMessage.USER_NOT_FOUND)
                call.respond(newUser)
            }
        }
    }

    suspend fun accountInfo(){
        call.wrapperException{
            val user = call.getUserFromToken()
            call.respond(user)
        }
    }

}