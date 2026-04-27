package ru.topbun.models.notification

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.topbun.models.favorite.FavoriteTable
import ru.topbun.models.recipe.RecipeTable
import ru.topbun.models.user.UserTable

object NotificationTable: IntIdTable("notifications") {

    val userId = reference("user_id", UserTable)
    val type = varchar("type", 32)
    val initiatorId = reference("initiator_id", UserTable)
    val recipeId = reference("recipe_id", RecipeTable).nullable()
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)


    fun deleteRecipe(recipeId: Int) = transaction {
        deleteWhere { NotificationTable.recipeId eq recipeId }
    }

    fun addNotification(userId: Int, type: NotificationType, initiatorId: Int, recipeId: Int? = null) = transaction {
        if (!containNotification(userId, type, initiatorId, recipeId)){
            insert {
                it[NotificationTable.userId] = userId
                it[NotificationTable.type] = type.toString()
                it[NotificationTable.initiatorId] = initiatorId
                it[NotificationTable.recipeId] = recipeId
            }
        }
    }

    fun containNotification(userId: Int, type: NotificationType, initiatorId: Int, recipeId: Int? = null) = transaction {
        selectAll().where {
            (NotificationTable.userId eq userId) and (NotificationTable.type eq type.toString()) and (NotificationTable.initiatorId eq initiatorId) and (NotificationTable.recipeId eq recipeId)
        }.count() > 0
    }

    fun getNotifications(userId: Int, limit: Int, offset: Int) = transaction {
        selectAll().where { NotificationTable.userId eq userId }
            .orderBy(createdAt, SortOrder.DESC)
            .limit(limit).offset(offset.toLong())
            .map { it.toDTO(userId) }
    }


    private fun ResultRow.toDTO(requestUserId: Int): NotificationDTO {
        val recipe = this[recipeId]?.let { RecipeTable.getRecipeById(it.value) }
        return NotificationDTO(
            id = this[id].value,
            type = NotificationType.valueOf(this[type]),
            initiator = UserTable.getUser(this[initiatorId].value).toProfile(requestUserId),
            recipe = recipe,
            createdAt = this[createdAt]
        )
    }

}