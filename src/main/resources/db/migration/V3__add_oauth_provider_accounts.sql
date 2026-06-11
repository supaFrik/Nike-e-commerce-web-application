CREATE TABLE oauth_provider_accounts
(
    id                 BIGINT AUTO_INCREMENT NOT NULL,
    create_date        datetime NULL,
    update_date        datetime NULL,
    user_id            BIGINT       NOT NULL,
    provider           VARCHAR(50)  NOT NULL,
    provider_subject   VARCHAR(255) NOT NULL,
    email_at_link_item VARCHAR(255) NOT NULL,
    CONSTRAINT pk_oauth_provider_accounts PRIMARY KEY (id)
);

ALTER TABLE oauth_provider_accounts
    ADD CONSTRAINT uk_oauth_provider_subject UNIQUE (provider, provider_subject);

ALTER TABLE oauth_provider_accounts
    ADD CONSTRAINT FK_OAUTH_PROVIDER_ACCOUNTS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);