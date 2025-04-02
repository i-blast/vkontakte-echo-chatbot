package com.pii.bot.vkontakte_echo_chatbot.service

import com.pii.bot.vkontakte_echo_chatbot.model.vk.event.Message
import com.pii.bot.vkontakte_echo_chatbot.repo.message.VkChatMessage
import com.pii.bot.vkontakte_echo_chatbot.repo.message.VkChatMessageRepository
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class VkMessageService(
    private val vkChatMessageRepository: VkChatMessageRepository,
) {

    suspend fun saveMessage(message: Message): VkChatMessage {
        val entity = message.toEntity()
        return vkChatMessageRepository.save(entity)
    }

    fun Message.toEntity(): VkChatMessage {
        return VkChatMessage(
            text = text,
            vkTimestamp = Instant.ofEpochSecond(date.toLong()),
            peerId = peerId.toLong(),
        )
    }
}
