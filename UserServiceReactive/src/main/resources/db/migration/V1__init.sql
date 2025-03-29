create table if not exists users
(
    id         serial primary key,
    username   varchar(255) unique,
    password   varchar(255) not null,
    email      varchar(255),
    created_at timestamp(6),
    updated_at timestamp(6),
    first_name varchar(255),
    last_name  varchar(255)
);