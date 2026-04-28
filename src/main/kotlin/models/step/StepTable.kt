package ru.topbun.models.step

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.topbun.features.recipe.entity.StepReceive
import ru.topbun.models.recipe.RecipeTable

object StepTable: IntIdTable("steps") {

    val recipeId = reference("recipe_id", RecipeTable)
    val description = text("description")
    val previewUrl = text("preview_url").nullable()

    fun addStep(recipeId: Int, step: StepReceive) = transaction {
        insert {
            it[StepTable.recipeId] = recipeId
            it[StepTable.description] = step.description
            it[StepTable.previewUrl] = step.previewUrl
        }
    }

    fun addStep(recipeId: Int, steps: List<StepReceive>) = steps.forEach { addStep(recipeId, it) }


    fun deleteSteps(recipeId: Int) = transaction {
        deleteWhere { StepTable.recipeId eq recipeId }
    }

    fun getSteps(recipeId: Int) = transaction {
        selectAll().where{ StepTable.recipeId eq recipeId }.map { it.toStep() }
    }

    private fun ResultRow.toStep() = StepDTO(
        id = this[id].value,
        description = this[description],
        previewUrl = this[previewUrl]
    )


}