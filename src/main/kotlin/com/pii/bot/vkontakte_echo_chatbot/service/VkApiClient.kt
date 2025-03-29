package com.pii.bot.vkontakte_echo_chatbot.service

import com.pii.bot.vkontakte_echo_chatbot.config.VkApiMethod
import com.pii.bot.vkontakte_echo_chatbot.exception.VkApiException
import com.pii.bot.vkontakte_echo_chatbot.model.vk.VkApiResponse
import com.pii.bot.vkontakte_echo_chatbot.model.vk.event.Message
import kotlinx.coroutines.reactive.awaitFirstOrDefault
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import kotlin.random.Random

@Service
class VkApiClient(
    @Value("\${vk.confirmation-code}")
    private val confirmationCode: String,
    @Value("\${vk.access-token}")
    private val accessToken: String,
    @Value("\${vk.api-version}")
    private val apiVersion: String,

    private val webClient: WebClient,
) {

    private val logger: Logger = LoggerFactory.getLogger(VkApiClient::class.java)

    suspend fun confirm() = confirmationCode

    suspend fun sendMessage(message: Message) {

        logger.debug(">>>>> Sending message: {}", message.text)

        webClient.post()
            .uri { builder ->
                val uri = builder.path(VkApiMethod.MESSAGES_SEND.buildUri())
                    .queryParams(createQueryParams(message))
                    .build()
                logger.debug(">>>>> uri: {}", uri)
                uri
            }
            .retrieve()
            .onStatus({ !it.is2xxSuccessful }) { clientResponse ->
                clientResponse.bodyToMono(String::class.java)
                    .flatMap { errorBody ->
                        Mono.error(VkApiException(errorMessage = "HTTP error: $errorBody"))
                    }
            }
            .bodyToMono(VkApiResponse::class.java)
            .flatMap { apiResponse ->
                if (apiResponse.error != null) {
                    Mono.error(VkApiException(apiResponse.error.code, apiResponse.error.message))
                } else {
                    Mono.empty<Void>()
                }
            }
            .awaitFirstOrDefault(null).let {} ?: throw IllegalStateException()
    }

    private fun createQueryParams(message: Message): MultiValueMap<String, String> {
        return LinkedMultiValueMap<String, String>().apply {
            add("peer_id", message.peerId.toString())
            add("message", "Вы сказали: " + message.text)
            add("random_id", Random.nextInt().toString())
            add("access_token", accessToken)
            add("v", apiVersion)
        }
    }
}
