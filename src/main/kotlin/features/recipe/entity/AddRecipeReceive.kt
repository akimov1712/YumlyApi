package ru.topbun.features.recipe.entity

import io.ktor.http.HttpStatusCode
import kotlinx.serialization.Serializable
import ru.topbun.models.ingredient.IngredientDTO
import ru.topbun.models.step.StepDTO
import ru.topbun.utills.AppException
import ru.topbun.utills.ErrorMessage

@Serializable
data class AddRecipeReceive(
    val id: Int,
    val title: String,
    val description: String?,
    val previewUrl: String?,
    val cookingTime: Int,
    val kcal: Int,
    val protein: Double,
    val fat: Double,
    val carb: Double,
    val ingredients: List<IngredientDTO>,
    val steps: List<StepDTO>,
    val tagIds: List<Int>,
){

    fun isValid(): Boolean{
        if (title.length > 48) throw AppException(HttpStatusCode.Conflict, ErrorMessage.LENGTH_TITLE)
        if ((description?.length ?: 0) > 500) throw AppException(HttpStatusCode.Conflict, ErrorMessage.LENGTH_DESCR)
        if (cookingTime > 14400) throw AppException(HttpStatusCode.Conflict, ErrorMessage.COUNT_COOKING_TIME)
        if (ingredients.size !in (1..32)) throw AppException(HttpStatusCode.Conflict, ErrorMessage.COUNT_INGREDIENTS)
        if (steps.size !in (1..32)) throw AppException(HttpStatusCode.Conflict, ErrorMessage.COUNT_STEPS)
        if (listOf(protein,carb, fat).any{ it > 5000 }) throw AppException(HttpStatusCode.Conflict, ErrorMessage.COUNT_NUTRIENTS)
        return true
    }

}
