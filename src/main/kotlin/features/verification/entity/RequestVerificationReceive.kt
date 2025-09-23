package ru.topbun.features.verification.entity

import kotlinx.serialization.Serializable
import models.verification.VerificationType

@Serializable
data class RequestVerificationReceive(
    val email: String,
    val type: VerificationType
)
