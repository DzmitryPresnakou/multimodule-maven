CREATE DATABASE hotel_repository;

CREATE TABLE hotel
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(128) NOT NULL,
    photo VARCHAR(128) NOT NULL
);

CREATE TABLE users
(
    id           SERIAL PRIMARY KEY,
    first_name   VARCHAR(128)                  NOT NULL,
    last_name    VARCHAR(128)                  NOT NULL,
    email        VARCHAR(128)                  NOT NULL unique,
    phone        VARCHAR(64)                   NOT NULL,
    photo        VARCHAR(128)                  NOT NULL,
    birth_date   DATE                          NOT NULL,
    money        INT                           NOT NULL,
    password     VARCHAR(128)                  NOT NULL,
    role         VARCHAR(128) 		       NOT NULL,
    is_active    BOOLEAN          DEFAULT true NOT NULL,
);

CREATE TABLE room
(
    id             SERIAL PRIMARY KEY,
    occupancy      INT                        NOT NULL,
    class          VARCHAR(64) 		          NOT NULL,
    photo          VARCHAR(128)               NOT NULL,
    price_per_day  INT	                      NOT NULL,
    hotel_id       INT REFERENCES hotel (id)  NOT NULL
);


CREATE TABLE room_order
(
    id              	SERIAL PRIMARY KEY,
    user_id         	INT REFERENCES users (id)          NOT NULL,
    room_id         	INT REFERENCES room (id)           NOT NULL,
    status              VARCHAR(64) 	    	           NOT NULL,
    payment_status      VARCHAR(64) 		               NOT NULL,
    check_in_date   	DATE                               NOT NULL,
    check_out_date   	DATE                               NOT NULL
);