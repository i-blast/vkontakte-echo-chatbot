package com.pii.bot.vkontakte_echo_chatbot.service

import com.pii.bot.vkontakte_echo_chatbot.exception.VkApiException
import com.pii.bot.vkontakte_echo_chatbot.model.vk.event.Confirmation
import com.pii.bot.vkontakte_echo_chatbot.repo.message.VkChatMessageRepository
import com.pii.bot.vkontakte_echo_chatbot.util.TestDataFactory.createMessageNew
import com.pii.bot.vkontakte_echo_chatbot.util.TestDataFactory.createMessageReply
import com.pii.bot.vkontakte_echo_chatbot.util.TestDataFactory.createTestMessage
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@Disabled
@ExtendWith(MockKExtension::class)
class VkEchoServiceTest {

    @MockK
    private lateinit var vkApiClient: VkApiClient

    @MockK
    private lateinit var messageStatsService: MessageStatsService

    private lateinit var vkEchoService: VkEchoService

    @MockK
    private lateinit var vkChatMessageRepository: VkChatMessageRepository

    @MockK
    private lateinit var vkMessageService: VkMessageService

    @MockK
    private lateinit var vkUserService: VkUserService

    @BeforeEach
    fun setUp() {
        vkEchoService = VkEchoService(
            vkApiClient,
            messageStatsService,
            vkMessageService,
            vkUserService
        )
    }

    @Test
    fun `should return confirmation code on Confirmation event`() = runTest {
        coEvery { vkApiClient.confirm() } returns "confirmation_code"
        val result = vkEchoService.processEvent(Confirmation())
        assertEquals("confirmation_code", result)
        coVerify(exactly = 1) { vkApiClient.confirm() }
    }

    /*@Test
    fun `should handle new message correctly`() = runTest {
        val message = createTestMessage(text = "Превед VK!")
        val messageNewEvent = createMessageNew(message)
        coEvery { vkApiClient.sendMessage(message) } just Runs
        coEvery { messageStatsService.addMessage(message.text) } just Runs

        val result = vkEchoService.processEvent(messageNewEvent)

        assertEquals("ok", result)
        coVerify { vkApiClient.sendMessage(message) }
        coVerify { messageStatsService.addMessage(message.text) }
    }

    @Test
    fun `should return 'ok' on MessageReply event`() = runTest {
        val result = vkEchoService.processEvent(createMessageReply())
        assertEquals("ok", result)
    }

    @Test
    fun `handleNewMessage should call sendMessage and addMessage concurrently`() = runTest {
        val message = createTestMessage()
        coEvery { vkApiClient.sendMessage(message) } just Runs
        coEvery { messageStatsService.addMessage(message.text) } just Runs

        vkEchoService.processEvent(createMessageNew())

        coVerify(exactly = 1) { vkApiClient.sendMessage(message) }
        coVerify(exactly = 1) { messageStatsService.addMessage(message.text) }
    }

    @Test
    fun `processEvent with MessageNew should handle sendMessage error`() = runTest {
        val message = createTestMessage()
        val messageNewEvent = createMessageNew()
        coEvery { vkApiClient.sendMessage(message) } throws VkApiException(
            100,
            "Один из необходимых параметров был не передан или неверен."
        )
        coEvery { messageStatsService.addMessage(message.text) } just Runs

        assertFailsWith<RuntimeException> {
            vkEchoService.processEvent(messageNewEvent)
        }
    }*/
}
