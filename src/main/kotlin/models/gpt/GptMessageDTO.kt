package ru.topbun.models.gpt

import kotlinx.serialization.Serializable

@Serializable
data class GptMessageDTO(
    val role: GptMessageRoleType,
    val text: String
)
