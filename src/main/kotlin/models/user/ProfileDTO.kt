package ru.topbun.models.user

import kotlinx.serialization.Serializable

@Serializable
data class ProfileDTO(
    val userId: Int,
    val username: String,
    val email: String,
    val photoUrl: String?,
    val countRecipes: Int,
    val countFollowing: Int,
    val countFollowers: Int,
    val countLikes: Int,
    val isFollow: Boolean
)
