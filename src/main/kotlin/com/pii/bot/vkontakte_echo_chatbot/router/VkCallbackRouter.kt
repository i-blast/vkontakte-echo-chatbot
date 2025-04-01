package com.pii.bot.vkontakte_echo_chatbot.router

import com.pii.bot.vkontakte_echo_chatbot.handler.VkCallbackHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class VkCallbackRouter(
    private val vkCallbackHandler: VkCallbackHandler
) {

    @Bean
    fun vkApiRoutes() = coRouter {
        "/api".nest {
            accept(APPLICATION_JSON).nest {
                contentType(APPLICATION_JSON).nest {
                    POST("/vk/echo", vkCallbackHandler::handle)
                }
            }
        }
    }
}
