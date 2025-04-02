package com.pii.bot.vkontakte_echo_chatbot.handler

import com.ninjasquad.springmockk.MockkBean
import com.pii.bot.vkontakte_echo_chatbot.router.MessageStatsRouter
import com.pii.bot.vkontakte_echo_chatbot.service.MessageStatsService
import io.mockk.coEvery
import org.junit.jupiter.api.Disabled
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import kotlin.test.Test


@Disabled
@WebFluxTest
@Import(MessageStatsRouter::class, MessageStatsHandler::class)
class MessageStatsHandlerTest(
    @Autowired private val webTestClient: WebTestClient,
) {

    @MockkBean
    private lateinit var messageStatsService: MessageStatsService

    @Test
    fun `getWordStats should return message count`() {
        coEvery { messageStatsService.getWordStats("превед") } returns 5L

        webTestClient.get()
            .uri("/api/stats/message?message=превед")
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody(Long::class.java)
            .isEqualTo(5L)
    }

/*    @Test
    fun `getTopMessages should return top messages`() {
        coEvery { messageStatsService.getTopMessages(any()) } returns mapOf(
            "превед" to 10L,
            "пока" to 5L
        )

        webTestClient.get()
            .uri { it.path("/api/stats/top").queryParam("limit", "2").build() }
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.превед").isEqualTo(10)
            .jsonPath("$.пока").isEqualTo(5)
    }

    @Test
    fun `getTopMessages should use default limit`() {
        coEvery { messageStatsService.getTopMessages(10) } returns emptyMap()
        webTestClient.get()
            .uri("/api/stats/top")
            .exchange()
            .expectStatus().isOk
    }*/
}
