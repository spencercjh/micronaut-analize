-- auto-generated definition
create table releases
(
    id               int auto_increment
        primary key,
    title            varchar(64)                         not null comment 'actual display name like v0.7.4-d20200608-1-9-g87eca2d39',
    `group`          varchar(64)                         not null,
    project          varchar(64)                         not null,
    stages           varchar(64)                         not null comment 'A,B,C',
    release_type     varchar(10)                         not null comment 'a enum defined in inceptioapi',
    release_note     text                                not null,
    content          text                                null,
    files            json                                not null,
    create_time      timestamp                           not null,
    update_time      timestamp                           not null,
    sync_create_time timestamp default CURRENT_TIMESTAMP not null,
    sync_update_time timestamp default CURRENT_TIMESTAMP not null
);

create index releases_stage_index
    on releases (stages);

create index releases_title_release_type_index
    on releases (title, release_type);

