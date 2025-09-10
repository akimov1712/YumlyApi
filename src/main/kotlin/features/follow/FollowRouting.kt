package ru.topbun.features.follow

import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.configureFollowRouting(){
    routing {
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
}