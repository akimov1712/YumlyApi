package ru.topbun.models.favorite

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.topbun.models.user.UserTable

object FavoriteTable: IntIdTable("favorite") {

    val userId = reference("user_id", UserTable)
    val recipeId = reference("recipe_id", UserTable)

    fun getFavoriteRecipeIds(userId: Int) = transaction {
        selectAll().where { FavoriteTable.userId eq userId }.map { it[FavoriteTable.recipeId].value }
    }

    fun deleteFavorite(recipeId: Int) = transaction {
        deleteWhere { FavoriteTable.recipeId eq recipeId }
    }

}