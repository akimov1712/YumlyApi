package ru.topbun.features.gpt.entity

import kotlinx.serialization.Serializable

@Serializable
data class AddMessageReceive(
    val chatId: Int,
    val text: String
)
