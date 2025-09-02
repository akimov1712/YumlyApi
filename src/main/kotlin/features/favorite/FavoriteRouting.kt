package ru.topbun.features.favorite

import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

fun Application.configureFavoriteRouting(){
    routing {
        authenticate {
            post("/favorite/{id}") {
                val controller = FavoriteController(call)
                controller.switchFavorite()
            }
        }
    }
}