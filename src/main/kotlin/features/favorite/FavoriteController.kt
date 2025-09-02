package ru.topbun.features.favorite

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingCall
import ru.topbun.models.favorite.FavoriteTable
import ru.topbun.utills.AppException
import ru.topbun.utills.ErrorMessage
import ru.topbun.utills.getUserFromToken
import ru.topbun.utills.wrapperException

class FavoriteController(
    val call: RoutingCall
) {

    suspend fun switchFavorite(){
        call.wrapperException {
            val user = call.getUserFromToken()
            val id = call.parameters["id"]?.toIntOrNull() ?: throw AppException(HttpStatusCode.BadRequest, ErrorMessage.PARAMS_ID)
            val result = FavoriteTable.switchFavorite(user.id, id)
            call.respond(result)
        }
    }

}