package ru.topbun.features.history

import io.ktor.server.application.Application
import io.ktor.server.routing.Route
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Route.configureHistoryRouting(){
    route("/history"){
        get("/top") {
            val controller = HistoryController(call)
            controller.getTopQueries()
        }
    }
}