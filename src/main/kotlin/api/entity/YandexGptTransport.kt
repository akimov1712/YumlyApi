package ru.topbun.api.entity

import kotlinx.serialization.Serializable
import ru.topbun.models.gpt.GptMessageDTO
import ru.topbun.utills.Env

@Serializable
data class YandexGptTransport(
    val modelUri: String = "gpt://${Env["GPT_ID_FOLDER"]}/yandexgpt",
    val completionOptions: CompletionOptions = CompletionOptions(),
    val messages: List<GptMessageDTO>
)