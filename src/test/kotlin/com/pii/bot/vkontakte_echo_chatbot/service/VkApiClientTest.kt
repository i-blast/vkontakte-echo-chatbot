package com.pii.bot.vkontakte_echo_chatbot.service

import com.pii.bot.vkontakte_echo_chatbot.exception.VkApiException
import com.pii.bot.vkontakte_echo_chatbot.model.vk.VkApiError
import com.pii.bot.vkontakte_echo_chatbot.model.vk.VkApiResponse
import com.pii.bot.vkontakte_echo_chatbot.util.TestDataFactory.createTestMessage
import com.pii.bot.vkontakte_echo_chatbot.util.TestDataFactory.objectMapper
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.springframework.web.reactive.function.client.WebClient
import kotlin.test.Test
import kotlin.test.assertFailsWith

class VkApiClientTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var vkApiClient: VkApiClient
    private lateinit var webClient: WebClient

    @BeforeEach
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        webClient = WebClient.builder()
            .baseUrl(mockWebServer.url("/method").toString())
            .defaultHeader("Content-Type", "application/json")
            .build()

        vkApiClient = VkApiClient(
            confirmationCode = "test-code",
            accessToken = "test-token",
            apiVersion = "5.199",
            webClient = webClient
        )
    }

    @Test
    fun `should return confirmation code`() = runTest {
        assertThat(vkApiClient.confirm()).isEqualTo("test-code")
    }

    @Test
    fun `should send message successfully`() = runTest {
        val message = createTestMessage()
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setHeader("Content-Type", "application/json")
            .setBody(
                VkApiResponse(
                    error = null,
                    response = 1
                ).toJson()
            )
        mockWebServer.enqueue(mockResponse)

        vkApiClient.sendMessage(message)

        val request = mockWebServer.takeRequest()
        assertThat(request.path).contains("messages.send")
        assertThat(request.path).contains("peer_id=123")
        assertThat(request.path).contains("random_id=")
    }

    @Test
    fun `should throw VkApiException on error response`() = runTest {
        val message = createTestMessage()
        val errorResponse = VkApiResponse(
            response = null,
            error = VkApiError(code = 100, message = "Один из необходимых параметров был не передан или неверен.")
        )
        val mockResponse = MockResponse()
            .setResponseCode(400)
            .setHeader("Content-Type", "application/json")
            .setBody(errorResponse.toJson())
        mockWebServer.enqueue(mockResponse)

        assertFailsWith<VkApiException> {
            vkApiClient.sendMessage(message)
        }
    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }

    fun <T> VkApiResponse<T>.toJson(): String = objectMapper.writeValueAsString(this)
}
