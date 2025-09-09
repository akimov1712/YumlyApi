package ru.topbun.api.entity

import kotlinx.serialization.Serializable

@Serializable
data class ReasoningOptions(
    val mode: String = "DISABLED"
)
