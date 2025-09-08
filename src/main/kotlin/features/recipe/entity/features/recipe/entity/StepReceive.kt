package ru.topbun.features.recipe.entity.features.recipe.entity

import kotlinx.serialization.Serializable

@Serializable
data class StepReceive(
    val description: String,
    val previewUrl: String?
)
