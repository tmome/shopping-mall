CREATE TABLE IF NOT EXISTS members (
  id BIGINT NOT NULL AUTO_INCREMENT,
  provider VARCHAR(20) NOT NULL,
  provider_id VARCHAR(100) NOT NULL,
  email VARCHAR(200) NULL,
  name VARCHAR(80) NOT NULL,
  role VARCHAR(20) NOT NULL DEFAULT 'USER',
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  PRIMARY KEY (id),
  UNIQUE KEY uk_members_provider_provider_id (provider, provider_id),
  KEY ix_members_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS signup_verifications (
  id BIGINT NOT NULL AUTO_INCREMENT,
  verification_id VARCHAR(64) NOT NULL,
  channel VARCHAR(20) NOT NULL,
  recipient VARCHAR(190) NOT NULL,
  purpose VARCHAR(30) NOT NULL DEFAULT 'SIGN_UP',
  code_hash VARCHAR(255) NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'REQUESTED',
  attempt_count INT NOT NULL DEFAULT 0,
  expires_at DATETIME(6) NOT NULL,
  verified_at DATETIME(6) NULL,
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  PRIMARY KEY (id),
  UNIQUE KEY uk_signup_verifications_verification_id (verification_id),
  KEY ix_signup_verifications_recipient_purpose_status (recipient, purpose, status),
  KEY ix_signup_verifications_expires_at (expires_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS categories (
  id BIGINT NOT NULL AUTO_INCREMENT,
  parent_id BIGINT NULL,
  name VARCHAR(80) NOT NULL,
  display_order INT NOT NULL DEFAULT 0,
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  PRIMARY KEY (id),
  UNIQUE KEY uk_categories_parent_name (parent_id, name),
  CONSTRAINT fk_categories_parent
    FOREIGN KEY (parent_id) REFERENCES categories (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS product_categories (
  product_id BIGINT NOT NULL,
  category_id BIGINT NOT NULL,
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  PRIMARY KEY (product_id, category_id),
  CONSTRAINT fk_product_categories_product
    FOREIGN KEY (product_id) REFERENCES products (id),
  CONSTRAINT fk_product_categories_category
    FOREIGN KEY (category_id) REFERENCES categories (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS carts (
  id BIGINT NOT NULL AUTO_INCREMENT,
  member_id BIGINT NOT NULL,
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  PRIMARY KEY (id),
  UNIQUE KEY uk_carts_member (member_id),
  CONSTRAINT fk_carts_member
    FOREIGN KEY (member_id) REFERENCES members (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS cart_items (
  id BIGINT NOT NULL AUTO_INCREMENT,
  cart_id BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  quantity INT NOT NULL,
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  PRIMARY KEY (id),
  UNIQUE KEY uk_cart_items_cart_product (cart_id, product_id),
  CONSTRAINT fk_cart_items_cart
    FOREIGN KEY (cart_id) REFERENCES carts (id),
  CONSTRAINT fk_cart_items_product
    FOREIGN KEY (product_id) REFERENCES products (id),
  CONSTRAINT chk_cart_items_quantity CHECK (quantity > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS payments (
  id BIGINT NOT NULL AUTO_INCREMENT,
  order_id BIGINT NOT NULL,
  payment_key VARCHAR(190) NULL,
  method VARCHAR(40) NOT NULL,
  amount DECIMAL(12,2) NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'READY',
  approved_at DATETIME(6) NULL,
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  PRIMARY KEY (id),
  UNIQUE KEY uk_payments_order (order_id),
  UNIQUE KEY uk_payments_payment_key (payment_key),
  CONSTRAINT fk_payments_order
    FOREIGN KEY (order_id) REFERENCES purchase_orders (id),
  CONSTRAINT chk_payments_amount CHECK (amount >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT IGNORE INTO categories (id, parent_id, name, display_order)
VALUES
  (1, NULL, 'Electronics', 1),
  (2, NULL, 'Fashion', 2),
  (3, NULL, 'Home', 3);

INSERT INTO members (id, provider, provider_id, email, name, role)
VALUES
  (1, 'KAKAO', 'local-kakao-user', 'kakao.user@example.com', 'Local Kakao User', 'USER'),
  (2, 'NAVER', 'local-naver-user', 'naver.user@example.com', 'Local Naver User', 'USER')
ON DUPLICATE KEY UPDATE
  email = VALUES(email),
  name = VALUES(name),
  role = VALUES(role);

INSERT INTO signup_verifications (
  verification_id,
  channel,
  recipient,
  purpose,
  code_hash,
  status,
  attempt_count,
  expires_at,
  verified_at
)
VALUES
  (
    'local-signup-email-verified',
    'EMAIL',
    'signup.user@example.com',
    'SIGN_UP',
    '$2a$10$local.signup.verification.code.hash.placeholder',
    'VERIFIED',
    1,
    DATE_ADD(CURRENT_TIMESTAMP(6), INTERVAL 10 MINUTE),
    CURRENT_TIMESTAMP(6)
  )
ON DUPLICATE KEY UPDATE
  status = VALUES(status),
  attempt_count = VALUES(attempt_count),
  expires_at = VALUES(expires_at),
  verified_at = VALUES(verified_at);
