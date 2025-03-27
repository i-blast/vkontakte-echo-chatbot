package com.pii.bot.vkontakte_echo_chatbot.controller

import com.pii.bot.vkontakte_echo_chatbot.model.vk.event.VkEvent
import com.pii.bot.vkontakte_echo_chatbot.service.VkEchoService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/vk/echo")
class VkCallbackController(
    private val vkEchoService: VkEchoService,
) {

    @PostMapping
    fun handle(
        @RequestBody event: VkEvent
    ): String {
        return vkEchoService.processEvent(event)
    }
}
