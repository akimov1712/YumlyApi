package ru.topbun.models.gpt.chat

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import ru.topbun.models.gpt.message.GptMessageDTO

@Serializable
data class GptChatDTO(
    val id: Int,
    val userId: Int,
    val messages: List<GptMessageDTO>,
    val createdAt: LocalDateTime,
)
