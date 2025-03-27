package com.pii.bot.vkontakte_echo_chatbot.model.vk

import com.fasterxml.jackson.annotation.JsonProperty

data class VkApiResponse<T>(
    @JsonProperty("response")
    val response: T?,

    @JsonProperty("error")
    val error: VkApiError?
)

data class VkApiError(
    @JsonProperty("error_code")
    val code: Int,
    @JsonProperty("error_msg")
    val message: String
)
