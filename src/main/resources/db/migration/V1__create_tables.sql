CREATE SCHEMA chefia;

CREATE TABLE chefia.user_types (
    nr_seq_user_type BIGINT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

CREATE TABLE chefia.users
(
    nr_seq_user  BIGINT PRIMARY KEY,
    name         VARCHAR(100)       NOT NULL,
    email        VARCHAR(50) UNIQUE NOT NULL,
    login        VARCHAR(30) UNIQUE NOT NULL,
    password     VARCHAR(255)        NOT NULL,
    active       BOOLEAN            NOT NULL DEFAULT TRUE,
    created_at   TIMESTAMP,
    updated_at   TIMESTAMP,
    user_type_id BIGINT,
    FOREIGN KEY (user_type_id) REFERENCES user_types (nr_seq_user_type)
);

CREATE TABLE chefia.addresses
(
    nr_seq_address BIGINT PRIMARY KEY,
    street   VARCHAR(50) NOT NULL,
    number   INT,
    city     VARCHAR(50),
    state    VARCHAR(50),
    country  VARCHAR(50),
    nr_seq_user  BIGINT,
    FOREIGN KEY (nr_seq_user) REFERENCES users (nr_seq_user)

);

CREATE TABLE chefia.restaurants (
    nr_seq_restaurant BIGINT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL,
    restaurant_type VARCHAR(50) NOT NULL,
    user_id BIGINT NOT NULL,
    address_id BIGINT NOT NULL,

    CONSTRAINT fk_restaurant_user FOREIGN KEY (user_id) REFERENCES users(nr_seq_user),
    CONSTRAINT fk_restaurant_address FOREIGN KEY (address_id) REFERENCES addresses(nr_seq_address)
);

CREATE TABLE chefia.business_hours (
    nr_seq_business_hours BIGINT PRIMARY KEY,
    week_day VARCHAR(20) NOT NULL,
    opening_time TIME NOT NULL,
    closing_time TIME NOT NULL,
    restaurant_id BIGINT NOT NULL,

    CONSTRAINT fk_business_hours_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurants(nr_seq_restaurant)
);

CREATE TABLE chefia.menu_items (
    nr_seq_menu_item BIGINT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    available_only_on_site BOOLEAN DEFAULT FALSE,
    image_path VARCHAR(255),
    restaurant_id BIGINT NOT NULL,

    CONSTRAINT fk_menu_item_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurants(nr_seq_restaurant)
);