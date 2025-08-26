package features.account

import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.configureAccountRouting(){
    routing {
        authenticate {
            route("/account"){
                route("/info"){
                    get {
                        val accountController = AccountController(call)
                        accountController.accountInfo()
                    }

                    put {
                        val accountController = AccountController(call)
                        accountController.updateInfo()
                    }

                    post("/confirm"){
                        val accountController = AccountController(call)
                        accountController.confirmAccount()
                    }
                }
            }
        }
    }
}