package ru.topbun.features.follow.entity

import kotlinx.serialization.Serializable
import models.user.UserDTO

@Serializable
data class FollowersResponse(
    val count: Int,
    val follows: List<UserDTO>
)
