package ru.topbun.features.login.entity

import kotlinx.serialization.Serializable

@Serializable
data class LoginReceive(
    val email: String,
    val password: String
)
