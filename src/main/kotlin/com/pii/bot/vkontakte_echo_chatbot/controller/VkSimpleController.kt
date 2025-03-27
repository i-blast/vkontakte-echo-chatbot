package com.pii.bot.vkontakte_echo_chatbot.controller

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

@RestController
@RequestMapping("/vk")
class VkSimpleController(
    @Value("\${vk.confirmation-code}") private val confirmationCode: String,
    @Value("\${vk.access-token}") private val accessToken: String,
    @Value("\${vk.group-id}") private val groupId: String,
) {

    private val logger: Logger = LoggerFactory.getLogger(VkSimpleController::class.java)

    @PostMapping
    fun handleVkCallback(
        @RequestBody request: Map<String, Any>
    ): ResponseEntity<String> {

        val type = request["type"] as? String
        return when (type) {
            "confirmation" -> ResponseEntity.ok(confirmationCode)
            "message_new" -> {
                val message = ((request["object"] as? Map<*, *>)?.get("message") as? Map<*, *>)
                val userId = message?.get("from_id") as? Int
                val text = message?.get("text") as? String ?: ""
                logger.debug("💎💎💎 {}", text)

                if (userId != null) {
                    sendMessage(userId, text)
                }
                ResponseEntity.ok("ok")
            }

            else -> ResponseEntity.ok("ok")
        }
    }

    private fun sendMessage(userId: Int, message: String) {
        val sendMessageUrl = "https://api.vk.com/method/messages.send"
        val sendMessageParams = mapOf(
            "user_id" to userId.toString(),
            "message" to message,
            "random_id" to (System.currentTimeMillis() % Int.MAX_VALUE).toInt().toString(),
            "access_token" to accessToken,
            "v" to "5.199"
        )
        val restTemplate = RestTemplate()
        restTemplate.postForEntity(sendMessageUrl, sendMessageParams, String::class.java)
    }
}
