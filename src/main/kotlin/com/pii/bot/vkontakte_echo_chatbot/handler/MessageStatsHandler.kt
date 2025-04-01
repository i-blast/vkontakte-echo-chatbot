package com.pii.bot.vkontakte_echo_chatbot.handler

import com.pii.bot.vkontakte_echo_chatbot.service.MessageStatsService
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait

@Component
class MessageStatsHandler(
    private val messageStatsService: MessageStatsService,
) {

    suspend fun getMessageStats(request: ServerRequest): ServerResponse {
        val message = request.queryParam("message").orElse("")
        val count = messageStatsService.getMessageStats(message)

        return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValueAndAwait(count)
    }

    suspend fun getTopMessages(request: ServerRequest): ServerResponse {
        val limit = request.queryParam("limit").map { it.toInt() }.orElse(10)
        val topMessages = messageStatsService.getTopMessages(limit)

        return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValueAndAwait(topMessages)
    }
}
