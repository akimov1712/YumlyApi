package features.signUp.entity

import io.ktor.http.HttpStatusCode
import kotlinx.serialization.Serializable
import ru.topbun.utills.AppException
import ru.topbun.utills.Error

@Serializable
data class SignUpReceive(
    val email: String,
    val username: String,
    val password: String,
    val photoUrl: String? = null
){

    fun isValid() = when{
        !Regex("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$").matches(email) -> throw AppException(
            HttpStatusCode.BadRequest, Error.INVALID_EMAIL)
        username.length < 4 -> throw AppException(HttpStatusCode.BadRequest, Error.USERNAME_LENGTH)
        password.length < 6 -> throw AppException(HttpStatusCode.BadRequest, Error.PASSWORD_LENGTH)
        else -> true
    }


}