package ru.topbun.api.entity.response

import kotlinx.serialization.Serializable

@Serializable
data class YandexGptResponse(
    val result: YandexGptResultDTO,
)