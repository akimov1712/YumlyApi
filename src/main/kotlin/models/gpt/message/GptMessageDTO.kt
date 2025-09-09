package ru.topbun.models.gpt.message

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class GptMessageDTO(
    val id: Int,
    val role: GptMessageRoleType,
    val text: String,
    val createdAt: LocalDateTime
)
