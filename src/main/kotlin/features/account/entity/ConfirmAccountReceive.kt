package ru.topbun.features.account.entity

import kotlinx.serialization.Serializable

@Serializable
data class ConfirmAccountReceive(
    val email: String,
    val code: String
)
