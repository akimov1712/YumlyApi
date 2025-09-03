package ru.topbun.features.recipe.entity

import kotlinx.serialization.Serializable
import ru.topbun.models.recipe.Difficulty

@Serializable
data class GetRecipeReceive(
    val q: String = "",
    val offset: Int = 0,
    val limit: Int = 20,
    val settings: Settings? = null
){

    @Serializable
    data class Settings(
        val tagIds: List<Int>,
        val cookingTime: Int? = null,
        val minKcal: Int? = null,
        val maxKcal: Int? = null,
        val difficulty: Difficulty? = null
    )

}
