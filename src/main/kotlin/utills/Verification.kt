package ru.topbun.utills

fun generateVerificationCode(): String {
    return (1000..9999).random().toString()
}