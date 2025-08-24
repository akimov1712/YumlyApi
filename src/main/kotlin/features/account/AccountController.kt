package features.account

import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingCall
import ru.topbun.utills.getUserFromToken
import ru.topbun.utills.wrapperException

class AccountController(
    private val call: RoutingCall
) {

    suspend fun accountInfo(){
        call.wrapperException{
            val user = call.getUserFromToken()
            call.respond(user)
        }
    }

}