package ru.topbun.features.favorite

import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.Route
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Route.configureFavoriteRouting(){
    authenticate {
        route("/favorite") {
            post("/switch/{id}") {
                val controller = FavoriteController(call)
                controller.switchFavorite()
            }
            post("/my") {
                val controller = FavoriteController(call)
                controller.getMyFavoriteRecipe()
            }
            post("/{id}") {
                val controller = FavoriteController(call)
                controller.getFavoriteRecipe()
            }
        }
    }

}