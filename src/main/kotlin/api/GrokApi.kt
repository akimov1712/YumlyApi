package ru.topbun.api

import io.ktor.client.request.post
import io.ktor.client.request.setBody
import ru.topbun.api.entity.GrokChatRequest
import ru.topbun.api.entity.appendSystemRoleMessage

class GrokApi(
    private val api: ApiFactory = ApiFactory
) {

    suspend fun sendMessage(request: GrokChatRequest) = api.client.post("https://api.x.ai/v1/chat/completions") {
        setBody(request.appendSystemRoleMessage())
    }

}
