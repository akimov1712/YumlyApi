package ru.topbun.features.gpt.entity

import kotlinx.serialization.Serializable
import ru.topbun.models.gpt.chat.GptChatDTO
import ru.topbun.utills.Env
import ru.topbun.utills.ErrorMessage.GPT_MAX_MESSAGE

@Serializable
data class GptChatResponse(
    val maxLimitMessages: Int = Env["GPT_MAX_MESSAGE"].toInt(),
    val chat: GptChatDTO
)
