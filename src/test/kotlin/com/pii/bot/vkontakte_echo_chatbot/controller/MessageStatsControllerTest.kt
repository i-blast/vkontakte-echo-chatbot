package com.pii.bot.vkontakte_echo_chatbot.controller

import com.ninjasquad.springmockk.MockkBean
import com.pii.bot.vkontakte_echo_chatbot.service.MessageStatsService
import io.mockk.coEvery
import kotlinx.coroutines.test.runTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import kotlin.test.Test

@WebFluxTest(MessageStatsController::class)
class MessageStatsControllerTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @MockkBean
    private lateinit var messageStatsService: MessageStatsService

    @Test
    fun `getWordStats should return word count`() = runTest {
        coEvery { messageStatsService.getMessageStats("превед") } returns 5L

        webTestClient.get()
            .uri { it.path("/stats/message").queryParam("message", "превед").build() }
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody(Long::class.java)
            .isEqualTo(5L)
    }

    @Test
    fun `getTopMessages should return top messages`() = runTest {
        coEvery { messageStatsService.getTopMessages(any()) } returns mapOf(
            "превед" to 10L,
            "пока" to 5L
        )

        webTestClient.get()
            .uri { it.path("/stats/top").queryParam("limit", "2").build() }
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.превед").isEqualTo(10)
            .jsonPath("$.пока").isEqualTo(5)
    }

    @Test
    fun `getTopMessages should use default limit`() = runTest {
        coEvery { messageStatsService.getTopMessages(10) } returns emptyMap()
        webTestClient.get()
            .uri("/stats/top")
            .exchange()
            .expectStatus().isOk
    }
}
