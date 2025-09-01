package ru.topbun.features.recipe.entity

import kotlinx.serialization.Serializable

@Serializable
data class GetRecipeReceive(
    val q: String = "",
    val offset: Int = 0,
    val limit: Int = 20
)
