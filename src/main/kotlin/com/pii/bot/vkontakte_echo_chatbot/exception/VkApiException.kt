package com.pii.bot.vkontakte_echo_chatbot.exception

class VkApiException(val errorCode: Int = 0, errorMessage: String) : RuntimeException("VK API Error $errorCode: $errorMessage")
