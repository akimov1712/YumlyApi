package ru.topbun.plugins

import features.account.configureAccountRouting
import features.signUp.configureSignUpRouting
import features.upload.configureUploadRouting
import io.ktor.server.application.Application
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import ru.topbun.features.confirmAccount.configureConfirmAccountRouting
import ru.topbun.features.favorite.configureFavoriteRouting
import ru.topbun.features.follow.configureFollowRouting
import ru.topbun.features.gpt.configureGptRouting
import ru.topbun.features.history.configureHistoryRouting
import ru.topbun.features.login.configureLoginRouting
import ru.topbun.features.notification.configureNotificationRouting
import ru.topbun.features.recipe.configureRecipeRouting
import ru.topbun.features.resetPassword.configureResetPasswordRouting

fun Application.configureRouting() {
    routing {
        route("/v1"){
            configureAccountRouting()
            configureConfirmAccountRouting()
            configureLoginRouting()
            configureRecipeRouting()
            configureResetPasswordRouting()
            configureSignUpRouting()
            configureUploadRouting()
            configureHistoryRouting()
            configureFavoriteRouting()
            configureGptRouting()
            configureNotificationRouting()
            configureFollowRouting()
        }
    }
}
