package ru.topbun.plugins

import features.account.configureAccountRouting
import features.signUp.configureSignUpRouting
import features.upload.configureUploadRouting
import io.ktor.server.application.Application
import ru.topbun.features.login.configureLoginRouting

fun Application.configureRouting() {
    configureSignUpRouting()
    configureAccountRouting()
    configureLoginRouting()
    configureUploadRouting()
}
