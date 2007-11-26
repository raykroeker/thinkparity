create table TPSD_MSG_CHANNEL(
    CHANNEL_ID smallint generated always as identity(start with 100),
    CHANNEL_NAME varchar(256) not null,
    primary key(CHANNEL_ID),
    unique(CHANNEL_NAME)
);
create table TPSD_MSG(
    NODE_USERNAME varchar(32) not null,
    MSG_ID bigint generated always as identity(start with 1),
    MSG_TIMESTAMP timestamp not null,
    MSG_CHANNEL smallint not null,
    MSG varchar(64) not null,
    primary key(MSG_ID),
    foreign key(MSG_CHANNEL) references TPSD_MSG_CHANNEL(CHANNEL_ID)
);
create index TPSD_MSG_IX_0 on TPSD_MSG(MSG_TIMESTAMP);
create table TPSD_MSG_DATA(
    MSG_ID bigint not null,
    DATA_ORDINAL smallint not null,
    DATA_NAME varchar(64) not null,
    DATA_TYPE varchar(32) not null,
    DATA_VALUE clob,
    primary key(MSG_ID,DATA_ORDINAL),
    unique(MSG_ID,DATA_NAME),
    foreign key(MSG_ID) references TPSD_MSG(MSG_ID)
);

