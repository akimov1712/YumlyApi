package ru.topbun.features.gpt

import io.ktor.client.call.body
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingCall
import ru.topbun.api.YandexGptApi
import ru.topbun.api.entity.YandexGptTransport
import ru.topbun.api.entity.response.YandexGptResponse
import ru.topbun.api.entity.toTransport
import ru.topbun.features.gpt.entity.AddMessageReceive
import ru.topbun.features.gpt.entity.GetChatsReceive
import ru.topbun.models.gpt.chat.GptChatTable
import ru.topbun.models.gpt.message.GptMessageRoleType
import ru.topbun.models.gpt.message.GptMessageTable
import ru.topbun.utills.AppException
import ru.topbun.utills.Env
import ru.topbun.utills.ErrorMessage
import ru.topbun.utills.getUserFromToken
import ru.topbun.utills.wrapperException

class GptController(
    private val call: RoutingCall
) {

    suspend fun getChats(){
        call.wrapperException {
            val user = call.getUserFromToken()
            val receive = call.receive<GetChatsReceive>()
            val chats = GptChatTable.getChats(userId = user.id, limit = receive.limit, offset = receive.offset).map { it.compress().toResponse() }
            call.respond(chats)
        }
    }

    suspend fun getChat(){
        call.wrapperException {
            val user = call.getUserFromToken()
            val chatId = call.parameters["id"]?.toIntOrNull() ?: throw AppException(HttpStatusCode.BadRequest, ErrorMessage.PARAMS_ID)
            val chat = GptChatTable.getChat(chatId) ?: throw AppException(HttpStatusCode.NotFound, ErrorMessage.CHAT_NOT_FOUND)
            if (user.id != chat.userId) throw AppException(HttpStatusCode.Forbidden, ErrorMessage.FORBIDDEN)
            call.respond(chat.toResponse())
        }
    }

    suspend fun sendMessage(){
        call.wrapperException {
            val api = YandexGptApi()

            val user = call.getUserFromToken()
            val receive = call.receive<AddMessageReceive>()
            val chatId = receive.chatId ?: GptChatTable.addChat(user.id)
            GptMessageTable.addMessage(chatId, GptMessageRoleType.USER, receive.text)

            val messages = GptMessageTable.getMessages(chatId)
            if (messages.count() > Env["GPT_MAX_MESSAGE"].toInt()) throw AppException(HttpStatusCode.BadRequest, ErrorMessage.GPT_MAX_MESSAGE)
            val yandexGptTransport = YandexGptTransport(messages = messages.toTransport())
            val response = api.sendMessage(yandexGptTransport)
            if (response.status != HttpStatusCode.OK) {
                println(response.bodyAsText())
                throw AppException(HttpStatusCode.BadRequest, ErrorMessage.ERROR_GPT_REQUEST)
            }

            val gptResponse = response.body<YandexGptResponse>()
            val assistantMessage = gptResponse.result.alternatives.last().message
            GptMessageTable.addMessage(chatId = chatId, role = GptMessageRoleType.fromString(assistantMessage.role), assistantMessage.text)

            val chat = GptChatTable.getChat(chatId) ?: throw AppException(HttpStatusCode.NotFound, ErrorMessage.CHAT_NOT_FOUND)
            call.respond(chat.toResponse())
        }
    }

}