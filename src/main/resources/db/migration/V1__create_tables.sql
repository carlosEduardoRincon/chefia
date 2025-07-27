CREATE TABLE users
(
    nr_seq_user  BIGSERIAL PRIMARY KEY,
    name         VARCHAR(100)       NOT NULL,
    email        VARCHAR(50) UNIQUE NOT NULL,
    login        VARCHAR(30) UNIQUE NOT NULL,
    password     VARCHAR(30)        NOT NULL,
    active       BOOLEAN            NOT NULL DEFAULT TRUE,
    created_at   DATE,
    updated_at   DATE,
    profile_type VARCHAR(30),
    CONSTRAINT chk_profile_type CHECK (profile_type IN ('CLIENT', 'OWNER'))
);

CREATE TABLE addresses
(
    nr_seq_address BIGSERIAL PRIMARY KEY,
    street   VARCHAR(50) NOT NULL,
    number   INT,
    city     VARCHAR(50),
    state    VARCHAR(50),
    country  VARCHAR(50),
    nr_seq_user  BIGINT NOT NULL,
    FOREIGN KEY (nr_seq_user) REFERENCES users (nr_seq_user)
);
