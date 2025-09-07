package features.account

import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.topbun.features.account.entity.UpdateAccountInfoReceive
import ru.topbun.models.user.UserTable
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

    suspend fun updateInfo(){
        call.wrapperException {
            val user = call.getUserFromToken()
            val newInfo = call.receive<UpdateAccountInfoReceive>()
            if (newInfo.isValid()){
                val newUser = UserTable.updateUser(
                    id = user.id,
                    username = newInfo.username,
                    photoUrl = newInfo.photoUrl,
                )
                call.respond(newUser)
            }
        }
    }

}