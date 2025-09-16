package features.signUp

import io.ktor.server.application.Application
import io.ktor.server.routing.Route
import io.ktor.server.routing.Routing
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import ru.topbun.features.signUp.SignUpController

fun Route.configureSignUpRouting() {
    post("/signUp") {
        val signUpController = SignUpController(call)
        signUpController.signUp()
    }
}