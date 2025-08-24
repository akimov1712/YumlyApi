package ru.topbun.models

sealed class FieldUpdate<out T> {
    object Skip : FieldUpdate<Nothing>()
    data class Set<T>(val value: T?) : FieldUpdate<T>()
}