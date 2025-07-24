CREATE TABLE address
(
    id      BIGINT PRIMARY KEY AUTO_INCREMENT,
    street  VARCHAR(255) NOT NULL,
    number  INT,
    city    VARCHAR(100),
    state   VARCHAR(100),
    country VARCHAR(100)
);

CREATE TABLE users
(
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    name         VARCHAR(255)        NOT NULL,
    email        VARCHAR(255) UNIQUE NOT NULL,
    login        VARCHAR(100)        NOT NULL,
    password     VARCHAR(255)        NOT NULL,
    active       BOOLEAN             NOT NULL DEFAULT TRUE,
    created_at   DATE,
    updated_at   DATE,
    deleted_at   DATE,
    profile_type VARCHAR(30),
    CONSTRAINT chk_profile_type CHECK (profile_type IN ('CLIENT', 'DONO'))
);

CREATE TABLE user_address
(
    user_id    BIGINT NOT NULL,
    address_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, address_id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (address_id) REFERENCES address (id)
);
