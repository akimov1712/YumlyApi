package ru.topbun.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import ru.topbun.utills.Env

object ApiFactory {

    val client = HttpClient(CIO) {
        install(HttpTimeout) {
            requestTimeoutMillis = 60 * 1000
            socketTimeoutMillis = 60 * 1000
        }
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    encodeDefaults = true
                }
            )
        }

        defaultRequest {
            contentType(ContentType.Application.Json.withParameter("charset", "utf-8"))
            headers[HEADER_NAME_AUTH] = "$HEADER_VALUE_BEARER ${Env["XAI_API_KEY"]}"
        }
    }

    const val HEADER_NAME_AUTH = "Authorization"
    const val HEADER_VALUE_BEARER = "Bearer"

}
