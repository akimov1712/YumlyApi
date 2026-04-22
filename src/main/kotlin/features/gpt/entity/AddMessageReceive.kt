package ru.topbun.features.gpt.entity

import kotlinx.serialization.Serializable

@Serializable
data class AddMessageReceive(
    val chatId: Int? = null,
    val text: String
)
