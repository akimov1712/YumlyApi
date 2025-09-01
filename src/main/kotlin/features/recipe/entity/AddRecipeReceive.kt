package ru.topbun.features.recipe.entity

import kotlinx.serialization.Serializable
import ru.topbun.models.ingredient.IngredientDTO
import ru.topbun.models.recipe.Difficulty
import ru.topbun.models.step.StepDTO

@Serializable
data class AddRecipeReceive(
    val id: Int,
    val title: String,
    val description: String?,
    val previewUrl: String?,
    val cookingTime: Int,
    val difficulty: Difficulty,
    val kcal: Int,
    val protein: Double,
    val fat: Double,
    val carb: Double,
    val ingredients: List<IngredientDTO>,
    val steps: List<StepDTO>,
    val categoryId: Int?,
    val dietsTypeId: Int?,
    val preparationId: Int?,
)
