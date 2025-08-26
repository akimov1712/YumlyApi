package ru.topbun.features.signUp

import features.signUp.entity.SignUpReceive
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingCall
import models.verification.VerificationTable
import models.verification.VerificationType
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
                val user = UserTable.getUser(signUp.email) ?: throw AppException(HttpStatusCode.NotFound, ErrorMessage.USER_NOT_FOUND)
                if (user.isVerified){
                    call.respond(generateToken(user.email))
                } else{
                    val code = VerificationTable.createVerification(user.id, VerificationType.SIGN_UP_CONFIRM)
                    call.respond(code)
                }
            }

        }
    }

}