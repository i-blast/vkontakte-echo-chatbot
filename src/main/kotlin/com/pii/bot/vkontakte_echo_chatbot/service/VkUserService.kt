package com.pii.bot.vkontakte_echo_chatbot.service

import com.pii.bot.vkontakte_echo_chatbot.model.vk.event.ClientInfo
import com.pii.bot.vkontakte_echo_chatbot.repo.message.VkChatMessage
import com.pii.bot.vkontakte_echo_chatbot.repo.message.VkChatMessageRepository
import com.pii.bot.vkontakte_echo_chatbot.repo.user.VkUser
import com.pii.bot.vkontakte_echo_chatbot.repo.user.VkUserChatMessage
import com.pii.bot.vkontakte_echo_chatbot.repo.user.VkUserChatMessageRepository
import com.pii.bot.vkontakte_echo_chatbot.repo.user.VkUserRepository
import org.springframework.stereotype.Service

@Service
class VkUserService(
    private val vkUserRepository: VkUserRepository,
    private val vkUserChatMessageRepository: VkUserChatMessageRepository,
    private val vkChatMessageRepository: VkChatMessageRepository,
) {

    suspend fun findOrCreateUser(vkUserId: Int): VkUser {
        return vkUserRepository.findByVkId(vkUserId) ?: run {
//            val userInfo = vkApiClient.getUserInfo(vkUserId)
            vkUserRepository.save(VkUser(vkId = vkUserId, firstName = null))
        }
    }

    suspend fun saveUserMessage(user: VkUser, message: VkChatMessage) {
        val savedMessage = vkChatMessageRepository.save(message)
        vkUserChatMessageRepository.save(VkUserChatMessage(userId = user.id!!, messageId = savedMessage.id!!))
    }

}
