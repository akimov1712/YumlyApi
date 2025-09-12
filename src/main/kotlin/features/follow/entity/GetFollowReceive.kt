package ru.topbun.features.follow.entity

import kotlinx.serialization.Serializable

@Serializable
data class GetFollowReceive(
    val followId: Int,
    val limit: Int = 20,
    val offset: Int = 0,
)