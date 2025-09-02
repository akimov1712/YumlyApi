package ru.topbun.models.favorite

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.topbun.models.user.UserTable

object FavoriteTable: IntIdTable("favorite") {

    val userId = reference("user_id", UserTable)
    val recipeId = reference("recipe_id", UserTable)

    fun getFavoriteRecipeIds(userId: Int) = transaction {
        selectAll().where { FavoriteTable.userId eq userId }.map { it[FavoriteTable.recipeId].value }
    }

    fun isFavorite(userId: Int, recipeId: Int) = transaction {
        selectAll().where { (FavoriteTable.userId eq userId) and (FavoriteTable.recipeId eq recipeId) }.count() > 0
    }

    fun switchFavorite(userId: Int, recipeId: Int) = transaction {
        if (isFavorite(userId, recipeId)){
            deleteFavorite(userId, recipeId)
            false
        } else {
            addFavorite(userId, recipeId)
            true
        }
    }

    private fun addFavorite(userId: Int, recipeId: Int) = transaction {
        insert {
            it[FavoriteTable.userId] = userId
            it[FavoriteTable.recipeId] = recipeId
        }
    }

    private fun deleteFavorite(userId: Int, recipeId: Int) = transaction {
        deleteWhere { (FavoriteTable.userId eq userId) and (FavoriteTable.recipeId eq recipeId) }
    }

    fun deleteRecipe(recipeId: Int) = transaction {
        deleteWhere { FavoriteTable.recipeId eq recipeId }
    }

}