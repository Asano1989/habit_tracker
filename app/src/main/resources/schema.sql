DROP TABLE IF EXISTS users;
CREATE TABLE users (
    id              SERIAL          PRIMARY KEY,
    account_id      VARCHAR(100)    UNIQUE NOT NULL,
    password        VARCHAR(255)    NOT NULL,
    name            VARCHAR(100)    NOT NULL
);
DROP TABLE IF EXISTS learning_subjects;
CREATE TABLE learning_subjects (
    id              SERIAL          PRIMARY KEY,
    user_id         INTEGER         NOT NULL,
    name            VARCHAR(500)    UNIQUE NOT NULL,
    description     TEXT
);
DROP TABLE IF EXISTS recordss;
CREATE TABLE records (
    id                      SERIAL          PRIMARY KEY,
    learning_subject_id     INTEGER         NOT NULL,
    date                    DATE            NOT NULL,
    start_time              TIME WITHOUT TIME ZONE,
    stop_time               TIME WITHOUT TIME ZONE,
    sum_time                TIME WITHOUT TIME ZONE,
    pomodoro                INTEGER         DEFAULT 0,
    uses_pomodoro           BOOLEAN         DEFAULT TRUE,
    memo                    TEXT,
    break_time              TIME WITHOUT TIME ZONE,
    is_published            BOOLEAN         DEFAULT FALSE
);
DROP TABLE IF EXISTS todo;
CREATE TABLE todo (
    id          SERIAL PRIMARY KEY,
    title       TEXT,
    importance  INTEGER,
    urgency     INTEGER,
    deadline    DATE,
    done        TEXT
);