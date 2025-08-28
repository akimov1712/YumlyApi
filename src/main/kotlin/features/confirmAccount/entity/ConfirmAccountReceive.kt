package ru.topbun.features.confirmAccount.entity

import kotlinx.serialization.Serializable

@Serializable
data class ConfirmAccountReceive(
    val email: String,
    val code: String
)