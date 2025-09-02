package ru.topbun.features.recipe

import io.ktor.server.application.Application
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.configureRecipeRouting(){
    routing {
        route("recipe"){
            get {
                val controller = RecipeController(call)
                controller.getRecipes()
            }
            get("/favorite"){
                val controller = RecipeController(call)
                controller.getFavoriteRecipe()
            }
        }
    }
}