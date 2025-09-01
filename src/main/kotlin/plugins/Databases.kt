package ru.topbun.plugins

import io.ktor.server.application.Application
import models.verification.VerificationTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import ru.topbun.models.ingredient.IngredientTable
import ru.topbun.models.recipe.RecipeTable
import ru.topbun.models.step.StepTable
import ru.topbun.models.user.UserTable
import ru.topbun.utills.Env

fun Application.configureDatabases() {
    Database.connect(
        url = Env["DATABASE_URL"],
        user = Env["DATABASE_USER"],
        driver = Env["DATABASE_DRIVER"],
        password = Env["DATABASE_PASSWORD"],
    )
    transaction {
        SchemaUtils.create(UserTable, VerificationTable, StepTable, IngredientTable, RecipeTable)
    }
}
