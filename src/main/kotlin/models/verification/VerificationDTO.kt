package ru.topbun.models.verification

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import models.verification.VerificationType

@Serializable
data class VerificationDTO(
    val id: Int,
    val userId: Int,
    val code: String,
    val confirmed : Boolean,
    val type: VerificationType,
    val expiresAt: LocalDateTime
)
