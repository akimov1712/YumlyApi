package ru.topbun.models.gpt.message

enum class GptMessageRoleType {

    SYSTEM, ASSISTANT, USER;

    override fun toString(): String = when(this){
        SYSTEM -> "system"
        ASSISTANT -> "assistant"
        USER -> "user"
    }

    companion object{

        fun fromString(str: String) = when(str){
            "system" -> SYSTEM
            "assistant" -> ASSISTANT
            "user" -> USER
            else -> throw RuntimeException("from $str failed to get a role")
        }

    }

}