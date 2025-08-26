package ru.topbun.features.signUp

import features.signUp.entity.SignUpReceive
import features.signUp.entity.SignUpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingCall
import ru.topbun.models.user.UserTable
import ru.topbun.utills.AppException
import ru.topbun.utills.ErrorMessage
import ru.topbun.utills.generateToken
import ru.topbun.utills.toPasswordHash
import ru.topbun.utills.wrapperException

class SignUpController(
    private val call: RoutingCall
) {

    suspend fun signUp(){
        call.wrapperException{
            val signUp = call.receive<SignUpReceive>()
            val userIsFound = UserTable.containsUser(signUp.email)
            if (userIsFound) throw AppException(HttpStatusCode.Conflict, ErrorMessage.USER_EXISTS)
            if (signUp.isValid()){
                val passwordHash = signUp.password.toPasswordHash()
                UserTable.insertUser(
                    username = signUp.username,
                    email = signUp.email,
                    password = passwordHash,
                    photoUrl = signUp.photoUrl
                )
                call.respond(SignUpResponse(token = generateToken(signUp.email)))
            }

        }
    }

}