package ru.topbun.models.recipe

import kotlinx.serialization.Serializable
import models.user.UserDTO
import ru.topbun.models.ingredient.IngredientDTO
import ru.topbun.models.step.StepDTO

@Serializable
data class RecipeDTO(
    val id: Int,
    val author: UserDTO,
    val title: String,
    val description: String?,
    val previewUrl: String,
    val cookingTime: Int,
    val kcal: Int,
    val protein: Double,
    val fat: Double,
    val carb: Double,
    val ingredients: List<IngredientDTO>,
    val steps: List<StepDTO>
)
