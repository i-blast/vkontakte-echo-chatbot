package com.pii.bot.vkontakte_echo_chatbot.controller

import com.pii.bot.vkontakte_echo_chatbot.service.WordStatsService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/stats")
class WordStatsController(
    private val wordStatsService: WordStatsService,
) {

    @GetMapping("/word")
    fun getWordStats(@RequestParam word: String): Map<String, Long> = wordStatsService.getMessageStats(word)

    @GetMapping("/top")
    fun getTopMessages(@RequestParam(defaultValue = "10") limit: Int) = wordStatsService.getTopMessages(limit)
}
