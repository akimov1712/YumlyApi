package ru.topbun.features.recipe.entity

import kotlinx.serialization.Serializable

@Serializable
data class IngredientReceive(
    val name: String,
    val value: String,
    )
