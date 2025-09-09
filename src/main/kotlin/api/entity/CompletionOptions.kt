package ru.topbun.api.entity

import kotlinx.serialization.Serializable

@Serializable
data class CompletionOptions(
    val stream: Boolean = false,
    val temperature: Double = 0.3,
    val maxTokens: Int = 2000,
    val reasoningOptions: ReasoningOptions = ReasoningOptions(),
)