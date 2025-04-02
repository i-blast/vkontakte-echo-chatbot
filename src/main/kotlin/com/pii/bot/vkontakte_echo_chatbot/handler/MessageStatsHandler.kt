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
        val word = request.queryParam("word").orElse("")
        if (word.isBlank()) {
            return ServerResponse.badRequest()
                .bodyValueAndAwait(mapOf("error" to "Parameter 'word' is required."))
        }

        val result = request.queryParam("vkUserId").orElse(null)?.toIntOrNull()?.let { vkUserId ->
            messageStatsService.getWordStatsForUser(word, vkUserId.toLong())
        } ?: messageStatsService.getWordStats(word)

        return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValueAndAwait(result)
    }
}
