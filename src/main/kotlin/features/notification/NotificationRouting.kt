package ru.topbun.features.notification

import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.Route
import io.ktor.server.routing.Routing
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

fun Route.configureNotificationRouting(){
    authenticate {
        post("/notification") {
            val controller = NotificationController(call)
            controller.getNotifications()
        }
    }
}