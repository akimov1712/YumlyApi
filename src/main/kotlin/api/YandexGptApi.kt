package ru.topbun.api

import io.ktor.client.request.post
import io.ktor.client.request.setBody
import ru.topbun.api.entity.YandexGptTransport


class YandexGptApi(
    private val api: ApiFactory = ApiFactory
) {

    suspend fun sendMessage(message: YandexGptTransport) = api.client.post("https://llm.api.cloud.yandex.net/foundationModels/v1/completion") {
        setBody(message)
    }

}