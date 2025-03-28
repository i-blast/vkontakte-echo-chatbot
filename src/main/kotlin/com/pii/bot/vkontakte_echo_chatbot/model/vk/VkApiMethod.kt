package com.pii.bot.vkontakte_echo_chatbot.model.vk

import java.net.URI
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

enum class VkApiMethod(
    val endpoint: String
) {

    MESSAGES_SEND("messages.send"),

    USERS_GET("users.get"),

    GROUPS_GET_BY_ID("groups.getById")

    ;

    companion object {
        private const val BASE_URL = "https://api.vk.com/method/"
    }

    fun buildUrl(queryParams: Map<String, String>): String {
        val queryString = queryParams.entries.joinToString("&") {
            "${it.key}=${it.value}"
        }
        return "$BASE_URL$endpoint?$queryString"
    }
}
