package models.tag.preparation

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.topbun.models.recipe.RecipeTable
import ru.topbun.models.tag.categories.RecipeToCategoryTable.categoryId
import ru.topbun.models.tag.preparation.PreparationsTable

object RecipeToPreparationTable: IntIdTable("recipe_to_preparation") {
    
    val recipeId = reference("recipe_id", RecipeTable)
    val preparationId = reference("preparation_id", PreparationsTable)

    fun recipeContainTag(recipeId: Int, tagId: Int) = transaction {
        selectAll().where { (RecipeToPreparationTable.recipeId eq recipeId) and (preparationId eq tagId) }.count() > 0
    }

    fun deletePreparation(recipeId: Int) = transaction { deleteWhere { RecipeToPreparationTable.recipeId eq recipeId } }

    fun getTag(recipeId: Int) = transaction {
        val categoryId = select(categoryId).where { RecipeToPreparationTable.recipeId eq recipeId }.firstOrNull()?.get(categoryId)?.value ?: return@transaction null
        PreparationsTable.getTag(categoryId)
    }

    fun addRecipe(recipeId: Int, categoryId: Int) = transaction {
        insert {
            it[RecipeToPreparationTable.recipeId] = recipeId
            it[RecipeToPreparationTable.preparationId] = categoryId
        }
    }


}