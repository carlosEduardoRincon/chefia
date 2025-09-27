CREATE TABLE users
(
    nr_seq_user  BIGSERIAL AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(100)       NOT NULL,
    email        VARCHAR(50) UNIQUE NOT NULL,
    login        VARCHAR(30) UNIQUE NOT NULL,
    password     VARCHAR(255)        NOT NULL,
    active       BOOLEAN            NOT NULL DEFAULT TRUE,
    created_at   TIMESTAMP,
    updated_at   TIMESTAMP,
    user_type_id VARCHAR(30),
    FOREIGN KEY (user_type_id) REFERENCES user_types (nr_seq_user_type)
);

CREATE TABLE addresses
(
    nr_seq_address BIGSERIAL AUTO_INCREMENT PRIMARY KEY,
    street   VARCHAR(50) NOT NULL,
    number   INT,
    city     VARCHAR(50),
    state    VARCHAR(50),
    country  VARCHAR(50),
    nr_seq_user  BIGINT,
    nr_seq_restaurant BIGINT,
    FOREIGN KEY (nr_seq_user) REFERENCES users (nr_seq_user)
    FOREIGN KEY (nr_seq_restaurant) REFERENCES users (nr_seq_restaurant)
);

CREATE TABLE restaurants (
    nr_seq_restaurant BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL,
    restaurant_type VARCHAR(50) NOT NULL,
    user_id BIGINT NOT NULL,
    address_id BIGINT NOT NULL,

    CONSTRAINT fk_restaurant_user FOREIGN KEY (user_id) REFERENCES users(nr_seq_user),
    CONSTRAINT fk_restaurant_address FOREIGN KEY (address_id) REFERENCES addresses(nr_seq_address)
);

CREATE TABLE business_hours (
    nr_seq_business_hours BIGINT AUTO_INCREMENT PRIMARY KEY,
    week_day VARCHAR(20) NOT NULL,
    opening_time TIME NOT NULL,
    closing_time TIME NOT NULL,
    restaurant_id BIGINT NOT NULL,

    CONSTRAINT fk_business_hours_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurant(nr_seq_restaurant)
);

CREATE TABLE menu_items (
    nr_seq_menu_item BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    available_only_on_site BOOLEAN DEFAULT FALSE,
    image_path VARCHAR(255),
    restaurant_id BIGINT NOT NULL,

    CONSTRAINT fk_menu_item_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurant(nr_seq_restaurant)
);

CREATE TABLE user_types (
    nr_seq_user_type BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    active BOOLEAN DEFAULT TRUE,
    created_at DATETIME NOT NULL,
    updated_at DATETIME
);