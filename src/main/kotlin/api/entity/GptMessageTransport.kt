package ru.topbun.api.entity

import kotlinx.serialization.Serializable
import ru.topbun.models.gpt.message.GptMessageRoleType

@Serializable
data class GptMessageTransport(
    val role: GptMessageRoleType,
    val text: String,
)
