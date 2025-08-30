package ru.topbun.models.ingredient

import org.jetbrains.exposed.dao.id.IntIdTable
import ru.topbun.models.recipe.RecipeTable

object IngredientTable: IntIdTable("ingredients") {

    val recipeId = reference("recipe_id", RecipeTable)
    val name = text("name")
    val value = text("value")

}