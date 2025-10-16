package ru.topbun.models.notification

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import ru.topbun.models.recipe.RecipeDTO
import ru.topbun.models.user.ProfileDTO

@Serializable
data class NotificationDTO(
    val id: Int,
    val type: NotificationType,
    val initiator: ProfileDTO,
    val recipe: RecipeDTO?,
    val createdAt: LocalDateTime
)
