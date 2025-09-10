package ru.topbun.models.notification

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import models.user.UserDTO
import ru.topbun.models.recipe.RecipeDTO

@Serializable
data class NotificationDTO(
    val id: Int,
    val type: NotificationType,
    val initiator: UserDTO,
    val recipe: RecipeDTO?,
    val createdAt: LocalDateTime
)
