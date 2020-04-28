drop table if exists casino_details;

create table casino_details
(
    casino_id      serial primary key,
    casino_name    varchar not null,
    balance_amount double precision default 0,
    status         boolean,
    created_at     timestamp        default now(),
    modified_at    timestamp        default now()
);

drop table if exists dealer_details;

create table dealer_details
(
    dealer_id   serial primary key,
    dealer_name varchar not null,
    casino_id   integer not null,
    status      smallint,
    created_at  timestamp default now(),
    modified_at timestamp default now()
);

drop table if exists game_details;

create table game_details
(
    game_id       serial primary key,
    start_time    timestamp,
    end_time      timestamp,
    status        varchar,
    dealer_id     integer,
    thrown_number integer,
    created_at    timestamp default now(),
    modified_at   timestamp default now()
);

drop table if exists user_details;

create table user_details
(
    user_id        serial primary key,
    user_name      varchar,
    balance_amount double precision,
    current_casino integer,
    status         boolean,
    created_at     timestamp default now(),
    modified_at    timestamp default now()
);

drop table if exists bet_details;

create table bet_details
(
    bet_id       serial primary key,
    bet_amount       double precision,
    user_id      integer,
    betting_time timestamp default now(),
    bet_number   integer,
    game_id      integer,
    bet_status   varchar,
    created_at timestamp default now(),
    modified_at timestamp default now()
);