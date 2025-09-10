package ru.topbun.api.entity.response

import kotlinx.serialization.Serializable

@Serializable
data class YandexGptResultDTO(
    val alternatives: List<YandexGptMessagesDTO>
)
