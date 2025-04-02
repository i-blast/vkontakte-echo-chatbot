package com.pii.bot.vkontakte_echo_chatbot.integration.repo

import com.pii.bot.vkontakte_echo_chatbot.integration.BaseIntegrationTest
import com.pii.bot.vkontakte_echo_chatbot.repo.message.VkChatMessageRepository
import com.pii.bot.vkontakte_echo_chatbot.util.TestDataFactory.createTestVkMessage
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest
import org.springframework.test.context.ActiveProfiles
import kotlin.test.Test

@DataR2dbcTest
@ActiveProfiles("repository-test")
class VkChatMessageRepositoryTest : BaseIntegrationTest() {

    @Autowired
    private lateinit var vkChatMessageRepository: VkChatMessageRepository

    @BeforeEach
    fun clearDatabase() = runTest {
        vkChatMessageRepository.deleteAll()
    }

    @Test
    fun `should save and find message by id`() = runTest {
        val message = createTestVkMessage()
        val savedMessage = vkChatMessageRepository.save(message)
        val foundMessage = vkChatMessageRepository.findById(savedMessage.id!!)
        foundMessage shouldBe savedMessage
    }

    @Test
    fun `should delete message`() = runTest {
        val message = vkChatMessageRepository.save(createTestVkMessage())
        vkChatMessageRepository.deleteById(message.id!!)
        val foundMessage = vkChatMessageRepository.findById(message.id!!)
        foundMessage shouldBe null
    }

    @Test
    fun `countByExactWord should match exact word with word boundaries`() = runTest {
        val messages = listOf(
            createTestVkMessage(text = "hello world"),
            createTestVkMessage(text = "hello, world!"),
            createTestVkMessage(text = "helloworld"),
            createTestVkMessage(text = "say hello to me"),
            createTestVkMessage(text = "HELLO")
        )
        vkChatMessageRepository.saveAll(messages).toList()
        vkChatMessageRepository.countByExactWord("hello") shouldBe 4
        vkChatMessageRepository.countByExactWord("helloworld") shouldBe 1
    }

    @Test
    fun `countByExactWord should be case insensitive`() = runTest {
        vkChatMessageRepository.save(createTestVkMessage())
        vkChatMessageRepository.countByExactWord("test") shouldBe 1
        vkChatMessageRepository.countByExactWord("TEST") shouldBe 1
    }
}
