package features.account

import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingCall
import ru.topbun.features.account.entity.UpdateAccountInfoReceive
import ru.topbun.models.user.UserTable
import ru.topbun.utills.AppException
import ru.topbun.utills.Error
import ru.topbun.utills.getUserFromToken
import ru.topbun.utills.wrapperException

class AccountController(
    private val call: RoutingCall
) {

    suspend fun updateInfo(){
        call.wrapperException {
            val user = call.getUserFromToken()
            val newInfo = call.receive<UpdateAccountInfoReceive>()
            if (newInfo.isValid()){
                val newUser = UserTable.updateUser(
                    id = user.id,
                    username = newInfo.username,
                    photoUrl = newInfo.photoUrl,
                ) ?: throw AppException(HttpStatusCode.NotFound, Error.USER_NOT_FOUND)
                call.respond(newUser)
            }
        }
    }

    suspend fun accountInfo(){
        call.wrapperException{
            val user = call.getUserFromToken()
            call.respond(user)
        }
    }

}