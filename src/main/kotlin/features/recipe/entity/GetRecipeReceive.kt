package ru.topbun.features.recipe.entity

import kotlinx.serialization.Serializable
import ru.topbun.models.recipe.Difficulty

@Serializable
data class GetRecipeReceive(
    val q: String = "",
    val offset: Int = 0,
    val limit: Int = 20,
    val recipeFilter: RecipeFilter? = null
){

    @Serializable
    data class RecipeFilter(
        val tagIds: List<Int> = emptyList(),
        val cookingTime: Int? = null,
        val minKcal: Int? = null,
        val maxKcal: Int? = null,
        val difficulty: Difficulty? = null
    )

}
