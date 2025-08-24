package features.signUp.entity

import kotlinx.serialization.Serializable

@Serializable
data class SignUpResponse(
    val token: String
)