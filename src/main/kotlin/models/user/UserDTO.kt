package models.user

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val id: Int,
    val username: String,
    val email: String,
    val password: String,
    val photoUrl: String?,
    val isVerified : Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)