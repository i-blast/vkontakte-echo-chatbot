package com.pii.bot.vkontakte_echo_chatbot.exception

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    private val logger: Logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(VkApiException::class)
    fun handleVkApiException(exc: VkApiException): ResponseEntity<String> {
        logger.error(">>>>> VK API error: ${exc.message}")
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exc.message)
    }

    @ExceptionHandler(Exception::class)
    fun handleGlobalException(exc: Exception): ResponseEntity<String> {
        logger.error(">>>>> Unexpected error: ", exc)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Unexpected error.")
    }
}
