package ru.topbun.features.resetPassword

import io.ktor.server.application.Application
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.configureResetPasswordRouting(){
    routing {
        route("/reset") {
            post("/request") {
                val controller = ResetPasswordController(call)
                controller.requestReset()
            }
            post("/confirm") {
                val controller = ResetPasswordController(call)
                controller.confirmReset()
            }
        }
    }
}