package com.pii.bot.vkontakte_echo_chatbot.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.pii.bot.vkontakte_echo_chatbot.exception.VkApiException
import com.pii.bot.vkontakte_echo_chatbot.model.vk.event.Confirmation
import com.pii.bot.vkontakte_echo_chatbot.model.vk.event.VkEvent
import com.pii.bot.vkontakte_echo_chatbot.service.VkEchoService
import com.pii.bot.vkontakte_echo_chatbot.util.TestDataFactory.createMessageNew
import org.hamcrest.CoreMatchers.containsString
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import kotlin.test.Test

@WebMvcTest(VkCallbackController::class)
class VkCallbackControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var vkEchoService: VkEchoService

    @Test
    fun `should return confirmation code`() {
        val event = Confirmation()
        `when`(vkEchoService.processEvent(event))
            .thenReturn(ResponseEntity.ok("test_code"))

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
        whenever(vkEchoService.processEvent(any<VkEvent>()))
            .thenThrow(VkApiException(100, "Один из необходимых параметров был не передан или неверен."))

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
        `when`(vkEchoService.processEvent(any<VkEvent>()))
            .thenThrow(RuntimeException("Unexpected error"))

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
