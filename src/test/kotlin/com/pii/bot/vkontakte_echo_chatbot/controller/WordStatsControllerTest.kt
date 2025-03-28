package com.pii.bot.vkontakte_echo_chatbot.controller

import com.ninjasquad.springmockk.MockkBean
import com.pii.bot.vkontakte_echo_chatbot.service.WordStatsService
import io.mockk.every
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import kotlin.test.Test

@WebMvcTest(WordStatsController::class)
class WordStatsControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var wordStatsService: WordStatsService

    @Test
    fun `getWordStats should return word count`() {
        every { wordStatsService.getMessageStats("превед") } returns mapOf("превед" to 5L)

        mockMvc.perform(get("/stats/word").param("word", "превед"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("превед").value(5))
    }

    @Test
    fun `getTopMessages should return top messages`() {
        every { wordStatsService.getTopMessages(any()) } returns mapOf(
            "привет" to 10L,
            "пока" to 5L
        )

        mockMvc.perform(get("/stats/top").param("limit", "2"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.привет").value(10))
            .andExpect(jsonPath("$.пока").value(5))
    }

    @Test
    fun `getTopMessages should use default limit`() {
        every { wordStatsService.getTopMessages(10) } returns emptyMap()

        mockMvc.perform(get("/stats/top"))
            .andExpect(status().isOk)
    }
}
