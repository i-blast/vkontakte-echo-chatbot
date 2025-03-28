package com.pii.bot.vkontakte_echo_chatbot.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.ninjasquad.springmockk.MockkBean
import com.pii.bot.vkontakte_echo_chatbot.exception.VkApiException
import com.pii.bot.vkontakte_echo_chatbot.model.vk.event.Confirmation
import com.pii.bot.vkontakte_echo_chatbot.model.vk.event.VkEvent
import com.pii.bot.vkontakte_echo_chatbot.service.VkEchoService
import com.pii.bot.vkontakte_echo_chatbot.util.TestDataFactory.createMessageNew
import io.mockk.every
import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(VkCallbackController::class)
class VkCallbackControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var vkEchoService: VkEchoService

    @Test
    fun `should return confirmation code`() {
        val event = Confirmation()
        every { vkEchoService.processEvent(event) } returns ResponseEntity.ok("test_code")

        mockMvc.perform(
            post("/vk/echo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(event.toJson())
        )
            .andExpect(status().isOk)
            .andExpect(content().string("test_code"))
    }

    @Test
    fun `should handle VkApiException`() {
        val event = createMessageNew()
        every { vkEchoService.processEvent(any()) } throws VkApiException(
            100,
            "Один из необходимых параметров был не передан или неверен."
        )

        mockMvc.perform(
            post("/vk/echo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(event.toJson())
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().string(containsString("Один из необходимых параметров был не передан или неверен.")))
    }

    @Test
    fun `should handle unexpected exceptions`() {
        every { vkEchoService.processEvent(any()) } throws RuntimeException("Unexpected error")

        mockMvc.perform(
            post("/vk/echo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createMessageNew().toJson())
        )
            .andExpect(status().isInternalServerError)
            .andExpect(content().string("Unexpected error."))
    }

    fun VkEvent.toJson(): String =
        objectMapper.writeValueAsString(this)

    private val objectMapper = ObjectMapper().apply {
        registerModule(KotlinModule.Builder().build())
//        registerModule(JavaTimeModule())
    }
}
