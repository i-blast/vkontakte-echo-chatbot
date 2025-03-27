package com.pii.bot.vkontakte_echo_chatbot.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class VkApiConfig(
    @Value("\${vk.api-version}") val apiVersion: String
)
