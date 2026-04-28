package ru.topbun.features.gpt

import io.ktor.client.call.body
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingCall
import ru.topbun.api.GrokApi
import ru.topbun.api.entity.GrokChatRequest
import ru.topbun.api.entity.GptMessageTransport
import ru.topbun.api.entity.response.GrokChatResponse
import ru.topbun.api.entity.toTransport
import ru.topbun.features.gpt.entity.AddMessageReceive
import ru.topbun.features.gpt.entity.GetChatsReceive
import ru.topbun.models.gpt.chat.GptChatTable
import ru.topbun.models.gpt.message.GptMessageRoleType
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
            val api = GrokApi()

            val user = call.getUserFromToken()
            val receive = call.receive<AddMessageReceive>()
            val userText = receive.text.trim()
            if (userText.isEmpty()) throw AppException(HttpStatusCode.BadRequest, ErrorMessage.GPT_EMPTY_MESSAGE)

            val chat = receive.chatId?.let { chatId ->
                GptChatTable.getChat(chatId, user.id) ?: throw AppException(HttpStatusCode.NotFound, ErrorMessage.CHAT_NOT_FOUND)
            }

            val history = chat?.messages.orEmpty()
            if (history.count() + 2 > Env["GPT_MAX_MESSAGE"].toInt()) throw AppException(HttpStatusCode.BadRequest, ErrorMessage.GPT_MAX_MESSAGE)

            val requestMessages = history.toTransport() + GptMessageTransport(
                role = GptMessageRoleType.USER.toLowerString(),
                content = userText,
            )
            val grokChatRequest = GrokChatRequest(messages = requestMessages)
            val response = api.sendMessage(grokChatRequest)
            if (response.status.value !in 200..299) {
                println(response.bodyAsText())
                throw AppException(HttpStatusCode.BadRequest, ErrorMessage.ERROR_GPT_REQUEST)
            }

            val gptResponse = try {
                response.body<GrokChatResponse>()
            } catch (e: Exception) {
                throw AppException(HttpStatusCode.BadRequest, ErrorMessage.ERROR_GPT_REQUEST)
            }
            val assistantMessage = gptResponse.choices.firstOrNull()?.message
                ?: throw AppException(HttpStatusCode.BadRequest, ErrorMessage.ERROR_GPT_REQUEST)
            val assistantText = assistantMessage.content.trim()
            if (assistantText.isEmpty()) throw AppException(HttpStatusCode.BadRequest, ErrorMessage.ERROR_GPT_REQUEST)
            val assistantRole = try {
                GptMessageRoleType.fromString(assistantMessage.role)
            } catch (e: Exception) {
                throw AppException(HttpStatusCode.BadRequest, ErrorMessage.ERROR_GPT_REQUEST)
            }

            val updatedChat = GptChatTable.addExchange(
                userId = user.id,
                chatId = receive.chatId,
                userText = userText,
                assistantRole = assistantRole,
                assistantText = assistantText,
            ) ?: throw AppException(HttpStatusCode.NotFound, ErrorMessage.CHAT_NOT_FOUND)
            call.respond(updatedChat.toResponse())
        }
    }

}
