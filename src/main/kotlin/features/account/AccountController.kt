package features.account

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingCall
import models.verification.VerificationTable
import models.verification.VerificationTable.getVerificationCode
import models.verification.VerificationType
import ru.topbun.features.account.entity.ResetPasswordReceive
import ru.topbun.features.account.entity.UpdateAccountInfoReceive
import ru.topbun.models.user.UserTable
import ru.topbun.utills.AppException
import ru.topbun.utills.ErrorMessage
import ru.topbun.utills.getUserFromToken
import ru.topbun.utills.toPasswordHash
import ru.topbun.utills.wrapperException

class AccountController(
    private val call: RoutingCall
) {

    suspend fun resetPassword(){
        call.wrapperException {
            val receive = call.receive<ResetPasswordReceive>()
            val user = UserTable.getUser(receive.email) ?: throw AppException(HttpStatusCode.NotFound, ErrorMessage.USER_NOT_FOUND)
            val verification = getVerificationCode(user.id, VerificationType.RESET_PASSWORD)
            if (verification?.confirmed ?: false){
                UserTable.updatePassword(user.id, receive.newPassword.toPasswordHash())
                VerificationTable.updateConfirmedVerificationCode(verification.id, false)
                call.respond(HttpStatusCode.OK)
            }
        }
    }

    suspend fun accountInfo(){
        call.wrapperException{
            val user = call.getUserFromToken()
            call.respond(user)
        }
    }

    suspend fun profileInfo(){
        call.wrapperException {
            val tokenPrincipal = call.principal<JWTPrincipal>()
            val userId = if (tokenPrincipal != null) call.getUserFromToken().id else null
            val id = call.parameters["id"]?.toIntOrNull() ?: throw AppException(HttpStatusCode.BadRequest, ErrorMessage.PARAMS_ID)
            val profile = UserTable.getUser(id).toProfile(userId)
            call.respond(profile)
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
                )
                call.respond(newUser)
            }
        }
    }

}