package ru.topbun.features.notification

import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingCall
import ru.topbun.features.notification.entity.GetNotificationsReceive
import ru.topbun.models.notification.NotificationTable
import ru.topbun.utills.getUserFromToken
import ru.topbun.utills.wrapperException

class NotificationController(
    private val call: RoutingCall
) {

    suspend fun getNotifications(){
        call.wrapperException {
            val user = call.getUserFromToken()
            val receive = call.receive<GetNotificationsReceive>()
            val notifications = NotificationTable.getNotifications(user.id, receive.limit, receive.offset)
            call.respond(notifications)
        }
    }


}