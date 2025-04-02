package com.pii.bot.vkontakte_echo_chatbot.repo.user

import org.springframework.data.annotation.Id
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface VkUserChatMessageRepository : CoroutineCrudRepository<VkUserChatMessage, Long> {

    @Query(
        """
        SELECT COUNT(*) FROM vk_chat.vk_chat_messages m
        JOIN vk_chat.vk_user_chat_messages um ON m.id = um.message_id
        JOIN vk_chat.vk_users u ON um.user_id = u.id
        WHERE u.vk_id = :vkUserId
        AND m.text ~* ('(^|[^а-яА-Яa-zA-Z0-9_])' || :word || '([^а-яА-Яa-zA-Z0-9_]|$)')
    """
    )
    suspend fun countByUserVkIdAndExactWord(vkUserId: Long, word: String): Long
}

@Table("vk_user_chat_messages")
data class VkUserChatMessage(
    @Id
    val id: Long? = null,
    val userId: Long,
    val messageId: Long
)
