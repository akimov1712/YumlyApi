package ru.topbun.features.notification.entity

import kotlinx.serialization.Serializable

@Serializable
data class GetNotificationsReceive(
    val limit: Int = 20,
    val offset: Int = 0,
)