package ru.topbun.features.follow

import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.Route
import io.ktor.server.routing.Routing
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Route.configureFollowRouting(){
    route("/follow"){
        post("/followers") {
            val controller = FollowController(call)
            controller.getFollowers()
        }

        post("/following") {
            val controller = FollowController(call)
            controller.getFollowing()
        }

        authenticate {
            post("/{id}"){
                val controller = FollowController(call)
                controller.switchFollowStatus()
            }
        }
    }
}