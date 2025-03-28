package com.pii.bot.vkontakte_echo_chatbot.service

import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.Test

@ExtendWith(MockKExtension::class)
class WordStatsServiceTest {

    @InjectMockKs
    private lateinit var wordStatsService: WordStatsService

    @Test
    fun `addMessage should increment message count`() {
        (0..1).forEach { wordStatsService.addMessage("превед") }
        val stats = wordStatsService.getMessageStats("превед")
        assertThat(stats["превед"]).isEqualTo(2L)
    }

    @Test
    fun `getMessageStats should return zero for unknown word`() {
        val stats = wordStatsService.getMessageStats("несуществующее_слово")
        assertThat(stats["несуществующее_слово"]).isEqualTo(0L)
    }

    @Test
    fun `getTopMessages should return correct order and limit`() {
        listOf("один", "два", "два", "три", "три", "три").forEach {
            wordStatsService.addMessage(it)
        }
        val top = wordStatsService.getTopMessages(2)
        assertThat(top.size).isEqualTo(2)
        assertThat(top["три"]).isEqualTo(3L)
    }
}
