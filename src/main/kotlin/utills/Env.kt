package ru.topbun.utills

import io.github.cdimascio.dotenv.dotenv

object Env {

    private val env = dotenv()

    operator fun get(key: String?): String = env.get(key) ?: throw RuntimeException("Value from env not found with key: $key")

}