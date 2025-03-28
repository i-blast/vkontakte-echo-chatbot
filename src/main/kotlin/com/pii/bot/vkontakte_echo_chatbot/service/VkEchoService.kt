package com.pii.bot.vkontakte_echo_chatbot.service

import com.pii.bot.vkontakte_echo_chatbot.exception.VkApiException
import com.pii.bot.vkontakte_echo_chatbot.model.vk.VkApiMethod
import com.pii.bot.vkontakte_echo_chatbot.model.vk.VkApiResponse
import com.pii.bot.vkontakte_echo_chatbot.model.vk.event.Confirmation
import com.pii.bot.vkontakte_echo_chatbot.model.vk.event.Message
import com.pii.bot.vkontakte_echo_chatbot.model.vk.event.MessageNew
import com.pii.bot.vkontakte_echo_chatbot.model.vk.event.MessageReply
import com.pii.bot.vkontakte_echo_chatbot.model.vk.event.VkEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.net.URLDecoder
import java.net.URLEncoder
import kotlin.random.Random

@Service
class VkEchoService(
    @Value("\${vk.access-token}")
    private val accessToken: String,
    @Value("\${vk.confirmation-code}")
    private val confirmationCode: String,
    @Value("\${vk.api-version}")
    private val apiVersion: String,

    private val restTemplate: RestTemplate,
) {

    private val logger: Logger = LoggerFactory.getLogger(VkEchoService::class.java)

    fun processEvent(event: VkEvent): ResponseEntity<String> {

        logger.debug(">>>>> New event: {}", event.type.toString())

        return when (event) {
            is Confirmation -> ResponseEntity.ok(confirmationCode)
            is MessageNew -> {
                echoMessage(event.eventObject.message)
                ResponseEntity.ok("ok")
            }

            is MessageReply -> {
                logger.debug(">>>>> message reply <<<<<")
                ResponseEntity.ok("ok")
            }
        }
    }

    private fun echoMessage(message: Message) {

        val sendMessageUrl = VkApiMethod.MESSAGES_SEND.buildUrl(
            mapOf(
                "peer_id" to message.peerId.toString(),
                "message" to message.text,
                "random_id" to Random.nextInt().toString(),
                "access_token" to accessToken,
                "v" to apiVersion
            )
        )

        val responseEntity = restTemplate.exchange(
            sendMessageUrl,
            HttpMethod.POST,
            HttpEntity.EMPTY,
            VkApiResponse::class.java
        )
        val response = responseEntity.body

        response?.error?.let {
            logger.error(">>>>> VK API error: ${it.code} - ${it.message}")
            throw VkApiException(it.code, it.message)
        }
    }
}
