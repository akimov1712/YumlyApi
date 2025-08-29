package ru.topbun.models.ingredient

import kotlinx.serialization.Serializable

@Serializable
data class IngredientDTO(
    val id: Int,
    val recipeId: Int,
    val name: String,
    val value: String,
)
