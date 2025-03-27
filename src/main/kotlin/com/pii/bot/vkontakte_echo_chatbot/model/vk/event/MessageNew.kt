package com.pii.bot.vkontakte_echo_chatbot.model.vk.event

import com.fasterxml.jackson.annotation.JsonProperty

data class MessageNew(
    @JsonProperty("type")
    override val type: VkEventType = VkEventType.MESSAGE_NEW,
    @JsonProperty("group_id")
    val groupId: Int,
    @JsonProperty("event_id")
    val eventId: String,
    @JsonProperty("v")
    val version: String,
    @JsonProperty("object")
    val eventObject: MessageNewEventObject,
) : VkEvent()

data class MessageNewEventObject(
    @JsonProperty("client_info")
    val clientInfo: ClientInfo,
    @JsonProperty("message")
    val message: Message,
)

data class ClientInfo(
    @JsonProperty("button_actions")
    val buttonActions: List<String>,
    @JsonProperty("keyboard")
    val keyboard: Boolean,
    @JsonProperty("inline_keyboard")
    val inlineKeyboard: Boolean,
    @JsonProperty("carousel")
    val carousel: Boolean,
    @JsonProperty("lang_id")
    val langId: Int,
)
