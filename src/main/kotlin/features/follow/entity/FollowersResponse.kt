package ru.topbun.features.follow.entity

import kotlinx.serialization.Serializable
import models.user.UserDTO
import ru.topbun.models.user.ProfileDTO

@Serializable
data class FollowersResponse(
    val count: Int,
    val follows: List<ProfileDTO>
)
