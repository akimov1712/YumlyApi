package ru.topbun

import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import ru.topbun.plugins.configureDatabases
import ru.topbun.plugins.configureLogging
import ru.topbun.plugins.configureRouting
import ru.topbun.plugins.configureSecurity
import ru.topbun.plugins.configureSerialization

fun main() {
    embeddedServer(Netty, port = 3000, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureLogging()
    configureSerialization()
    configureDatabases()
    configureSecurity()
    configureRouting()
}
