# Vkontakte Echo Chatbot

В рамках задания нужно создать бота, который будет цитировать присланный ему текст. Нельзя использовать готовые библиотеки для реализации VkApi.

## Стек

- **VK API (Callback API)**
- **Kotlin (JDK 21), Kotlin Coroutines, Spring Boot 3.4.x, Spring WebFlux**
- **Spring Test, MockK, Springmockk, MockWebServer, JUnit 5, AssertJ,  ()**
- **ngrok** – HTTPS-туннель 

## Запуск

1. **Настроить ngrok:**
   ```sh
   ngrok http 8080
   ```
   Скопировать HTTPS-адрес.

2. **Настроить Callback API в VK:**
    - Ввести `https://xyz.ngrok-free.app/vk` в настройках API группы
    - Указать `confirmation-code`
    - Подтвердить сервер

3. **Запустить Spring Boot приложение c env-параметрами VK_ACCESS_TOKEN, VK_CONFIRMATION_CODE и VK_GROUP_ID:**
   ```sh
   ./gradlew bootRun
   ```

4. **Написать боту в ЛС**

## Дополнительно

- Сервис подсчёта статистики сообщений (in-memory)
- Асинхронный (non-blocking I/O) стек: Spring WebFlux (Project Reactor) + Kotlin Coroutines

## TODO

- Реальное хранилище для реализации статистики сообщений. PostgreSQL R2DBC, Elasticsearch,.. (отдельная ветка non-blocking-storage)
- 
