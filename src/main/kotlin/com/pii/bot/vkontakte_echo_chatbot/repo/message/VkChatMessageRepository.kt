package com.pii.bot.vkontakte_echo_chatbot.repo.message

import org.springframework.data.annotation.Id
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.time.Instant

interface VkChatMessageRepository : CoroutineCrudRepository<VkChatMessage, Long> {

    @Query(
        """
        SELECT COUNT(*) FROM vk_chat.vk_chat_messages 
        WHERE text ~* ('(^|[^а-яА-Яa-zA-Z0-9_])' || :word || '([^а-яА-Яa-zA-Z0-9_]|$)')
    """
    )
    suspend fun countByExactWord(word: String): Long
}

@Table("vk_chat_messages")
data class VkChatMessage(
    @Id
    val id: Long? = null,
    val text: String,
    val vkTimestamp: Instant,
    val peerId: Long
)
