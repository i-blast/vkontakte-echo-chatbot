package com.pii.bot.vkontakte_echo_chatbot.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

@Service
class MessageStatsService() {

    private val messagesStats = ConcurrentHashMap<String, AtomicLong>()

    suspend fun addMessage(text: String) {
        withContext(Dispatchers.IO) {
            messagesStats.computeIfAbsent(text) { AtomicLong(0L) }.incrementAndGet()
        }
    }

    suspend fun getTopMessages(limit: Int): Map<String, Long> {
        return messagesStats.entries
            .sortedByDescending { it.value.get() }
            .take(limit)
            .associate { it.key to it.value.get() }
    }

    suspend fun getMessageStats(word: String): Long = messagesStats.getOrDefault(word, AtomicLong(0L)).get()
}
