create schema if not exists users;

CREATE TABLE users.address
(
    id               uuid PRIMARY KEY,
    town             VARCHAR(100) NOT NULL,
    postal_code      VARCHAR(10)  NOT NULL,
    house_number     VARCHAR(20)  NOT NULL,
    apartment_number VARCHAR(20),
    city             VARCHAR(100) NOT NULL,
    street           VARCHAR(100) NOT NULL,
    created_at       DATE         NOT NULL,
    modified_at      DATE         NOT NULL,
    deleted          boolean      NOT NULL
);

CREATE TABLE users.company
(
    id                 uuid PRIMARY KEY,
    address_id         uuid         not null,
    name               VARCHAR(100) NOT NULL,
    tax_identification VARCHAR(20)  NOT NULL,
    email              VARCHAR(100) NOT NULL,
    phone_number       VARCHAR(12),
    created_at         DATE         NOT NULL,
    modified_at        DATE         NOT NULL,
    deleted            boolean      NOT NULL,
    CONSTRAINT fk_address FOREIGN KEY (address_id) REFERENCES users.address (id)
);

CREATE TABLE users.user_settings
(
    id                 uuid PRIMARY KEY,
    sms_notification   boolean NOT NULL,
    app_notification   boolean NOT NULL,
    email_notification boolean NOT NULL,
    created_at         DATE    NOT NULL,
    modified_at        DATE    NOT NULL,
    deleted            boolean NOT NULL
);

CREATE TABLE users.users
(
    id               uuid PRIMARY KEY,
    company_id       uuid,
    address_id       uuid,
    user_settings_id uuid         NOT NULL,
    name             VARCHAR(100) NOT NULL,
    surname          VARCHAR(100) NOT NULL,
    email            VARCHAR(100) NOT NULL,
    phone_number     VARCHAR(12),
    password         VARCHAR(200) NOT NULL,
    role             VARCHAR(100) NOT NULL,
    created_at       DATE         NOT NULL,
    modified_at      DATE         NOT NULL,
    last_activity_at DATE         NOT NULL,
    deleted          boolean      NOT NULL,
    enabled          boolean      NOT NULL,
    CONSTRAINT fk_company FOREIGN KEY (company_id) REFERENCES users.company (id),
    CONSTRAINT fk_address FOREIGN KEY (address_id) REFERENCES users.address (id),
    CONSTRAINT fk_user_settings FOREIGN KEY (user_settings_id) REFERENCES users.user_settings (id)
);
