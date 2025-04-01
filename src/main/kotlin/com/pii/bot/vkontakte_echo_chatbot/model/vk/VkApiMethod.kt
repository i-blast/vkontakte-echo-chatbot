package com.pii.bot.vkontakte_echo_chatbot.model.vk

enum class VkApiMethod(val endpoint: String) {

    MESSAGES_SEND("/messages.send"),

    USERS_GET("/users.get"),

    GROUPS_GET_BY_ID("/groups.getById")

    ;

    fun buildUri(): String = "$endpoint"
}
