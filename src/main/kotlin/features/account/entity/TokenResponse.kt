package ru.topbun.features.account.entity

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    val token: String
)