package ru.topbun.features.notification.entity

import kotlinx.serialization.Serializable

@Serializable
data class GetNotificationsReceive(
    val limit: Int,
    val offset: Int,
)