CREATE TABLE users
(
    nr_seq_user  BIGSERIAL PRIMARY KEY,
    name         VARCHAR(255)        NOT NULL,
    email        VARCHAR(255) UNIQUE NOT NULL,
    login        VARCHAR(100)        NOT NULL,
    password     VARCHAR(255)        NOT NULL,
    active       BOOLEAN             NOT NULL DEFAULT TRUE,
    created_at   DATE,
    updated_at   DATE,
    deleted_at   DATE,
    profile_type VARCHAR(30),
    CONSTRAINT chk_profile_type CHECK (profile_type IN ('CLIENT', 'OWNER'))
);

CREATE TABLE addresses
(
    nr_seq_address BIGSERIAL PRIMARY KEY,
    street   VARCHAR(255) NOT NULL,
    number   INT,
    city     VARCHAR(100),
    state    VARCHAR(100),
    country  VARCHAR(100),
    nr_seq_user  BIGINT NOT NULL,
    FOREIGN KEY (nr_seq_user) REFERENCES users (nr_seq_user)
);
