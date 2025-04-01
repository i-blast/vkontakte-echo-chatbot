package com.pii.bot.vkontakte_echo_chatbot.handler

import com.pii.bot.vkontakte_echo_chatbot.model.vk.event.VkEvent
import com.pii.bot.vkontakte_echo_chatbot.service.VkEchoService
import kotlinx.coroutines.reactive.awaitSingle
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait

@Component
class VkCallbackHandler(
    private val vkEchoService: VkEchoService,
) {

    private val logger: Logger = LoggerFactory.getLogger(VkCallbackHandler::class.java)

    suspend fun handle(request: ServerRequest): ServerResponse {
        val event = request.bodyToMono(VkEvent::class.java).awaitSingle()
        val result = vkEchoService.processEvent(event)

        return ServerResponse.ok()
            .contentType(MediaType.TEXT_PLAIN)
            .bodyValueAndAwait(result)
    }
}
