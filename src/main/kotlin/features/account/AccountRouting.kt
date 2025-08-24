package features.account

import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun Application.configureAccountRouting(){
    routing {
        authenticate {
            get("/account") {
                val accountController = AccountController(call)
                accountController.accountInfo()
            }
        }
    }
}