package ru.topbun.features.recipe

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingCall
import ru.topbun.features.recipe.entity.AddRecipeReceive
import ru.topbun.features.recipe.entity.GetRecipeReceive
import ru.topbun.models.favorite.FavoriteTable
import ru.topbun.models.history.HistoryTable
import ru.topbun.models.recipe.RecipeTable
import ru.topbun.utills.AppException
import ru.topbun.utills.ErrorMessage
import ru.topbun.utills.getUserFromToken
import ru.topbun.utills.wrapperException

class RecipeController(
    val call: RoutingCall
) {

    suspend fun getRecipes(){
        call.wrapperException {
            val receive = call.receive<GetRecipeReceive>()
            HistoryTable.insert(receive.q)

            val tokenPrincipal = call.principal<JWTPrincipal>()
            if(tokenPrincipal == null){
                val recipe = RecipeTable.getRecipes(receive.q, receive.limit, receive.offset, settings = receive.settings)
                call.respond(recipe)
            } else {
                val user = call.getUserFromToken()
                val recipe = RecipeTable.getRecipes(receive.q, receive.limit, receive.offset, settings = receive.settings, requestUserId = user.id)
                call.respond(recipe)
            }
        }
    }

    suspend fun getFavoriteRecipe(){
        call.wrapperException {
            val user = call.getUserFromToken()
            val receive = call.receive<GetRecipeReceive>()
            val favoriteRecipeIds = FavoriteTable.getFavoriteRecipeIds(user.id)
            val favoriteRecipe = favoriteRecipeIds.map { RecipeTable.getRecipes(limit = receive.limit, offset = receive.offset, settings = receive.settings, requestUserId = user.id) }
            call.respond(favoriteRecipe)
        }
    }

    suspend fun deleteRecipe() {
        call.wrapperException {
            val id = call.parameters["id"]?.toIntOrNull() ?: throw AppException(HttpStatusCode.BadRequest, ErrorMessage.PARAMS_ID)
            val user = call.getUserFromToken()
            val recipe = RecipeTable.getRecipeWithId(id)
            if (user.id != recipe.author?.userId) throw AppException(HttpStatusCode.Forbidden, ErrorMessage.DELETE_RECIPE)
            RecipeTable.deleteRecipe(id)
            call.respond(HttpStatusCode.OK)
        }
    }

    suspend fun getRecipeWithId(){
        call.wrapperException {
            val id = call.parameters["id"]?.toIntOrNull() ?: throw AppException(HttpStatusCode.BadRequest, ErrorMessage.PARAMS_ID)
            val tokenPrincipal = call.principal<JWTPrincipal>()
            if(tokenPrincipal == null){
                val recipe = RecipeTable.getRecipeWithId(id)
                call.respond(recipe)
            } else {
                val user = call.getUserFromToken()
                val recipe = RecipeTable.getRecipeWithId(id, user.id)
                call.respond(recipe)
            }
        }
    }

    suspend fun addRecipe(){
        call.wrapperException {
            val user = call.getUserFromToken()
            val recipeReceive = call.receive<AddRecipeReceive>()
            if (recipeReceive.isValid()){
                val recipe = RecipeTable.addRecipe(user.id, recipeReceive)
                call.respond(recipe)
            }
        }
    }

}