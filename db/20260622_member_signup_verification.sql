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
