
-- v1_0-20070919-1200

-- create/populate content tables
create table CONTAINER_DRAFT_DOCUMENT_CONTENT(CONTAINER_ID bigint not null,DOCUMENT_ID bigint not null,CONTENT blob not null,primary key(CONTAINER_ID,DOCUMENT_ID),foreign key(CONTAINER_ID) references CONTAINER_DRAFT(CONTAINER_ID),foreign key(DOCUMENT_ID) references DOCUMENT(DOCUMENT_ID));
insert into CONTAINER_DRAFT_DOCUMENT_CONTENT(CONTAINER_ID,DOCUMENT_ID,CONTENT) select CONTAINER_ID,DOCUMENT_ID,CONTENT from CONTAINER_DRAFT_DOCUMENT;

create table DOCUMENT_VERSION_CONTENT(DOCUMENT_ID bigint not null,DOCUMENT_VERSION_ID bigint not null,CONTENT blob not null,primary key(DOCUMENT_ID, DOCUMENT_VERSION_ID),foreign key(DOCUMENT_ID, DOCUMENT_VERSION_ID) references ARTIFACT_VERSION(ARTIFACT_ID, ARTIFACT_VERSION_ID));
insert into DOCUMENT_VERSION_CONTENT (DOCUMENT_ID,DOCUMENT_VERSION_ID,CONTENT) select DOCUMENT_ID,DOCUMENT_VERSION_ID,CONTENT from DOCUMENT_VERSION;

-- create/populate temp tables
create table TMP_CONTAINER_DRAFT_DOCUMENT(CONTAINER_ID bigint not null,DOCUMENT_ID bigint not null,CONTENT_SIZE bigint not null,CONTENT_CHECKSUM varchar(256) not null,CHECKSUM_ALGORITHM varchar(16) not null,primary key(CONTAINER_ID,DOCUMENT_ID),foreign key(CONTAINER_ID) references CONTAINER_DRAFT(CONTAINER_ID),foreign key(DOCUMENT_ID) references DOCUMENT(DOCUMENT_ID));
insert into TMP_CONTAINER_DRAFT_DOCUMENT(CONTAINER_ID,DOCUMENT_ID,CONTENT_SIZE,CONTENT_CHECKSUM,CHECKSUM_ALGORITHM) select CONTAINER_ID,DOCUMENT_ID,CONTENT_SIZE,CONTENT_CHECKSUM,CHECKSUM_ALGORITHM from CONTAINER_DRAFT_DOCUMENT;

create table TMP_DOCUMENT_VERSION(DOCUMENT_ID bigint not null,DOCUMENT_VERSION_ID bigint not null,CONTENT_SIZE bigint not null,CONTENT_CHECKSUM varchar(256) not null,CHECKSUM_ALGORITHM varchar(16) not null,primary key(DOCUMENT_ID, DOCUMENT_VERSION_ID),foreign key(DOCUMENT_ID, DOCUMENT_VERSION_ID) references ARTIFACT_VERSION(ARTIFACT_ID, ARTIFACT_VERSION_ID));
insert into TMP_DOCUMENT_VERSION(DOCUMENT_ID,DOCUMENT_VERSION_ID,CONTENT_SIZE,CONTENT_CHECKSUM,CHECKSUM_ALGORITHM) select DOCUMENT_ID,DOCUMENT_VERSION_ID,CONTENT_SIZE,CONTENT_CHECKSUM,CHECKSUM_ALGORITHM from DOCUMENT_VERSION;

-- drop/create/populate original tables
drop table CONTAINER_DRAFT_DOCUMENT;
create table CONTAINER_DRAFT_DOCUMENT(CONTAINER_ID bigint not null,DOCUMENT_ID bigint not null,CONTENT_SIZE bigint not null,CONTENT_CHECKSUM varchar(256) not null,CHECKSUM_ALGORITHM varchar(16) not null,primary key(CONTAINER_ID,DOCUMENT_ID),foreign key(CONTAINER_ID) references CONTAINER_DRAFT(CONTAINER_ID),foreign key(DOCUMENT_ID) references DOCUMENT(DOCUMENT_ID));
insert into CONTAINER_DRAFT_DOCUMENT(CONTAINER_ID,DOCUMENT_ID,CONTENT_SIZE,CONTENT_CHECKSUM,CHECKSUM_ALGORITHM) select CONTAINER_ID,DOCUMENT_ID,CONTENT_SIZE,CONTENT_CHECKSUM,CHECKSUM_ALGORITHM from TMP_CONTAINER_DRAFT_DOCUMENT;

drop table DOCUMENT_VERSION;
create table DOCUMENT_VERSION(DOCUMENT_ID bigint not null,DOCUMENT_VERSION_ID bigint not null,CONTENT_SIZE bigint not null,CONTENT_CHECKSUM varchar(256) not null,CHECKSUM_ALGORITHM varchar(16) not null,primary key(DOCUMENT_ID, DOCUMENT_VERSION_ID),foreign key(DOCUMENT_ID, DOCUMENT_VERSION_ID) references ARTIFACT_VERSION(ARTIFACT_ID, ARTIFACT_VERSION_ID));
insert into DOCUMENT_VERSION(DOCUMENT_ID,DOCUMENT_VERSION_ID,CONTENT_SIZE,CONTENT_CHECKSUM,CHECKSUM_ALGORITHM) select DOCUMENT_ID,DOCUMENT_VERSION_ID,CONTENT_SIZE,CONTENT_CHECKSUM,CHECKSUM_ALGORITHM from TMP_DOCUMENT_VERSION;

-- drop temp tables
drop table TMP_CONTAINER_DRAFT_DOCUMENT;
drop table TMP_DOCUMENT_VERSION;

-- update release
update META_DATA set META_DATA_VALUE = 'v1_0-20070919-1200' where META_DATA_KEY = 'thinkparity.release-name';
update MIGRATOR set RELEASE_NAME = 'v1_0-20070919-1200';
