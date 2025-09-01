package ru.topbun.models.user

import kotlinx.serialization.Serializable

@Serializable
data class ProfileDTO(
    val userId: Int,
    val username: String,
    val photoUrl: String?,
)
