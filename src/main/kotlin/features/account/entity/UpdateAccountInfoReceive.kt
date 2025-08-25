package ru.topbun.features.account.entity

import io.ktor.http.HttpStatusCode
import kotlinx.serialization.Serializable
import ru.topbun.utills.AppException

@Serializable
data class UpdateAccountInfoReceive(
    val username: String,
    val photoUrl: String? = null
){

    fun isValid() = when{
        username.length < 4 -> throw AppException(HttpStatusCode.BadRequest, ru.topbun.utills.Error.USERNAME_LENGTH)
        else -> true
    }

}
