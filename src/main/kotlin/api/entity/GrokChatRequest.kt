package ru.topbun.api.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.topbun.utills.Env

@Serializable
data class GrokChatRequest(
    val model: String = Env.getOrDefault("GROK_MODEL", "grok-4-1-fast-non-reasoning"),
    val messages: List<GptMessageTransport>,
    val stream: Boolean = false,
    @SerialName("max_tokens")
    val maxTokens: Int = 2000,
)

fun GrokChatRequest.appendSystemRoleMessage(): GrokChatRequest {
    val newMessages = listOf(GptMessageTransport.createSystemRoleMessage()) + this.messages
    return copy(messages = newMessages)
}
