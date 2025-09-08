package ru.topbun.models.ingredient

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.topbun.features.recipe.entity.IngredientReceive
import ru.topbun.models.recipe.RecipeTable

object IngredientTable: IntIdTable("ingredients") {

    val recipeId = reference("recipe_id", RecipeTable)
    val name = text("name")
    val value = text("value")

    fun addIngredient(recipeId: Int, ingredient: IngredientReceive) = transaction {
        insert {
            it[IngredientTable.recipeId] = recipeId
            it[IngredientTable.name] = ingredient.name
            it[IngredientTable.value] = ingredient.value
        }
    }

    fun addIngredient(recipeId: Int, ingredients: List<IngredientReceive>) = ingredients.forEach { addIngredient(recipeId, it) }

    fun deleteIngredients(recipeId: Int) = transaction {
        deleteWhere { IngredientTable.recipeId eq recipeId }
    }

    fun getIngredients(recipeId: Int) = transaction {
        selectAll().where{ IngredientTable.recipeId eq recipeId }.map { it.toIngredient() }
    }

    private fun ResultRow.toIngredient() = IngredientDTO(
        id = this[id].value,
        name = this[name],
        value = this[value]
    )

}