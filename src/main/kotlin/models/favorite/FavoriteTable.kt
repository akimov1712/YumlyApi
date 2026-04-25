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
import ru.topbun.models.recipe.RecipeTable
import ru.topbun.models.user.UserTable

object FavoriteTable: IntIdTable("favorite") {

    val userId = reference("user_id", UserTable)
    val authorId = reference("author_id", UserTable)
    val recipeId = reference("recipe_id", RecipeTable)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)

    fun getFavoriteRecipes(userId: Int, limit: Int, offset: Int) = transaction {
        selectAll()
            .where { FavoriteTable.userId eq userId }
            .orderBy(FavoriteTable.createdAt, SortOrder.DESC)
            .limit(limit).offset(offset.toLong())
            .map { it[recipeId].value }
            .map { RecipeTable.getRecipeById(it, userId) }
    }

    fun getCountLikes(authorId: Int) = transaction {
        selectAll().where { FavoriteTable.authorId eq authorId }.count()

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
        val authorId = RecipeTable.getRecipeById(recipeId)?.author?.userId  ?: return@transaction
        insert {
            it[FavoriteTable.userId] = userId
            it[FavoriteTable.recipeId] = recipeId
            it[FavoriteTable.authorId] = authorId
        }
    }

    private fun deleteFavorite(userId: Int, recipeId: Int) = transaction {
        deleteWhere { (FavoriteTable.userId eq userId) and (FavoriteTable.recipeId eq recipeId) }
    }

    fun deleteRecipe(recipeId: Int) = transaction {
        deleteWhere { FavoriteTable.recipeId eq recipeId }
    }

}