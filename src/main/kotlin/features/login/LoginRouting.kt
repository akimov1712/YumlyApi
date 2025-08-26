package ru.topbun.features.login

import io.ktor.server.application.Application
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import ru.topbun.features.signUp.SignUpController

fun Application.configureLoginRouting(){
    routing {
        post("/login") {
            val controller = LoginController(call)
            controller.login()
        }
    }
}