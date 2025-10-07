package ru.topbun.features.follow

import io.ktor.server.auth.authenticate
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.configureFollowRouting(){
    authenticate {
        route("/follow"){
            post("/followers") {
                val controller = FollowController(call)
                controller.getFollowers()
            }

            post("/following") {
                val controller = FollowController(call)
                controller.getFollowing()
            }
            post("/{id}"){
                val controller = FollowController(call)
                controller.switchFollowStatus()
            }
        }
    }
}