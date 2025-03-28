package com.pii.bot.vkontakte_echo_chatbot.service

import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

@Service
class WordStatsService() {

    private val messageStats = ConcurrentHashMap<String, AtomicLong>()

    fun addMessage(text: String) = messageStats.computeIfAbsent(text) { AtomicLong(0L) }.incrementAndGet()

    fun getTopMessages(limit: Int): Map<String, Long> {
        return messageStats.entries
            .sortedByDescending { it.value.get() }
            .take(limit)
            .associate { it.key to it.value.get() }
    }

    fun getMessageStats(word: String): Map<String, Long> {
        return mapOf(word to messageStats.getOrDefault(word, AtomicLong(0L)).get())
    }
}
