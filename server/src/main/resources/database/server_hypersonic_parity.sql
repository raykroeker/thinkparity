create table PARITY_USER(
    USER_ID bigint generated by default as identity (start with 7000),
    USERNAME varchar(32) not null,
    SECURITY_QUESTION varchar(64) not null,
    SECURITY_ANSWER varchar(64) not null,
    DISABLED char not null,
    TOKEN varchar(32) null,
    VCARD varchar(1024) not null,
    primary key(USER_ID),
    unique(USERNAME),
    foreign key(USERNAME) references jiveUser(USERNAME)
);
create table USER_BACKUP_REL(
    USER_ID bigint not null,
    BACKUP_ID bigint not null,
    primary key(USER_ID,BACKUP_ID),
    foreign key(USER_ID) references PARITY_USER(USER_ID),
    foreign key(BACKUP_ID) references PARITY_USER(USER_ID)
);
create table USER_EMAIL(
    USER_ID bigint not null,
    EMAIL varchar(512) not null,
    VERIFIED char not null,
    VERIFICATION_KEY varchar(512) null,
    primary key(EMAIL),
    foreign key(USER_ID) references PARITY_USER(USER_ID)
);
create table USER_EVENT_QUEUE(
    USER_ID bigint not null,
    EVENT_ID varchar(32) not null,
    EVENT_DATE timestamp not null,
    EVENT_PRIORITY smallint not null,
    EVENT_XML varchar(2048) not null,
    primary key(EVENT_ID),
    foreign key(USER_ID) references PARITY_USER(USER_ID)
);
create index USER_EVENT_QUEUE_IX_0 on USER_EVENT_QUEUE(EVENT_PRIORITY);
create index USER_EVENT_QUEUE_IX_1 on USER_EVENT_QUEUE(EVENT_DATE);
create table USER_INVITATION(
    INVITATION_FROM bigint not null,
    INVITATION_TO bigint not null,
    CREATED_BY bigint not null,
    CREATED_ON timestamp not null,
    UPDATED_BY bigint not null,
    UPDATED_ON timestamp not null,
    primary key(INVITATION_FROM,INVITATION_TO),
    foreign key(INVITATION_FROM) references PARITY_USER(USER_ID),
    foreign key(INVITATION_TO) references PARITY_USER(USER_ID),
    foreign key(CREATED_BY) references PARITY_USER(USER_ID),
    foreign key(UPDATED_BY) references PARITY_USER(USER_ID)
);
create table USER_EMAIL_INVITATION(
    INVITATION_FROM bigint not null,
    INVITATION_TO varchar(512) not null,
    CREATED_BY bigint not null,
    CREATED_ON timestamp not null,
    UPDATED_BY bigint not null,
    UPDATED_ON timestamp not null,
    primary key(INVITATION_FROM,INVITATION_TO),
    foreign key(INVITATION_FROM) references PARITY_USER(USER_ID),
    foreign key(CREATED_BY) references PARITY_USER(USER_ID),
    foreign key(UPDATED_BY) references PARITY_USER(USER_ID)
);
create table USER_CONTACT(
	USER_ID bigint not null,
	CONTACT_ID bigint not null,
    CREATED_BY bigint not null,
    CREATED_ON timestamp not null,
    UPDATED_BY bigint not null,
    UPDATED_ON timestamp not null,
	primary key(USER_ID,CONTACT_ID),
    foreign key(USER_ID) references PARITY_USER(USER_ID),
    foreign key(CREATED_BY) references PARITY_USER(USER_ID),
    foreign key(UPDATED_BY) references PARITY_USER(USER_ID)
);
create index CONTACT_IX_0 on USER_CONTACT(USER_ID);
create index CONTACT_IX_1 on USER_CONTACT(CONTACT_ID);

