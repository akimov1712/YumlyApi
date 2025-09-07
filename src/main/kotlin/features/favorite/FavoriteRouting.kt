package ru.topbun.features.favorite

import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import ru.topbun.features.recipe.RecipeController

fun Application.configureFavoriteRouting(){
    routing {
        authenticate {
            route("/favorite") {
                post("/{id}") {
                    val controller = FavoriteController(call)
                    controller.switchFavorite()
                }
                post {
                    val controller = FavoriteController(call)
                    controller.getFavoriteRecipe()
                }
            }
        }
    }
}