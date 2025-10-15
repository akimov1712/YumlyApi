package ru.topbun.features.favorite

import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingCall
import ru.topbun.features.favorite.entity.GetFavoriteReceive
import ru.topbun.models.favorite.FavoriteTable
import ru.topbun.models.notification.NotificationTable
import ru.topbun.models.notification.NotificationType
import ru.topbun.models.recipe.RecipeTable
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
            val recipe = RecipeTable.getRecipeById(id)
            val result = FavoriteTable.switchFavorite(user.id, id)
            if (result) NotificationTable.addNotification(user.id, NotificationType.LIKE,recipe.author.userId, recipe.id )
            call.respond(result)
        }
    }


    suspend fun getMyFavoriteRecipe(){
        call.wrapperException {
            val user = call.getUserFromToken()
            val receive = call.receive<GetFavoriteReceive>()
            val favoriteRecipes = FavoriteTable.getFavoriteRecipes(userId = user.id, limit = receive.limit, offset = receive.offset)
            call.respond(favoriteRecipes)
        }
    }

    suspend fun getFavoriteRecipe(){
        call.wrapperException {
            val id = call.parameters["id"]?.toIntOrNull() ?: throw AppException(HttpStatusCode.BadRequest, ErrorMessage.PARAMS_ID)
            val receive = call.receive<GetFavoriteReceive>()
            val favoriteRecipes = FavoriteTable.getFavoriteRecipes(userId = id, limit = receive.limit, offset = receive.offset)
            call.respond(favoriteRecipes)
        }
    }

}