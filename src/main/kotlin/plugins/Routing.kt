package ru.topbun.plugins

import features.account.configureAccountRouting
import features.signUp.configureSignUpRouting
import features.upload.configureUploadRouting
import io.ktor.server.application.Application
import ru.topbun.features.confirmAccount.configureConfirmAccountRouting
import ru.topbun.features.login.configureLoginRouting
import ru.topbun.features.recipe.configureRecipeRouting
import ru.topbun.features.resetPassword.configureResetPasswordRouting

fun Application.configureRouting() {
    configureAccountRouting()
    configureConfirmAccountRouting()
    configureLoginRouting()
    configureRecipeRouting()
    configureResetPasswordRouting()
    configureSignUpRouting()
    configureUploadRouting()
}
