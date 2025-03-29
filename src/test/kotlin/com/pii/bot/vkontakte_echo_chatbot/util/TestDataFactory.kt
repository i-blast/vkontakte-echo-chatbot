package com.pii.bot.vkontakte_echo_chatbot.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.pii.bot.vkontakte_echo_chatbot.model.vk.event.ClientInfo
import com.pii.bot.vkontakte_echo_chatbot.model.vk.event.Message
import com.pii.bot.vkontakte_echo_chatbot.model.vk.event.MessageNew
import com.pii.bot.vkontakte_echo_chatbot.model.vk.event.MessageNewEventObject
import com.pii.bot.vkontakte_echo_chatbot.model.vk.event.MessageReply
import com.pii.bot.vkontakte_echo_chatbot.model.vk.event.MessageReplyObject
import com.pii.bot.vkontakte_echo_chatbot.model.vk.event.VkEventType

object TestDataFactory {

    fun createTestMessage(
        text: String = "hi тест",
        peerId: Int = 12345,
        fromId: Int = 12345,
    ): Message = Message(
        date = 0,
        fromId = fromId,
        id = 1,
        messageVersion = 1,
        out = 0,
        fwdMessages = emptyList(),
        important = false,
        isHidden = false,
        attachments = emptyList(),
        conversationMessageId = 0,
        text = text,
        peerId = peerId,
        randomId = 0
    )

    fun createMessageNew(
        message: Message = createTestMessage(),
        eventId: String = "test_event_id",
        groupId: Int = 1
    ): MessageNew = MessageNew(
        type = VkEventType.MESSAGE_NEW,
        groupId = groupId,
        eventId = eventId,
        version = "5.199",
        eventObject = MessageNewEventObject(
            clientInfo = ClientInfo(
                buttonActions = emptyList(),
                keyboard = false,
                inlineKeyboard = false,
                carousel = false,
                langId = 0
            ),
            message = message
        )
    )

    fun createMessageReply(
        text: String = "Reply message",
        peerId: Int = 12345,
        eventId: String = "reply_event_id",
        groupId: Int = 1
    ): MessageReply = MessageReply(
        type = VkEventType.MESSAGE_REPLY,
        groupId = groupId,
        eventId = eventId,
        version = "5.199",
        eventObject = MessageReplyObject(
            message = createTestMessage(text = text, peerId = peerId)
        )
    )

    val objectMapper: ObjectMapper = ObjectMapper().apply {
        registerModule(KotlinModule.Builder().build())
    }
}
