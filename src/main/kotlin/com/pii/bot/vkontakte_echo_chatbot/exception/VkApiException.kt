package com.pii.bot.vkontakte_echo_chatbot.exception

class VkApiException(val errorCode: Int, message: String) : RuntimeException("VK API Error $errorCode: $message")
