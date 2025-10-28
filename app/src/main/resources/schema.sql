DROP TABLE IF EXISTS users;
CREATE TABLE users (
    id              SERIAL          PRIMARY KEY,
    user_id          VARCHAR(100)    UNIQUE NOT NULL,
    password        VARCHAR(255)    NOT NULL,
    name            VARCHAR(100)    NOT NULL
);