package ru.topbun.utills

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.toJavaLocalDateTime
import java.time.format.DateTimeFormatter

fun LocalDateTime.parseToString(): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy")
    return this.toJavaLocalDateTime().format(formatter)
}