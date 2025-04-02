package com.pii.bot.vkontakte_echo_chatbot.service

import com.pii.bot.vkontakte_echo_chatbot.repo.message.VkChatMessageRepository
import com.pii.bot.vkontakte_echo_chatbot.repo.user.VkUserChatMessageRepository
import org.springframework.stereotype.Service

@Service
class MessageStatsService(
    private val vkChatMessageRepository: VkChatMessageRepository,
    private val vkUserChatMessageRepository: VkUserChatMessageRepository,
) {

    suspend fun getWordStats(word: String): Long = vkChatMessageRepository.countByExactWord(word)

    suspend fun getWordStatsForUser(word: String, vkUserId: Long): Long =
        vkUserChatMessageRepository.countByUserVkIdAndExactWord(vkUserId, word)
}
