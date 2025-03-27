package com.pii.bot.vkontakte_echo_chatbot.model.vk.event

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes(
    JsonSubTypes.Type(value = Confirmation::class, name = "confirmation"),
    JsonSubTypes.Type(value = MessageNew::class, name = "message_new"),
    JsonSubTypes.Type(value = MessageReply::class, name = "message_reply"),
)
sealed class VkEvent {
    abstract val type: VkEventType
}

enum class VkEventType {
    @JsonProperty("confirmation")
    CONFIRMATION,

    @JsonProperty("message_new")
    MESSAGE_NEW,

    @JsonProperty("message_reply")
    MESSAGE_REPLY,
}
