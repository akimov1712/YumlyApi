package ru.topbun.features.verification.entity

import kotlinx.serialization.Serializable

@Serializable
data class VerificationStatusResponse(
    val status: VerificationStatusType
)
