package com.pii.bot.vkontakte_echo_chatbot.controller

import com.pii.bot.vkontakte_echo_chatbot.service.MessageStatsService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/stats")
class MessageStatsController(
    private val messageStatsService: MessageStatsService,
) {

    @GetMapping("/message")
    suspend fun getMessageStats(
        @RequestParam message: String
    ): ResponseEntity<Long> {
        return ResponseEntity.ok(messageStatsService.getMessageStats(message))
    }

    @GetMapping("/top")
    suspend fun getTopMessages(
        @RequestParam(defaultValue = "10") limit: Int
    ): ResponseEntity<Map<String, Long>> {
        return ResponseEntity.ok(messageStatsService.getTopMessages(limit))
    }
}
