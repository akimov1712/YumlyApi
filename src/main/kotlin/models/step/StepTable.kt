package ru.topbun.models.step

import org.jetbrains.exposed.dao.id.IntIdTable
import ru.topbun.models.recipe.RecipeTable

object StepTable: IntIdTable("steps") {

    val recipeId = reference("recipe_id", RecipeTable)
    val description = text("description")
    val previewUrl = text("preview_url").nullable()

}