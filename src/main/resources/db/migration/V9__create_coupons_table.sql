CREATE TABLE coupons
(
    id                   BIGINT AUTO_INCREMENT NOT NULL,
    create_date          datetime NULL,
    update_date          datetime NULL,
    code                 VARCHAR(50)    NOT NULL,
    `description`        VARCHAR(255) NULL,
    discount_type        VARCHAR(255)   NOT NULL,
    discount_value       DECIMAL(15, 2) NOT NULL,
    minimum_order_amount DECIMAL(15, 2) NULL,
    maximum_discount     DECIMAL(15, 2) NULL,
    start_date           datetime       NOT NULL,
    end_date             datetime       NOT NULL,
    usage_limit          INT NULL,
    usage_count          INT NULL,
    active               BIT(1) NULL,
    CONSTRAINT pk_coupons PRIMARY KEY (id)
);

ALTER TABLE coupons
    ADD CONSTRAINT uc_coupons_code UNIQUE (code);