package features.account

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

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
                }
            }
        }
    }
}