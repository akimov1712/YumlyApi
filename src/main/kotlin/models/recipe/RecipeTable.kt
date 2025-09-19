package ru.topbun.models.recipe

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import org.jetbrains.exposed.sql.lowerCase
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.topbun.features.recipe.entity.AddRecipeReceive
import ru.topbun.features.recipe.entity.GetRecipeReceive
import ru.topbun.models.favorite.FavoriteTable
import ru.topbun.models.ingredient.IngredientTable
import ru.topbun.models.step.StepTable
import ru.topbun.models.tag.TagToRecipeTable
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
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    val updatedAt = datetime("updated_at").defaultExpression(CurrentDateTime)

    fun addRecipe(userId: Int, recipe: AddRecipeReceive) = transaction {
        val id = insert {
            it[RecipeTable.userId] = userId
            it[RecipeTable.title] = recipe.title
            it[RecipeTable.description] = recipe.description
            it[RecipeTable.largeImage] = recipe.previewUrl
            it[RecipeTable.smallImage] = recipe.previewUrl
            it[RecipeTable.kcal] = recipe.kcal
            it[RecipeTable.cookingTime] = recipe.cookingTime
            it[RecipeTable.difficulty] = Difficulty.calculateDifficulty(recipe.cookingTime, recipe.ingredients.count()).toString()
            it[RecipeTable.protein] = recipe.protein
            it[RecipeTable.fat] = recipe.fat
            it[RecipeTable.carb] = recipe.carb
        }[RecipeTable.id].value

        IngredientTable.addIngredient(id, recipe.ingredients)
        StepTable.addStep(id, recipe.steps)

        recipe.tagIds.forEach { TagToRecipeTable.addTagToRecipe(id, it) }

        getRecipeWithId(id)
    }

    fun deleteRecipe(id: Int) = transaction {
        FavoriteTable.deleteRecipe(id)
        StepTable.deleteSteps(id)
        IngredientTable.deleteIngredients(id)
        TagToRecipeTable.delete(id)
        RecipeTable.deleteWhere { RecipeTable.id eq id }
    }

    fun getRecipeWithId(id: Int, requestUserId: Int? = null) = transaction {
        selectAll().where { RecipeTable.id eq id }.first().toRecipe(requestUserId)
    }


    fun getRecipes(
        q: String = "",
        limit: Int,
        offset: Int,
        recipeFilter: GetRecipeReceive.RecipeFilter?,
        requestUserId: Int? = null
    ) = transaction {
        var query = RecipeTable
            .selectAll()
            .limit(limit)
            .offset(offset.toLong())
            .apply {
                andWhere { RecipeTable.title.lowerCase() like "%${q.lowercase()}%" }
            }

        recipeFilter?.let { s ->
            s.minKcal?.let { query = query.andWhere { RecipeTable.kcal greaterEq it } }
            s.maxKcal?.let { query = query.andWhere { RecipeTable.kcal lessEq it } }
            s.cookingTime?.let { query = query.andWhere { RecipeTable.cookingTime lessEq it } }
            s.difficulty?.let { query = query.andWhere { RecipeTable.difficulty eq it.name } }
        }

        query.map { it.toRecipe(requestUserId) }.filter { recipe ->
            recipeFilter?.tagIds?.let { TagToRecipeTable.recipeContainTag(recipe.id, it) } ?: true
        }
    }

    private fun ResultRow.toRecipe(requestUserId: Int? = null): RecipeDTO {
        val id = this[id].value
        val userId = this[userId].value
        val ingredients = IngredientTable.getIngredients(id)
        val steps = StepTable.getSteps(id)

        val tags = TagToRecipeTable.getTags(id)

        val isFavorite = requestUserId?.let { FavoriteTable.isFavorite(it, id) } ?: false

        return RecipeDTO(
            id = id,
            author = UserTable.getUser(userId).toProfile(requestUserId),
            title = this[title],
            description = this[description],
            largeImage = this[smallImage],
            smallImage = this[largeImage],
            cookingTime = this[cookingTime],
            isFavorite = isFavorite,
            difficulty = Difficulty.valueOf(this[difficulty]),
            kcal = this[kcal],
            protein = this[protein],
            fat = this[fat],
            carb = this[carb],
            ingredients = ingredients,
            steps = steps,
            tags = tags
        )
    }

}