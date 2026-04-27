package models.user

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import ru.topbun.models.favorite.FavoriteTable
import ru.topbun.models.follow.FollowTable
import ru.topbun.models.recipe.RecipeTable
import ru.topbun.models.user.ProfileDTO

@Serializable
data class UserDTO(
    val id: Int,
    val username: String,
    val email: String,
    val password: String,
    val photoUrl: String?,
    val isVerified : Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
){

    fun toProfile(requestUserId: Int?): ProfileDTO {
        return ProfileDTO(
            userId = id,
            username = username,
            photoUrl = photoUrl,
            email = email,
            countRecipes = RecipeTable.getRecipesCountByUserId(id).toInt(),
            countFollowing = FollowTable.getFollowingCount(id).toInt(),
            countFollowers = FollowTable.getFollowersCount(id).toInt(),
            countLikes = FavoriteTable.getCountLikes(id).toInt(),
            isFollow = requestUserId?.let { FollowTable.isFollowed(requestUserId, id) } ?: false
        )
    }

}