package ru.topbun.features.recipe.entity

import io.ktor.http.HttpStatusCode
import kotlinx.serialization.Serializable
import ru.topbun.features.recipe.entity.StepReceive
import ru.topbun.utills.AppException
import ru.topbun.utills.ErrorMessage

@Serializable
data class AddRecipeReceive(
    val title: String,
    val description: String? = null,
    val previewUrl: String? = null,
    val cookingTime: Int,
    val kcal: Int,
    val protein: Double,
    val fat: Double,
    val carb: Double,
    val ingredients: List<IngredientReceive>,
    val steps: List<StepReceive>,
    val tagIds: List<Int>,
){

    fun isValid(): Boolean{
        if (title.length > 72) throw AppException(HttpStatusCode.Conflict, ErrorMessage.LENGTH_TITLE)
        if ((description?.length ?: 0) > 500) throw AppException(HttpStatusCode.Conflict, ErrorMessage.LENGTH_DESCR)
        if (cookingTime > 14400) throw AppException(HttpStatusCode.Conflict, ErrorMessage.COUNT_COOKING_TIME)
        if (ingredients.size !in (1..32)) throw AppException(HttpStatusCode.Conflict, ErrorMessage.COUNT_INGREDIENTS)
        if (steps.size !in (1..32)) throw AppException(HttpStatusCode.Conflict, ErrorMessage.COUNT_STEPS)
        if (listOf(protein,carb, fat).any{ it > 5000 }) throw AppException(HttpStatusCode.Conflict, ErrorMessage.COUNT_NUTRIENTS)
        return true
    }

}
