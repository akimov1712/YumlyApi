package ru.topbun.features.resetPassword

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Route.configureResetPasswordRouting(){
    route("/reset") {
        post("/request") {
            val controller = ResetPasswordController(call)
            controller.request()
        }
            post("/confirm") {
            val controller = ResetPasswordController(call)
            controller.confirm()
        }
    }
}