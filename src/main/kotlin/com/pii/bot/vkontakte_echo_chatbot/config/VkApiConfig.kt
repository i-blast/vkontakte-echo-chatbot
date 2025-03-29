package com.pii.bot.vkontakte_echo_chatbot.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class VkApiConfig(
    @Value("\${vk.api-version}") val apiVersion: String
) {

    @Bean
    fun vkWebClient() = WebClient.builder()
        .baseUrl("https://api.vk.com/method")
        .defaultHeader("Content-Type", "application/json")
        .build()
}

enum class VkApiMethod(val endpoint: String) {

    MESSAGES_SEND("/messages.send"),

    USERS_GET("/users.get"),

    GROUPS_GET_BY_ID("/groups.getById")

    ;

    fun buildUri(): String = "$endpoint"
}
