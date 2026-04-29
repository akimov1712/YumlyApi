package ru.topbun.features.recipe

import io.ktor.server.auth.authenticate
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.configureRecipeRouting() {
    authenticate {
        route("/recipe") {
            post {
                val controller = RecipeController(call)
                controller.getRecipes()
            }
            post("/follow") {
                val controller = RecipeController(call)
                controller.getFollowRecipes()
            }
            get("/{id}") {
                val controller = RecipeController(call)
                controller.getRecipeById()
            }
            post("/user/{id}") {
                val controller = RecipeController(call)
                controller.getRecipesByUserId()
            }
            delete("/{id}") {
                val controller = RecipeController(call)
                controller.deleteRecipe()
            }
            post("/add") {
                val controller = RecipeController(call)
                controller.addRecipe()
            }
            get("/tags") {
                val controller = RecipeController(call)
                controller.getTags()
            }
        }
    }
}