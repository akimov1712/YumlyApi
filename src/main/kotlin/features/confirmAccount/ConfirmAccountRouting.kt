package ru.topbun.features.confirmAccount

import io.ktor.server.routing.Route
import io.ktor.server.routing.Routing
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.configureConfirmAccountRouting(){
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