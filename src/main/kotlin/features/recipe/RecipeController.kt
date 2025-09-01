package ru.topbun.features.recipe

import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingCall
import ru.topbun.features.recipe.entity.GetRecipeReceive
import ru.topbun.models.history.HistoryTable
import ru.topbun.models.recipe.RecipeTable
import ru.topbun.utills.wrapperException

class RecipeController(
    val call: RoutingCall
) {

    suspend fun getRecipes(){
        call.wrapperException {
            val receive = call.receive<GetRecipeReceive>()
            HistoryTable.insert(receive.q)
            val recipes = RecipeTable.getRecipes(receive.q, receive.limit, receive.offset)
            call.respond(recipes)
        }
    }

}