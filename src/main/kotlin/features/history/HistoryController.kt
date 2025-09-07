package ru.topbun.features.history

import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingCall
import ru.topbun.models.history.HistoryTable
import ru.topbun.utills.wrapperException

class HistoryController(
    val call: RoutingCall
) {

    suspend fun getTopQueries(){
        call.wrapperException {
            val top = HistoryTable.getTop5Query()
            call.respond(top)
        }
    }

}