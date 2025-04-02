GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA vk_chat TO message;

CREATE TABLE IF NOT EXISTS vk_chat.vk_users
(
    id         BIGSERIAL PRIMARY KEY,
    vk_id      INTEGER NOT NULL UNIQUE,
    first_name VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS vk_chat.vk_chat_messages
(
    id          BIGSERIAL PRIMARY KEY,
    text        TEXT      NOT NULL,
    vkTimestamp TIMESTAMP NOT NULL,
    peer_id     BIGINT    NOT NULL
);

CREATE TABLE IF NOT EXISTS vk_chat.vk_user_chat_messages
(
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT NOT NULL REFERENCES vk_users (id) ON DELETE CASCADE,
    message_id BIGINT NOT NULL REFERENCES vk_chat_messages (id) ON DELETE CASCADE,
    UNIQUE (user_id, message_id)
);

CREATE INDEX IF NOT EXISTS idx_vk_users_vk_id ON vk_chat.vk_users(vk_id);
CREATE INDEX IF NOT EXISTS idx_vk_user_messages_user_id ON vk_chat.vk_user_chat_messages(user_id);
CREATE INDEX IF NOT EXISTS idx_vk_user_messages_message_id ON vk_chat.vk_user_chat_messages(message_id);

CREATE EXTENSION IF NOT EXISTS pg_trgm;
CREATE INDEX IF NOT EXISTS idx_vk_chat_messages_text ON vk_chat.vk_chat_messages USING gin(text gin_trgm_ops);
