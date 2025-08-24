package ru.topbun.plugins

import features.account.configureAccountRouting
import features.signUp.configureSignUpRouting
import io.ktor.server.application.Application

fun Application.configureRouting() {
    configureSignUpRouting()
    configureAccountRouting()
}
