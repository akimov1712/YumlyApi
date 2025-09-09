package ru.topbun.models.gpt

enum class GptMessageRoleType {

    SYSTEM, ASSISTANT, USER;

    override fun toString(): String = when(this){
        SYSTEM -> "system"
        ASSISTANT -> "assistant"
        USER -> "user"
    }
}