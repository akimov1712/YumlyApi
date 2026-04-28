package ru.topbun.plugins

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.request.contentType
import io.ktor.server.request.httpMethod
import io.ktor.server.request.path
import io.ktor.server.response.respond
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.slf4j.event.Level
import models.error.ErrorDTO

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(
            Json {
                ignoreUnknownKeys = true
                encodeDefaults = true
            }
        )
    }
}

fun Application.configureLogging() {
    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path() != "/health" }
    }
}
