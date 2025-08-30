package ru.topbun.models.recipe

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import ru.topbun.models.user.UserTable

object RecipeTable: IntIdTable("recipes") {

    val author = reference("user_id", UserTable, onDelete = ReferenceOption.SET_NULL)
    val title = text("title")
    val description = text("description").nullable()
    val previewUrl = text("preview").nullable()
    val cookingTime = integer("cooking_time")
    val kcal = integer("kcal")
    val protein = double("protein")
    val fat = double("fat")
    val carb = double("carb")


}