create table ARTIFACT(
    ARTIFACT_ID bigint generated by default as identity (start with 2000),
    ARTIFACT_UNIQUE_ID varchar(256) not null,
    ARTIFACT_DRAFT_OWNER bigint not null,
    CREATED_BY bigint not null,
    CREATED_ON timestamp not null,
    UPDATED_BY bigint not null,
    UPDATED_ON timestamp not null,
    primary key(ARTIFACT_ID),
    unique(ARTIFACT_UNIQUE_ID),
    foreign key(ARTIFACT_DRAFT_OWNER) references PARITY_USER(USER_ID),
    foreign key(CREATED_BY) references PARITY_USER(USER_ID),
    foreign key(UPDATED_BY) references PARITY_USER(USER_ID)
);
create index ARTIFACT_IX_0 on ARTIFACT(ARTIFACT_UNIQUE_ID);
create index ARTIFACT_IX_1 on ARTIFACT(ARTIFACT_DRAFT_OWNER);
create table ARTIFACT_TEAM_REL (
    ARTIFACT_ID bigint not null,
    USER_ID bigint not null,
    primary key (ARTIFACT_ID, USER_ID),
    foreign key (ARTIFACT_ID) references ARTIFACT(ARTIFACT_ID),
    foreign key (USER_ID) references PARITY_USER(USER_ID)
);
create index ARTIFACT_TEAM_REL_IX_0 on ARTIFACT_TEAM_REL(ARTIFACT_ID);
create index ARTIFACT_TEAM_REL_IX_1 on ARTIFACT_TEAM_REL(USER_ID);

create table PRODUCT(
    PRODUCT_ID bigint generated by default as identity (start with 1000),
    PRODUCT_NAME varchar(64) not null,
    primary key(PRODUCT_ID),
    unique(PRODUCT_NAME)
);
create table PRODUCT_FEATURE(
    PRODUCT_ID bigint not null,
    FEATURE_ID bigint generated by default as identity (start with 5000),
    FEATURE_NAME varchar(16) not null,
    primary key(FEATURE_ID),
    unique(PRODUCT_ID,FEATURE_NAME),
    foreign key(PRODUCT_ID) references PRODUCT(PRODUCT_ID)
);
create table PRODUCT_RELEASE(
	PRODUCT_ID bigint not null,
	RELEASE_ID bigint generated by default as identity (start with 3000),
    RELEASE_NAME varchar(64) not null,
    RELEASE_OS varchar(32) not null,
    RELEASE_DATE timestamp not null,
    primary key(RELEASE_ID),
    unique(PRODUCT_ID,RELEASE_NAME,RELEASE_OS),
    foreign key(PRODUCT_ID) references PRODUCT(PRODUCT_ID)
);
create table RESOURCE(
	RESOURCE_ID bigint generated by default as identity (start with 4000),
    RESOURCE_NAME varchar(64) not null,
    RESOURCE_VERSION varchar(16) not null,
    RESOURCE_CHECKSUM varchar(256) not null,
    RESOURCE_SIZE bigint not null,
	RESOURCE longvarbinary not null,
    primary key(RESOURCE_ID),
    unique(RESOURCE_NAME,RESOURCE_VERSION,RESOURCE_CHECKSUM)
);
create table RESOURCE_OS(
	RESOURCE_ID bigint not null,
	RESOURCE_OS varchar(32) not null,
	primary key(RESOURCE_ID,RESOURCE_OS),
	foreign key(RESOURCE_ID) references RESOURCE(RESOURCE_ID)
);
create table PRODUCT_RELEASE_RESOURCE_REL(
    RELEASE_ID bigint not null,
    RESOURCE_ID bigint not null,
    RESOURCE_PATH varchar(256) not null,
    primary key(RELEASE_ID,RESOURCE_ID),
    foreign key(RELEASE_ID) references PRODUCT_RELEASE(RELEASE_ID),
    foreign key(RESOURCE_ID) references RESOURCE(RESOURCE_ID)
);
create table USER_FEATURE_REL(
    USER_ID bigint not null,
    FEATURE_ID bigint not null,
    primary key(USER_ID,FEATURE_ID),
    foreign key(USER_ID) references PARITY_USER(USER_ID),
    foreign key(FEATURE_ID) references PRODUCT_FEATURE(FEATURE_ID)
);
