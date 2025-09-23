package ru.topbun.features.account.entity

import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordReceive(
    val email: String,
    val newPassword: String
)
