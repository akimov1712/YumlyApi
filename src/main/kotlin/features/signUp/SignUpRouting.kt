package features.signUp

import io.ktor.server.application.Application
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import ru.topbun.features.signUp.SignUpController

fun Application.configureSignUpRouting() {
    routing {
        post("/signUp") {
            val signUpController = SignUpController(call)
            signUpController.signUp()
        }
    }
}