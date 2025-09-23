package features.account

import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.Route
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Route.configureAccountRouting(){
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
            put("/reset-password"){
                val accountController = AccountController(call)
                accountController.resetPassword()
            }
        }
        route("/profile"){
            get("/info/{id}"){
                val accountController = AccountController(call)
                accountController.profileInfo()
            }
        }
    }

}