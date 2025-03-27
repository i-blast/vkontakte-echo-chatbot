package com.pii.bot.vkontakte_echo_chatbot.model.vk.event

import com.fasterxml.jackson.annotation.JsonProperty

data class MessageReply(
    @JsonProperty("type")
    override val type: VkEventType = VkEventType.MESSAGE_REPLY,
    @JsonProperty("event_id")
    val eventId: String,
    @JsonProperty("group_id")
    val groupId: Int,
    @JsonProperty("v")
    val version: String,
    @JsonProperty("object")
    val eventObject: MessageReplyObject,
) : VkEvent()

data class MessageReplyObject(
    @JsonProperty("message") val message: Message?,
)
