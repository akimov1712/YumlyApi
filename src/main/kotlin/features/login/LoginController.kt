package ru.topbun.features.login

import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingCall
import ru.topbun.features.account.entity.TokenResponse
import ru.topbun.features.login.entity.LoginReceive
import ru.topbun.models.user.UserTable
import ru.topbun.utills.AppException
import ru.topbun.utills.ErrorMessage
import ru.topbun.utills.generateToken
import ru.topbun.utills.passwordVerify
import ru.topbun.utills.wrapperException

class LoginController(private val call: RoutingCall) {

    suspend fun login(){
        call.wrapperException {
            val login = call.receive<LoginReceive>()
            val user = UserTable.getUser(login.email) ?: throw AppException(HttpStatusCode.NotFound, ErrorMessage.USER_NOT_FOUND_WITH_EMAIL_PASSWORD)
            val passwordIsCorrect = passwordVerify(login.password, user.password)
            if (!passwordIsCorrect) throw AppException(HttpStatusCode.NotFound, ErrorMessage.USER_NOT_FOUND_WITH_EMAIL_PASSWORD)
            if (user.isVerified){
                call.respond(TokenResponse(generateToken(user.email)))
            } else{
                call.respond(user)
            }
        }
    }

}