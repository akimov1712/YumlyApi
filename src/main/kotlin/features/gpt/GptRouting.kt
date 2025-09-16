package ru.topbun.features.gpt

import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.Route
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Route.configureGptRouting(){
    authenticate {
        route("/gpt"){
            post {
                val controller = GptController(call)
                controller.getChats()
            }
            get("/{id}"){
                val controller = GptController(call)
                controller.getChat()
            }
            post("/send") {
                val controller = GptController(call)
                controller.sendMessage()
            }
        }
    }
}