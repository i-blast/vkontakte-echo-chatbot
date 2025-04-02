package com.pii.bot.vkontakte_echo_chatbot.service

import com.pii.bot.vkontakte_echo_chatbot.model.vk.event.Confirmation
import com.pii.bot.vkontakte_echo_chatbot.model.vk.event.Message
import com.pii.bot.vkontakte_echo_chatbot.model.vk.event.MessageNew
import com.pii.bot.vkontakte_echo_chatbot.model.vk.event.MessageReply
import com.pii.bot.vkontakte_echo_chatbot.model.vk.event.VkEvent
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class VkEchoService(
    private val vkApiClient: VkApiClient,
    private val messageStatsService: MessageStatsService,
    private val vkMessageService: VkMessageService,
    private val vkUserService: VkUserService,
) {

    private val logger: Logger = LoggerFactory.getLogger(VkEchoService::class.java)

    suspend fun processEvent(event: VkEvent): String {

        logger.debug(">>>>> New event: {}", event.type.toString())

        return when (event) {
            is Confirmation -> vkApiClient.confirm()
            is MessageNew -> handleNewMessage(event.eventObject.message)

            is MessageReply -> {
                logger.debug(">>>>> message reply <<<<<")
                "ok"
            }
        }
    }

    suspend fun handleNewMessage(vkApiMessage: Message): String = coroutineScope {

        val sendMessageDeferred = async {
            vkApiClient.sendMessage(vkApiMessage).also { logger.debug(">>>>> message sent: ${vkApiMessage.text}") }
        }
        val saveUserMessageDeferred = async {
            val savedUser = vkUserService.findOrCreateUser(vkApiMessage.fromId)
            val savedMessage = vkMessageService.saveMessage(vkApiMessage)
            vkUserService.saveUserMessage(savedUser, savedMessage)
        }
        val reIndexStatsDeferred = async {
//            TODO("после реализации elasticsearch хранилища")
        }

        try {
            sendMessageDeferred.await()
            saveUserMessageDeferred.await()
//            reIndexStatsDeferred.await()

            return@coroutineScope "ok"
        } catch (exc: Exception) {
            logger.error(">>>>> failed to process message sendMessage & addMessage concurrently. Reason: ${exc.message}")
            throw exc
        }
    }
}
