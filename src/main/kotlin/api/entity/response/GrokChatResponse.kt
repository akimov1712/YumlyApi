package ru.topbun.api.entity.response

import kotlinx.serialization.Serializable

@Serializable
data class GrokChatResponse(
    val choices: List<GrokChoiceDTO>,
)
