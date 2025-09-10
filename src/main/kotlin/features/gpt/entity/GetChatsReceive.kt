package ru.topbun.features.gpt.entity

import kotlinx.serialization.Serializable

@Serializable
data class GetChatsReceive(
    val offset: Int,
    val limit: Int
)
