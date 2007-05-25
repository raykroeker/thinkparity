
-- v1_0-20070516-1300

alter table ARTIFACT_VERSION alter column COMMENT set data type varchar(4096);
alter table ARTIFACT_VERSION add column NAME varchar(64) default null;
alter table CONTAINER_DRAFT add column COMMENT varchar(4096) default null;

-- create published to e-mail
create table CONTAINER_VERSION_PUBLISHED_TO_EMAIL(
CONTAINER_ID bigint not null,
CONTAINER_VERSION_ID bigint not null,
EMAIL_ID bigint not null,
PUBLISHED_ON timestamp,
primary key(CONTAINER_ID,CONTAINER_VERSION_ID,EMAIL_ID,PUBLISHED_ON),
foreign key(CONTAINER_ID,CONTAINER_VERSION_ID) references CONTAINER_VERSION(CONTAINER_ID,CONTAINER_VERSION_ID),
foreign key(EMAIL_ID) references EMAIL(EMAIL_ID));

-- create migrator table
create table MIGRATOR(
PRODUCT_NAME varchar(64) not null,
RELEASE_NAME varchar(64) not null,
DOWNLOADED_RELEASE_NAME varchar(64) default null,
primary key(PRODUCT_NAME),
unique (RELEASE_NAME));

-- drop product/release tables
drop table PRODUCT;
drop table PRODUCT_RELEASE_RESOURCE;
drop table PRODUCT_RELEASE;

-- update release
update META_DATA set META_DATA_VALUE = 'v1_0-20070516-1300' where META_DATA_KEY = 'thinkparity.release-name' and META_DATA_VALUE = 'v1_0-20070430-1500';
