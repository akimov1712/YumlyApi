package ru.topbun.features.verification

import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.configureVerificationRouting(){
    route("/verification"){
        post("/request") {
            val controller = VerificationController(call)
            controller.request()
        }

        post("/confirm"){
            val controller = VerificationController(call)
            controller.confirm()
        }
    }
}