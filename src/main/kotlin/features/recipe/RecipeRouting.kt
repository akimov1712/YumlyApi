package ru.topbun.features.recipe

import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.configureRecipeRouting() {
    routing {
        authenticate {
            route("recipe") {
                post {
                    val controller = RecipeController(call)
                    controller.getRecipes()
                }
                get("/{id}") {
                    val controller = RecipeController(call)
                    controller.getRecipeWithId()
                }
                post("/favorite") {
                    val controller = RecipeController(call)
                    controller.getFavoriteRecipe()
                }
                delete("/{id}") {
                    val controller = RecipeController(call)
                    controller.deleteRecipe()
                }
                post("/add") {
                    val controller = RecipeController(call)
                    controller.addRecipe()
                }
            }
        }
    }
}