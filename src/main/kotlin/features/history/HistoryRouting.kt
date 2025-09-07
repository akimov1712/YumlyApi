package ru.topbun.features.history

import io.ktor.server.application.Application
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.configureHistoryRouting(){
    routing {
        route("/history"){
            get("/top") {
                val controller = HistoryController(call)
                controller.getTopQueries()
            }
        }
    }
}