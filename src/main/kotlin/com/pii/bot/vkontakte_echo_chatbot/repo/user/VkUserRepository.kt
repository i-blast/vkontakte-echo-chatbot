package com.pii.bot.vkontakte_echo_chatbot.repo.user

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface VkUserRepository : CoroutineCrudRepository<VkUser, Long> {

    suspend fun findByVkId(vkId: Int): VkUser?

}

@Table("vk_users")
data class VkUser(
    @Id
    val id: Long? = null,
    val vkId: Int,
    val firstName: String? = null,
)
