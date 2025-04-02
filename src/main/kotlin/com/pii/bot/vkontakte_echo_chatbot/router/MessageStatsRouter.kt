package com.pii.bot.vkontakte_echo_chatbot.router

import com.pii.bot.vkontakte_echo_chatbot.handler.MessageStatsHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class MessageStatsRouter(
    private val messageStatsHandler: MessageStatsHandler
) {

    @Bean
    fun statsRoutes() = coRouter {
        "/api".nest {
            "/stats".nest {
                GET("/message", messageStatsHandler::getMessageStats)
//                GET("/top", messageStatsHandler::getTopMessages)
            }
        }
    }
}
