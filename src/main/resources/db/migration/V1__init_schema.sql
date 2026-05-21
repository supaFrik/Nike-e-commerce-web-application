CREATE TABLE IF NOT EXISTS users (
  id BIGINT NOT NULL AUTO_INCREMENT,
  create_date DATETIME(6) NULL,
  update_date DATETIME(6) NULL,
  username VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  role VARCHAR(255) NOT NULL,
  locked BIT(1) NULL,
  enabled BIT(1) NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_users_username (username),
  UNIQUE KEY uk_users_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS addresses (
  id BIGINT NOT NULL AUTO_INCREMENT,
  user_id BIGINT NULL,
  recipient_name VARCHAR(255) NULL,
  line1 VARCHAR(255) NULL,
  line2 VARCHAR(255) NULL,
  city VARCHAR(255) NULL,
  province VARCHAR(255) NULL,
  country VARCHAR(255) NULL,
  postal_code VARCHAR(255) NULL,
  phone VARCHAR(255) NULL,
  primary_address BIT(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (id),
  KEY idx_addresses_user_id (user_id),
  CONSTRAINT fk_addresses_user
    FOREIGN KEY (user_id) REFERENCES users (id)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS signup_verification (
  id BIGINT NOT NULL AUTO_INCREMENT,
  create_date DATETIME(6) NULL,
  update_date DATETIME(6) NULL,
  email VARCHAR(255) NULL,
  code_hash VARCHAR(255) NULL,
  attempt_count INT NULL,
  expires_at DATETIME(6) NULL,
  last_sent_at DATETIME(6) NULL,
  used_at DATETIME(6) NULL,
  PRIMARY KEY (id),
  KEY idx_signup_verification_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS category (
  id BIGINT NOT NULL AUTO_INCREMENT,
  create_date DATETIME(6) NULL,
  update_date DATETIME(6) NULL,
  name VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS products (
  id BIGINT NOT NULL AUTO_INCREMENT,
  create_date DATETIME(6) NULL,
  update_date DATETIME(6) NULL,
  name VARCHAR(255) NOT NULL,
  description TEXT NOT NULL,
  type VARCHAR(50) NOT NULL,
  category_id BIGINT NOT NULL,
  product_status VARCHAR(20) NOT NULL,
  price DECIMAL(19,2) NOT NULL,
  sale_price DECIMAL(19,2) NULL,
  PRIMARY KEY (id),
  KEY idx_products_category_id (category_id),
  KEY idx_products_status (product_status),
  CONSTRAINT fk_products_category
    FOREIGN KEY (category_id) REFERENCES category (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS product_colors (
  id BIGINT NOT NULL AUTO_INCREMENT,
  create_date DATETIME(6) NULL,
  update_date DATETIME(6) NULL,
  color_name VARCHAR(100) NOT NULL,
  hex_code VARCHAR(255) NULL,
  display_order INT NULL,
  product_id BIGINT NOT NULL,
  PRIMARY KEY (id),
  KEY idx_product_colors_product_id (product_id),
  CONSTRAINT fk_product_colors_product
    FOREIGN KEY (product_id) REFERENCES products (id)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS product_images (
  id BIGINT NOT NULL AUTO_INCREMENT,
  create_date DATETIME(6) NULL,
  update_date DATETIME(6) NULL,
  url VARCHAR(1024) NOT NULL,
  provider_public_id VARCHAR(255) NOT NULL,
  title VARCHAR(255) NULL,
  alt_text VARCHAR(512) NULL,
  is_main BIT(1) NULL DEFAULT b'0',
  order_index INT NULL DEFAULT 0,
  color_id BIGINT NOT NULL,
  PRIMARY KEY (id),
  KEY idx_product_images_color_id (color_id),
  CONSTRAINT fk_product_images_color
    FOREIGN KEY (color_id) REFERENCES product_colors (id)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS product_variants (
  id BIGINT NOT NULL AUTO_INCREMENT,
  create_date DATETIME(6) NULL,
  update_date DATETIME(6) NULL,
  sku VARCHAR(100) NULL,
  size VARCHAR(50) NULL,
  stock INT NULL DEFAULT 0,
  active BIT(1) NULL DEFAULT b'1',
  inventory_status VARCHAR(20) NULL DEFAULT 'IN_ORDER',
  color_id BIGINT NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_product_variants_color_size (color_id, size),
  KEY idx_product_variants_color_id (color_id),
  KEY idx_product_variants_sku (sku),
  CONSTRAINT fk_product_variants_color
    FOREIGN KEY (color_id) REFERENCES product_colors (id)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS cart_items (
  id BIGINT NOT NULL AUTO_INCREMENT,
  create_date DATETIME(6) NULL,
  update_date DATETIME(6) NULL,
  user_id BIGINT NOT NULL,
  variant_id BIGINT NOT NULL,
  quantity INT NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_user_variant (user_id, variant_id),
  KEY idx_cart_items_variant_id (variant_id),
  CONSTRAINT fk_cart_items_user
    FOREIGN KEY (user_id) REFERENCES users (id)
    ON DELETE CASCADE,
  CONSTRAINT fk_cart_items_variant
    FOREIGN KEY (variant_id) REFERENCES product_variants (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS orders (
  id BIGINT NOT NULL AUTO_INCREMENT,
  create_date DATETIME(6) NULL,
  update_date DATETIME(6) NULL,
  user_id BIGINT NOT NULL,
  order_status VARCHAR(20) NOT NULL DEFAULT 'PENDING_PAYMENT',
  shipping_method VARCHAR(15) NOT NULL DEFAULT 'STANDARD',
  shipping_recipient_name VARCHAR(255) NOT NULL,
  shipping_phone VARCHAR(50) NOT NULL,
  shipping_line1 VARCHAR(255) NOT NULL,
  shipping_line2 VARCHAR(255) NULL,
  shipping_city VARCHAR(100) NOT NULL,
  shipping_province VARCHAR(100) NULL,
  shipping_postal_code VARCHAR(30) NULL,
  shipping_country VARCHAR(100) NOT NULL,
  subtotal DECIMAL(15,2) NOT NULL DEFAULT 0.00,
  shipping_cost DECIMAL(15,2) NOT NULL DEFAULT 0.00,
  discount DECIMAL(15,2) NOT NULL DEFAULT 0.00,
  total DECIMAL(15,2) NOT NULL DEFAULT 0.00,
  payment_method VARCHAR(20) NOT NULL,
  PRIMARY KEY (id),
  KEY idx_orders_user_id (user_id),
  KEY idx_orders_status (order_status),
  CONSTRAINT fk_orders_user
    FOREIGN KEY (user_id) REFERENCES users (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS order_items (
  id BIGINT NOT NULL AUTO_INCREMENT,
  create_date DATETIME(6) NULL,
  update_date DATETIME(6) NULL,
  order_id BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  variant_id BIGINT NULL,
  sku VARCHAR(100) NULL,
  product_name VARCHAR(255) NOT NULL,
  size VARCHAR(20) NULL,
  color VARCHAR(50) NULL,
  unit_price DECIMAL(15,2) NOT NULL,
  quantity INT NOT NULL,
  line_total DECIMAL(15,2) NOT NULL,
  PRIMARY KEY (id),
  KEY idx_order_items_order_id (order_id),
  KEY idx_order_items_product_id (product_id),
  KEY idx_order_items_variant_id (variant_id),
  CONSTRAINT fk_order_items_order
    FOREIGN KEY (order_id) REFERENCES orders (id)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS payment_transaction (
  id BIGINT NOT NULL AUTO_INCREMENT,
  create_date DATETIME(6) NULL,
  update_date DATETIME(6) NULL,
  order_id BIGINT NOT NULL,
  provider VARCHAR(255) NOT NULL,
  txn_ref VARCHAR(100) NOT NULL,
  amount DECIMAL(15,2) NOT NULL,
  status VARCHAR(255) NOT NULL,
  response_code VARCHAR(10) NULL,
  transaction_status VARCHAR(10) NULL,
  transaction_no VARCHAR(20) NULL,
  bank_code VARCHAR(20) NULL,
  pay_date DATETIME(6) NULL,
  expire_date DATETIME(6) NULL,
  ip_address VARCHAR(50) NULL,
  ipn_processed BIT(1) NOT NULL DEFAULT b'0',
  raw_request_payload LONGTEXT NULL,
  raw_response_payload LONGTEXT NULL,
  failure_reason VARCHAR(255) NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_txn_ref (txn_ref),
  KEY idx_order_id (order_id),
  CONSTRAINT fk_payment_transaction_order
    FOREIGN KEY (order_id) REFERENCES orders (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS contact_messages (
  id BIGINT NOT NULL AUTO_INCREMENT,
  create_date DATETIME(6) NULL,
  update_date DATETIME(6) NULL,
  user_id BIGINT NULL,
  name VARCHAR(80) NULL,
  email VARCHAR(100) NULL,
  message VARCHAR(500) NOT NULL,
  PRIMARY KEY (id),
  KEY idx_contact_messages_user_id (user_id),
  CONSTRAINT fk_contact_messages_user
    FOREIGN KEY (user_id) REFERENCES users (id)
    ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
