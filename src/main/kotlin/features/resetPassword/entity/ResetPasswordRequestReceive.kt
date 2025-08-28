package features.resetPassword.entity

import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordRequestReceive(val email: String)