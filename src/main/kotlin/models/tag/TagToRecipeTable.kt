package ru.topbun.models.tag

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.topbun.models.recipe.RecipeTable

object TagToRecipeTable: IntIdTable("tag_to_recipe") {

    val recipeId = reference("recipe_id", RecipeTable)
    val tagId = reference("tag_id", TagTable)

    fun recipeContainTag(recipeId: Int, tags: List<Int>) = transaction {
        if (tags.isEmpty()) return@transaction true
        val tagIds = selectAll().where { (TagToRecipeTable.recipeId eq recipeId)}.map { it[TagToRecipeTable.tagId].value }
        tags.all { tagIds.contains(it) }
    }

    fun delete(recipeId: Int) =
        transaction { deleteWhere { TagToRecipeTable.recipeId eq recipeId } }


    fun getTags(recipeId: Int) = transaction {
        selectAll().where { TagToRecipeTable.recipeId eq recipeId }.map {
            val id = it[tagId].value
            TagTable.getTag(id)
        }
    }

    fun addTagToRecipe(recipeId: Int, tagId: Int) = transaction {
        insert {
            it[TagToRecipeTable.recipeId] = recipeId
            it[TagToRecipeTable.tagId] = tagId
        }
    }

}