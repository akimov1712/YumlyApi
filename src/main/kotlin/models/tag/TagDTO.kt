package ru.topbun.models.category

import kotlinx.serialization.Serializable

@Serializable
data class TagDTO(
    val id: Int,
    val name: String,
    val icon: String
)
