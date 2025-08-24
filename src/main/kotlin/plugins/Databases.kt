package ru.topbun.plugins

import io.ktor.server.application.Application
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import ru.topbun.utills.Env

fun Application.configureDatabases() {
    Database.connect(
        url = Env["DATABASE_URL"],
        user = Env["DATABASE_DRIVER"],
        driver = Env["DATABASE_USER"],
        password = Env["DATABASE_PASSWORD"],
    )
    transaction {
//        SchemaUtils.create()
    }
}
