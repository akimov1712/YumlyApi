package ru.topbun.models.tag.categories

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.topbun.models.recipe.RecipeTable

object RecipeToCategoryTable : IntIdTable("recipe_to_category") {

    val recipeId = reference("recipe_id", RecipeTable)
    val categoryId = reference("category_id", CategoriesTable)

    fun recipeContainTag(recipeId: Int, tagId: Int) = transaction {
        selectAll().where { (RecipeToCategoryTable.recipeId eq recipeId) and (categoryId eq tagId) }.count() > 0
    }

    fun deleteCategory(recipeId: Int) =
        transaction { deleteWhere { RecipeToCategoryTable.recipeId eq recipeId } }

    fun getTag(recipeId: Int) = transaction {
        val categoryId = select(categoryId).where { RecipeToCategoryTable.recipeId eq recipeId }.firstOrNull()?.get(categoryId)?.value ?: return@transaction null
        CategoriesTable.getTag(categoryId)
    }

    fun addRecipe(recipeId: Int, categoryId: Int) = transaction {
        insert {
            it[RecipeToCategoryTable.recipeId] = recipeId
            it[RecipeToCategoryTable.categoryId] = categoryId
        }
    }

}