package com.pii.bot.vkontakte_echo_chatbot.model.vk.event

import com.fasterxml.jackson.annotation.JsonProperty

data class Message(
    @JsonProperty("date")
    val date: Int,
    @JsonProperty("from_id")
    val fromId: Int,
    @JsonProperty("id")
    val id: Int,
    @JsonProperty("version")
    val messageVersion: Int,
    @JsonProperty("out")
    val out: Int,
    @JsonProperty("fwd_messages")
    val fwdMessages: List<Message>,
    @JsonProperty("important")
    val important: Boolean,
    @JsonProperty("is_hidden")
    val isHidden: Boolean,
    @JsonProperty("attachments")
    val attachments: List<Any>,
    @JsonProperty("conversation_message_id")
    val conversationMessageId: Int,
    @JsonProperty("text")
    val text: String,
    @JsonProperty("peer_id")
    val peerId: Int,
    @JsonProperty("random_id")
    val randomId: Int,
)
