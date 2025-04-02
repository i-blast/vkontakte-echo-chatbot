package com.pii.bot.vkontakte_echo_chatbot.integration

import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
abstract class BaseIntegrationTest {

    companion object {

        protected val postgreSQLContainer = PostgreSQLContainer("postgres:17.4").apply {
            withDatabaseName("testdb")
            withUsername("testuser")
            withPassword("testpassword")
            start()
        }

        @DynamicPropertySource
        @JvmStatic
        fun configureProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.flyway.url", postgreSQLContainer::getJdbcUrl)
            registry.add("spring.flyway.user", postgreSQLContainer::getUsername)
            registry.add("spring.flyway.password", postgreSQLContainer::getPassword)

            registry.add("spring.r2dbc.url") {
                "r2dbc:postgresql://${postgreSQLContainer.host}:${postgreSQLContainer.getMappedPort(5432)}/testdb?schema=vk_chat"
            }
            registry.add("spring.datasource.username", postgreSQLContainer::getUsername)
            registry.add("spring.datasource.password", postgreSQLContainer::getPassword)
        }
    }
}
