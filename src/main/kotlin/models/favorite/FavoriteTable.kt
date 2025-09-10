package ru.topbun.models.favorite

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.topbun.models.follow.FollowTable
import ru.topbun.models.recipe.RecipeTable
import ru.topbun.models.user.UserTable

object FavoriteTable: IntIdTable("favorite") {

    val userId = reference("user_id", UserTable)
    val recipeId = reference("recipe_id", RecipeTable)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)

    fun getFavoriteRecipeIds(userId: Int, limit: Int, offset: Int) = transaction {
        selectAll()
            .orderBy(FollowTable.createdAt, SortOrder.DESC)
            .where { FavoriteTable.userId eq userId }
            .limit(limit).offset(offset.toLong())
            .map { it[FavoriteTable.recipeId].value }
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