package com.pii.bot.vkontakte_echo_chatbot.exception

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebExceptionHandler
import reactor.core.publisher.Mono

@Component
@Order(-2)
class GlobalExceptionHandler : WebExceptionHandler {

    private val logger: Logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)


    override fun handle(exchange: ServerWebExchange, ex: Throwable): Mono<Void> {

        logger.error(">>>>> Exception caught: ${ex.message}", ex)

        val response = exchange.response
        val status = when (ex) {
            is VkApiException -> HttpStatus.BAD_REQUEST
            else -> HttpStatus.INTERNAL_SERVER_ERROR
        }
        response.statusCode = status
        response.headers.contentType = MediaType.TEXT_PLAIN

        val errorMessage = ex.message ?: "Unexpected error"

        return response.writeWith(
            Mono.just(response.bufferFactory().wrap(errorMessage.toByteArray()))
        )
    }
}
