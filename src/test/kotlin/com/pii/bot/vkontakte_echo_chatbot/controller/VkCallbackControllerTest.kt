package com.pii.bot.vkontakte_echo_chatbot.controller

import com.ninjasquad.springmockk.MockkBean
import com.pii.bot.vkontakte_echo_chatbot.exception.VkApiException
import com.pii.bot.vkontakte_echo_chatbot.model.vk.event.Confirmation
import com.pii.bot.vkontakte_echo_chatbot.model.vk.event.VkEvent
import com.pii.bot.vkontakte_echo_chatbot.service.VkEchoService
import com.pii.bot.vkontakte_echo_chatbot.util.TestDataFactory.createMessageNew
import com.pii.bot.vkontakte_echo_chatbot.util.TestDataFactory.objectMapper
import io.mockk.coEvery
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import kotlin.test.Test

@WebFluxTest(VkCallbackController::class)
class VkCallbackControllerTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @MockkBean
    private lateinit var vkEchoService: VkEchoService

    @Test
    fun `should return confirmation code`() = runTest {
        val event = Confirmation()
        coEvery { vkEchoService.processEvent(event) } returns "превед"

        webTestClient.post()
            .uri("/vk/echo")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(event.toJson())
            .exchange()
            .expectStatus().isOk
            .expectBody(String::class.java)
            .isEqualTo("превед")
    }

    @Test
    fun `should handle VkApiException`() = runTest {
        val event = createMessageNew()
        coEvery { vkEchoService.processEvent(any()) } throws VkApiException(
            100,
            "Один из необходимых параметров был не передан или неверен."
        )

        webTestClient.post()
            .uri("/vk/echo")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(event.toJson())
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(String::class.java)
            .consumeWith { result ->
                assertThat(result.responseBody).contains("Один из необходимых параметров был не передан или неверен.")
            }
    }

    @Test
    fun `should handle unexpected exceptions`() = runTest {
        coEvery { vkEchoService.processEvent(any()) } throws RuntimeException("Unexpected error.")

        webTestClient.post()
            .uri("/vk/echo")
            .contentType(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().is5xxServerError
            .expectBody(String::class.java)
            .isEqualTo("Unexpected error.")
    }

    fun VkEvent.toJson(): String = objectMapper.writeValueAsString(this)
}
