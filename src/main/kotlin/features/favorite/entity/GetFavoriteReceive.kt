package ru.topbun.features.favorite.entity

import kotlinx.serialization.Serializable

@Serializable
data class GetFavoriteReceive(
    val limit: Int = 20,
    val offset: Int = 0,
)