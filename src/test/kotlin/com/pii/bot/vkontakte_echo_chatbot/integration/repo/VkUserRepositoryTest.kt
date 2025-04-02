package com.pii.bot.vkontakte_echo_chatbot.integration.repo

import com.pii.bot.vkontakte_echo_chatbot.integration.BaseIntegrationTest
import com.pii.bot.vkontakte_echo_chatbot.repo.message.VkChatMessageRepository
import com.pii.bot.vkontakte_echo_chatbot.repo.user.VkUserChatMessage
import com.pii.bot.vkontakte_echo_chatbot.repo.user.VkUserChatMessageRepository
import com.pii.bot.vkontakte_echo_chatbot.repo.user.VkUserRepository
import com.pii.bot.vkontakte_echo_chatbot.util.TestDataFactory.createTestUser
import com.pii.bot.vkontakte_echo_chatbot.util.TestDataFactory.createTestVkMessage
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest
import org.springframework.test.context.ActiveProfiles
import kotlin.test.Test

@DataR2dbcTest
@ActiveProfiles("repository-test")
class VkUserRepositoryTest : BaseIntegrationTest() {

    @Autowired
    private lateinit var vkUserRepository: VkUserRepository

    @Autowired
    private lateinit var vkMessageRepository: VkChatMessageRepository

    @Autowired
    private lateinit var vkUserChatMessageRepository: VkUserChatMessageRepository

    @BeforeEach
    fun clearDatabase() = runTest {
        vkUserChatMessageRepository.deleteAll()
        vkUserRepository.deleteAll()
        vkMessageRepository.deleteAll()
    }


    @Test
    fun `should save and retrieve user by vkId`() = runTest {
        val testUser = createTestUser()
        val savedUser = vkUserRepository.save(testUser)
        vkUserRepository.findByVkId(testUser.vkId) shouldBe savedUser
    }

    @Test
    fun `should return null when user not found`() = runTest {
        vkUserRepository.findByVkId(666) shouldBe null
    }

    @Test
    fun `should count all users`() = runTest {
        vkUserRepository.save(createTestUser(vkId = 1))
        vkUserRepository.save(createTestUser(vkId = 2))
        vkUserRepository.count() shouldBe 2
    }

    @Test
    fun `should count messages by user vkId and exact word`() = runTest {
        val user = vkUserRepository.save(createTestUser(vkId = 123))
        val messages = listOf(
            vkMessageRepository.save(createTestVkMessage(text = "hello world")),
            vkMessageRepository.save(createTestVkMessage(text = "hello, friend!")),
            vkMessageRepository.save(createTestVkMessage(text = "say hello")),
            vkMessageRepository.save(createTestVkMessage(text = "HELLO"))
        )
        messages.forEach { message ->
            vkUserChatMessageRepository.save(
                VkUserChatMessage(
                    userId = user.id!!, messageId = message.id!!
                )
            )
        }

        vkUserChatMessageRepository.countByUserVkIdAndExactWord(123, "hello") shouldBe 4
        vkUserChatMessageRepository.countByUserVkIdAndExactWord(123, "friend") shouldBe 1
        vkUserChatMessageRepository.countByUserVkIdAndExactWord(123, "world") shouldBe 1
        vkUserChatMessageRepository.countByUserVkIdAndExactWord(123, "bye") shouldBe 0
    }
}
