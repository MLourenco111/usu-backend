-- ============================================
-- SCHEMA
-- ============================================
CREATE SCHEMA IF NOT EXISTS usu;
SET search_path TO usu;

-- ============================================
-- Tabela de usuários
-- ============================================
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    version BIGINT NOT NULL,
    status BOOLEAN NOT NULL DEFAULT TRUE,
    createdAt TIMESTAMPTZ NOT NULL DEFAULT now(),
    updatedAt TIMESTAMPTZ NOT NULL DEFAULT now(),
    login VARCHAR(80) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(50) NOT NULL,
    name VARCHAR(200),
    document VARCHAR(30) NOT NULL UNIQUE,
    birthday DATE,
    CONSTRAINT users_email_unique UNIQUE (email),
    CONSTRAINT users_login_unique UNIQUE (login),
    CONSTRAINT users_document_unique UNIQUE (document)
);

-- ============================================
-- Tabela de tipos de usuário
-- ============================================
CREATE TABLE IF NOT EXISTS users_type (
    id BIGSERIAL PRIMARY KEY,
    type VARCHAR(50) NOT NULL,
    CONSTRAINT uq_user_type_type UNIQUE (type)
);

-- ============================================
-- Tabela de telefones (1:N com usuário)
-- ============================================
CREATE TABLE IF NOT EXISTS phones (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    number VARCHAR(40) NOT NULL,
    type VARCHAR(50),
    CONSTRAINT fk_phones_users_user_id FOREIGN KEY (user_id) REFERENCES users (id)
);

-- ============================================
-- Tabela de endereços
-- ============================================
CREATE TABLE IF NOT EXISTS address (
    id BIGSERIAL PRIMARY KEY,
    street VARCHAR(255),
    number VARCHAR(50),
    city VARCHAR(150),
    state VARCHAR(100),
    country VARCHAR(100),
    zip_code VARCHAR(30)
);

-- ============================================
-- Relacionamento many-to-many users <-> types
-- ============================================
CREATE TABLE IF NOT EXISTS users_users_type (
    user_id BIGINT NOT NULL,
    type_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, type_id),
    CONSTRAINT fk_users_user_id FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_users_type_type_id FOREIGN KEY (type_id) REFERENCES users_type (id)
);

-- ============================================
-- Relacionamento usuário <-> endereço
-- ============================================
CREATE TABLE IF NOT EXISTS users_address (
    user_id BIGINT NOT NULL,
    address_id BIGINT NOT NULL,
    address_type VARCHAR(30) NOT NULL DEFAULT 'HOME',
    complement VARCHAR(255),
    is_primary BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (user_id, address_id),
    CONSTRAINT fk_users_address_users_user_id FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_users_address_address_address_id FOREIGN KEY (address_id) REFERENCES address (id)
);

-- ============================================
-- Índices
-- ============================================
CREATE INDEX IF NOT EXISTS idx_users_login ON users (login);
CREATE INDEX IF NOT EXISTS idx_users_email ON users (email);
CREATE INDEX IF NOT EXISTS ix_users_address_user ON users_address (user_id);
CREATE INDEX IF NOT EXISTS ix_users_address_address ON users_address (address_id);
CREATE INDEX IF NOT EXISTS ix_phones_user_id ON phones (user_id);
CREATE INDEX IF NOT EXISTS ix_users_users_type_type_id ON users_users_type (type_id);

CREATE INDEX IF NOT EXISTS address_idx_id ON address (id);
CREATE INDEX IF NOT EXISTS phones_idx_user_id ON phones (user_id);
CREATE INDEX IF NOT EXISTS users_idx_id ON users (id);
CREATE INDEX IF NOT EXISTS users_address_idx_user_id ON users_address (user_id);
CREATE INDEX IF NOT EXISTS users_type_idx_id ON users_type (id);
CREATE INDEX IF NOT EXISTS users_type_idx_user_id ON users_users_type (user_id);

-- ============================================
-- Tipos de usuário padrão (somente se não existirem)
-- ============================================
INSERT INTO users_type (id, type)
SELECT 1, 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM users_type WHERE id = 1);

INSERT INTO users_type (id, type)
SELECT 2, 'CUSTOMER'
WHERE NOT EXISTS (SELECT 1 FROM users_type WHERE id = 2);

INSERT INTO users_type (id, type)
SELECT 3, 'RESTAURANT_OWNER'
WHERE NOT EXISTS (SELECT 1 FROM users_type WHERE id = 3);

-- ============================================
-- Usuário padrão (somente se não existir)
-- ============================================
INSERT INTO users (login, email, password, name, document, version)
SELECT 'admin', 'admin@fiap.com', 'admin123', 'Administrador', '00000000000', 1
WHERE NOT EXISTS (SELECT 1 FROM users WHERE login = 'admin');

-- ============================================
-- Vincular usuário padrão com tipo de usuário
-- ============================================
INSERT INTO users_users_type (user_id, type_id) SELECT 1,1 WHERE NOT exists (SELECT 1 FROM users_users_type WHERE user_id = 1 and type_id = 1)


