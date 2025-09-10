package ru.topbun.features.favorite.entity

import kotlinx.serialization.Serializable

@Serializable
data class GetFavoriteReceive(
    val userId: Int,
    val limit: Int,
    val offset: Int,
)