package ru.topbun.api.entity

import kotlinx.serialization.Serializable
import ru.topbun.models.gpt.message.GptMessageDTO
import ru.topbun.models.gpt.message.GptMessageRoleType
import ru.topbun.utills.Env

@Serializable
data class GptMessageTransport(
    val role: String,
    val text: String,
){

    companion object{
        fun createSystemRoleMessage() = GptMessageTransport(
            role = GptMessageRoleType.SYSTEM.toString(),
            text = Env["GPT_SYSTEM_ROLE_TEXT"]
        )
    }

}


fun List<GptMessageDTO>.toTransport() = map {
    GptMessageTransport(it.role.toString(), it.text)
}