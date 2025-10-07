package ru.topbun.features.follow

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingCall
import ru.topbun.features.follow.entity.FollowersResponse
import ru.topbun.features.follow.entity.GetFollowReceive
import ru.topbun.models.follow.FollowTable
import ru.topbun.models.notification.NotificationTable
import ru.topbun.models.notification.NotificationType
import ru.topbun.models.user.UserTable
import ru.topbun.utills.AppException
import ru.topbun.utills.ErrorMessage
import ru.topbun.utills.getUserFromToken
import ru.topbun.utills.wrapperException

class FollowController(
    private val call: RoutingCall
) {

    suspend fun switchFollowStatus(){
        call.wrapperException {
            val user = call.getUserFromToken()
            val followingId = call.parameters["id"]?.toIntOrNull() ?: throw AppException(HttpStatusCode.BadRequest, ErrorMessage.PARAMS_ID)
            if (user.id == followingId) throw AppException(HttpStatusCode.BadRequest, ErrorMessage.SELF_FOLLOW)
            val isFollowed = FollowTable.switchFollow(user.id, followingId)
            if (isFollowed) NotificationTable.addNotification(user.id, NotificationType.FOLLOW, followingId)
            call.respond(isFollowed)
        }
    }

    suspend fun getFollowers(){
        call.wrapperException {
            val tokenPrincipal = call.principal<JWTPrincipal>()
            val userId = call.getUserFromToken().takeIf { tokenPrincipal != null }?.id
            val receive = call.receive<GetFollowReceive>()
            val followersIds = FollowTable.getFollowersIds(receive.followId, receive.limit, receive.offset)
            val followResponse = FollowersResponse(
                count = FollowTable.getFollowersCount(receive.followId).toInt(),
                follows = followersIds.map { UserTable.getUser(it).toProfile(userId) }
            )
            call.respond(followResponse)
        }
    }

    suspend fun getFollowing(){
        call.wrapperException {
            val tokenPrincipal = call.principal<JWTPrincipal>()
            val userId = call.getUserFromToken().takeIf { tokenPrincipal != null }?.id
            val receive = call.receive<GetFollowReceive>()
            val followingIds = FollowTable.getFollowingIds(receive.followId, receive.limit, receive.offset)
            val followResponse = FollowersResponse(
                count = FollowTable.getFollowingCount(receive.followId).toInt(),
                follows = followingIds.map { UserTable.getUser(it).toProfile(userId) }
            )
            call.respond(followResponse)
        }
    }

}