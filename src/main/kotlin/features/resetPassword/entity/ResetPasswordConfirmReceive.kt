package features.resetPassword.entity

import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordConfirmReceive(
    val email: String,
    val code: String,
    val newPassword: String
)