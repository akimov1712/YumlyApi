package ru.topbun.features.verification.entity

import kotlinx.serialization.Serializable
import models.verification.VerificationType

@Serializable
data class ConfirmVerificationReceive (
    val email: String,
    val code: String,
    val type: VerificationType
)