package ru.topbun.features.confirmAccount.entity

import kotlinx.serialization.Serializable

@Serializable
data class VerificationStatusResponse(
    val status: VerificationStatusType
)
