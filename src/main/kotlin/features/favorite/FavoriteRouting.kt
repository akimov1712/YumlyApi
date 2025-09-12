package ru.topbun.features.favorite

import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.configureFavoriteRouting(){
    routing {
        authenticate {
            route("/favorite") {
                post("/{id}") {
                    val controller = FavoriteController(call)
                    controller.switchFavorite()
                }
                post("/my") {
                    val controller = FavoriteController(call)
                    controller.getMyFavoriteRecipe()
                }
                get("/{id}") {
                    val controller = FavoriteController(call)
                    controller.getFavoriteRecipe()
                }
            }
        }
    }
}