package ru.topbun.api.entity.response

import kotlinx.serialization.Serializable
import ru.topbun.api.entity.GptMessageTransport

@Serializable
data class GrokChoiceDTO(
    val message: GptMessageTransport,
)
