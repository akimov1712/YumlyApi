package ru.topbun.features.confirmAccount.entity

import kotlinx.serialization.Serializable

@Serializable
data class ConfirmAccountRequestReceive(val email: String)