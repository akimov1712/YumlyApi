package ru.topbun.models.recipe

import models.tag.diets.RecipeToDietsTable
import models.tag.preparation.RecipeToPreparationTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.lowerCase
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.topbun.features.recipe.entity.AddRecipeReceive
import ru.topbun.models.favorite.FavoriteTable
import ru.topbun.models.ingredient.IngredientTable
import ru.topbun.models.step.StepTable
import ru.topbun.models.tag.categories.RecipeToCategoryTable
import ru.topbun.models.user.UserTable

object RecipeTable: IntIdTable("recipes") {

    val userId = reference("user_id", UserTable)
    val title = text("title")
    val description = text("description").nullable()
    val largeImage = text("large_image").nullable()
    val smallImage = text("small_image").nullable()
    val kcal = integer("calories")
    val cookingTime = integer("cooking_time")
    val difficulty = text("difficulty")
    val protein = double("protein")
    val fat = double("fat")
    val carb = double("carbs")

    fun addRecipe(userId: Int, recipe: AddRecipeReceive) = transaction {
        val id = insert {
            it[RecipeTable.userId] = userId
            it[RecipeTable.title] = recipe.title
            it[RecipeTable.description] = recipe.description
            it[RecipeTable.largeImage] = recipe.previewUrl
            it[RecipeTable.smallImage] = recipe.previewUrl
            it[RecipeTable.kcal] = recipe.kcal
            it[RecipeTable.cookingTime] = recipe.cookingTime
            it[RecipeTable.difficulty] = recipe.difficulty.toString()
            it[RecipeTable.protein] = recipe.protein
            it[RecipeTable.fat] = recipe.fat
            it[RecipeTable.carb] = recipe.carb
        }[RecipeTable.id].value

        IngredientTable.addIngredient(id, recipe.ingredients)
        StepTable.addStep(id, recipe.steps)

        recipe.categoryId?.let { RecipeToCategoryTable.addRecipe(id, it) }
        recipe.dietsTypeId?.let { RecipeToDietsTable.addRecipe(id, it) }
        recipe.preparationId?.let { RecipeToPreparationTable.addRecipe(id, it) }

        getRecipeWithId(id)
    }

    fun deleteRecipe(id: Int) = transaction {
        FavoriteTable.deleteFavorite(id)
        StepTable.deleteSteps(id)
        IngredientTable.deleteIngredients(id)
        RecipeToCategoryTable.deleteCategory(id)
        RecipeToDietsTable.deleteDiets(id)
        RecipeToPreparationTable.deletePreparation(id)
        RecipeTable.deleteWhere { RecipeTable.id eq id }
    }

    fun getRecipeWithId(id: Int) = transaction {
        selectAll().where { RecipeTable.id eq id }.first().toRecipe()
    }


    fun getRecipes(q: String, limit: Int, offset: Int) = transaction {
        selectAll().limit(count = limit).offset(start = offset.toLong()).where {
            (title.lowerCase() like "%${q.lowercase()}%")
        }
    }

    private fun ResultRow.toRecipe(): RecipeDTO {
        val id = this[id].value
        val ingredients = IngredientTable.getIngredients(id)
        val steps = StepTable.getSteps(id)

        val category = RecipeToCategoryTable.getTag(id)
        val diets = RecipeToDietsTable.getTag(id)
        val preparation = RecipeToPreparationTable.getTag(id)

        return RecipeDTO(
            id = id,
            author = UserTable.getUser(this[userId].value).toProfile(),
            title = this[title],
            description = this[description],
            largeImage = this[smallImage],
            smallImage = this[largeImage],
            cookingTime = this[cookingTime],
            kcal = this[kcal],
            protein = this[protein],
            fat = this[fat],
            carb = this[carb],
            ingredients = ingredients,
            steps = steps,
            category = category,
            dietsType = diets,
            preparation = preparation,
        )
    }

}