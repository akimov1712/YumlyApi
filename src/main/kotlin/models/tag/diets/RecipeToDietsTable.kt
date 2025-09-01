package models.tag.diets

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import ru.topbun.models.recipe.RecipeTable
import ru.topbun.models.tag.categories.RecipeToCategoryTable.categoryId
import ru.topbun.models.tag.diets.DietsTable

object RecipeToDietsTable: IntIdTable("recipe_to_diets") {
    
    val recipeId = reference("recipe_id", RecipeTable)
    val dietsId = reference("diets_id", DietsTable)


    fun deleteDiets(recipeId: Int) = transaction { deleteWhere { RecipeToDietsTable.recipeId eq recipeId } }

    fun getTag(recipeId: Int) = transaction {
        val categoryId = select(categoryId).where { RecipeToDietsTable.recipeId eq recipeId }.first()[categoryId].value
        DietsTable.getTag(categoryId)
    }

    fun addRecipe(recipeId: Int, categoryId: Int) = transaction {
        insert {
            it[RecipeToDietsTable.recipeId] = recipeId
            it[RecipeToDietsTable.dietsId] = categoryId
        }
    }

}