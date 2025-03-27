package com.pii.bot.vkontakte_echo_chatbot.model.vk.event

import com.fasterxml.jackson.annotation.JsonProperty
import com.pii.bot.vkontakte_echo_chatbot.model.vk.event.VkEvent
import com.pii.bot.vkontakte_echo_chatbot.model.vk.event.VkEventType

data class Confirmation(
    @JsonProperty("type")
    override val type: VkEventType = VkEventType.CONFIRMATION,
) : VkEvent()
