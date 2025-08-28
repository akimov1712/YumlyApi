package ru.topbun.features.confirmAccount

import features.account.AccountController
import io.ktor.server.application.Application
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.configureConfirmAccountRouting(){
    routing {
        route("/account"){
            post("/confirm"){
                val controller = ConfirmAccountController(call)
                controller.confirmAccount()
            }

        }
    }
}