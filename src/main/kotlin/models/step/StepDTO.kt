package ru.topbun.models.step

import kotlinx.serialization.Serializable

@Serializable
data class StepDTO(
    val id: Int,
    val description: String,
    val previewUrl: String?
)
