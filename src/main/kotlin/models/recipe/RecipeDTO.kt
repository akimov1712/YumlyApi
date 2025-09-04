package ru.topbun.models.recipe

import TagDTO
import kotlinx.serialization.Serializable
import ru.topbun.models.ingredient.IngredientDTO
import ru.topbun.models.step.StepDTO
import ru.topbun.models.user.ProfileDTO

@Serializable
data class RecipeDTO(
    val id: Int,
    val author: ProfileDTO?,
    val title: String,
    val description: String?,
    val largeImage: String?,
    val smallImage: String?,
    val isFavorite: Boolean = false,
    val difficulty: Difficulty,
    val cookingTime: Int,
    val kcal: Int,
    val protein: Double,
    val fat: Double,
    val carb: Double,
    val tags: List<TagDTO>,
    val ingredients: List<IngredientDTO>,
    val steps: List<StepDTO>,
)
