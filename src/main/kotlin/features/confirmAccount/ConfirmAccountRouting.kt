package ru.topbun.features.confirmAccount

import io.ktor.server.application.Application
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.configureConfirmAccountRouting(){
    routing {
        route("/verify"){
            post("/request") {
                val controller = ConfirmAccountController(call)
                controller.request()
            }

            post("/confirm"){
                val controller = ConfirmAccountController(call)
                controller.confirm()
            }
        }
    }
}