package com.pii.bot.vkontakte_echo_chatbot.service

import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.Test

@ExtendWith(MockKExtension::class)
class MessageStatsServiceTest {

    @InjectMockKs
    private lateinit var messageStatsService: MessageStatsService

    @Test
    fun `addMessage should increment message count`() = runTest {
        (0..1).forEach { messageStatsService.addMessage("превед") }
        assertThat(messageStatsService.getMessageStats("превед")).isEqualTo(2L)
    }

    @Test
    fun `getMessageStats should return zero for unknown word`() = runTest {
        val stats = messageStatsService.getMessageStats("несуществующее_слово")
        assertThat(stats).isEqualTo(0L)
    }

    @Test
    fun `getTopMessages should return correct order and limit`() = runTest {
        listOf("один", "два", "два", "три", "три", "три").forEach {
            messageStatsService.addMessage(it)
        }
        val top = messageStatsService.getTopMessages(2)
        assertThat(top.size).isEqualTo(2)
        assertThat(top["три"]).isEqualTo(3L)
    }
}
