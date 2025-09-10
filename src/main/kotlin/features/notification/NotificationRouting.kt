package ru.topbun.features.notification

import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

fun Application.configureNotificationRouting(){
    routing {
        authenticate {
            post("/notification") {
                val controller = NotificationController(call)
                controller.getNotifications()
            }
        }
    }
}