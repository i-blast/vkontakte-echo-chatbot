package com.pii.bot.vkontakte_echo_chatbot.service

import com.ninjasquad.springmockk.MockkBean
import com.pii.bot.vkontakte_echo_chatbot.exception.VkApiException
import com.pii.bot.vkontakte_echo_chatbot.model.vk.VkApiError
import com.pii.bot.vkontakte_echo_chatbot.model.vk.VkApiResponse
import com.pii.bot.vkontakte_echo_chatbot.model.vk.event.Confirmation
import com.pii.bot.vkontakte_echo_chatbot.model.vk.event.MessageReply
import com.pii.bot.vkontakte_echo_chatbot.model.vk.event.MessageReplyObject
import com.pii.bot.vkontakte_echo_chatbot.model.vk.event.VkEventType
import com.pii.bot.vkontakte_echo_chatbot.util.TestDataFactory.createMessageNew
import io.mockk.every
import io.mockk.slot
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.web.client.RestTemplate

@SpringBootTest
class VkEchoServiceTest {

    @MockkBean
    private lateinit var restTemplate: RestTemplate

    @Autowired
    private lateinit var vkEchoService: VkEchoService

    companion object {

        @DynamicPropertySource
        @JvmStatic
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("vk.access-token") { "test_access_token" }
            registry.add("vk.confirmation-code") { "test_confirmation_code" }
            registry.add("vk.api-version") { "5.199" }
        }
    }

    @Test
    fun `should return confirmation code on Confirmation event`() {
        val result = vkEchoService.processEvent(Confirmation())
        assertEquals(ResponseEntity.ok("test_confirmation_code"), result)
    }

    @Test
    fun `should throw VkApiException when VK API returns an error`() {
        val event = createMessageNew()
        val errorResponse = VkApiResponse(
            null,
            VkApiError(100, "Один из необходимых параметров был не передан или неверен.")
        )

        every {
            restTemplate.exchange(
                any<String>(),
                any(),
                any(),
                VkApiResponse::class.java
            )
        } returns ResponseEntity.ok(errorResponse)

        assertThrows<VkApiException> { vkEchoService.processEvent(event) }
    }

    @Test
    fun `should return ok on MessageNew event`() {
        val event = createMessageNew()

        every {
            restTemplate.exchange(
                any<String>(),
                any(),
                any(),
                VkApiResponse::class.java
            )
        } returns ResponseEntity.ok(VkApiResponse(null, null))

        val result = vkEchoService.processEvent(event)
        assertEquals(ResponseEntity.ok("ok"), result)
    }

    @Test
    fun `should return ok on MessageReply event`() {
        val event = MessageReply(
            type = VkEventType.MESSAGE_REPLY,
            eventId = "eventId",
            groupId = 222222,
            version = "5.199",
            eventObject = MessageReplyObject(message = null)
        )
        val result = vkEchoService.processEvent(event)
        assertEquals(ResponseEntity.ok("ok"), result)
    }

    @Test
    fun `should correctly construct URL when sending message`() {
        val event = createMessageNew()
        val urlSlot = slot<String>()

        every {
            restTemplate.exchange(
                capture(urlSlot),
                any<HttpMethod>(),
                any(),
                VkApiResponse::class.java
            )
        } returns ResponseEntity.ok(VkApiResponse(null, null))

        vkEchoService.processEvent(event)

        val url = urlSlot.captured
        assertThat(url).contains(
            "peer_id=12345",
            "message=hi",
            "access_token=test_access_token",
            "v=5.199"
        )
    }
}
