create table TPSD_EMAIL_RESERVATION(
    EMAIL varchar(512) not null,
    TOKEN varchar(64) not null,
    EXPIRES_ON timestamp not null,
    CREATED_ON timestamp not null,
    primary key (EMAIL),
    unique (TOKEN)
);
create table TPSD_EMAIL(
    EMAIL_ID bigint generated always as identity(start with 5000),
    EMAIL varchar(512) not null,
    primary key(EMAIL_ID),
    unique(EMAIL)
);

create table TPSD_USERNAME_RESERVATION(
    USERNAME varchar(32) not null,
    TOKEN varchar(64) not null,
    EXPIRES_ON timestamp not null,
    CREATED_ON timestamp not null,
    primary key (USERNAME),
    unique (TOKEN)
);
create table TPSD_USER(
    USER_ID bigint generated always as identity(start with 7000),
    USERNAME varchar(32) not null,
    PASSWORD varchar(32) not null,
    SECURITY_QUESTION varchar(64) not null,
    SECURITY_ANSWER varchar(64) not null,
    DISABLED char not null,
    TOKEN varchar(64),
    VCARD clob not null,
    primary key(USER_ID),
    unique(USERNAME)
);
create table TPSD_USER_EMAIL(
    USER_ID bigint not null,
    EMAIL_ID bigint not null,
    VERIFIED char not null,
    VERIFICATION_KEY varchar(512),
    primary key(USER_ID,EMAIL_ID),
    foreign key(USER_ID) references TPSD_USER(USER_ID),
    foreign key(EMAIL_ID) references TPSD_EMAIL(EMAIL_ID)
);
create table TPSD_USER_EVENT_QUEUE(
    USER_ID bigint not null,
    EVENT_ID varchar(32) not null,
    EVENT_DATE timestamp not null,
    EVENT_PRIORITY smallint not null,
    EVENT_XML clob not null,
    primary key(EVENT_ID),
    foreign key(USER_ID) references TPSD_USER(USER_ID)
);
create index TPSD_USER_EVENT_QUEUE_IX_0 on TPSD_USER_EVENT_QUEUE(EVENT_PRIORITY);
create index TPSD_USER_EVENT_QUEUE_IX_1 on TPSD_USER_EVENT_QUEUE(EVENT_DATE);
create table TPSD_USER_TEMPORARY_CREDENTIAL(
    USER_ID bigint not null,
    TOKEN varchar(64) not null,
    EXPIRES_ON timestamp not null,
    CREATED_ON timestamp not null,
    primary key (USER_ID),
    unique (TOKEN)
);

create table TPSD_CONTACT(
    USER_ID bigint not null,
    CONTACT_ID bigint not null,
    CREATED_BY bigint not null,
    CREATED_ON timestamp not null,
    UPDATED_BY bigint not null,
    UPDATED_ON timestamp not null,
    primary key(USER_ID,CONTACT_ID),
    foreign key(USER_ID) references TPSD_USER(USER_ID),
    foreign key(CREATED_BY) references TPSD_USER(USER_ID),
    foreign key(UPDATED_BY) references TPSD_USER(USER_ID)
);

create table TPSD_CONTACT_INVITATION(
    CONTACT_INVITATION_ID bigint generated always as identity(start with 8000),
    CREATED_BY bigint not null,
    CREATED_ON timestamp not null,
    primary key(CONTACT_INVITATION_ID),
    foreign key(CREATED_BY) references TPSD_USER(USER_ID)
);
create table TPSD_CONTACT_INVITATION_ATTACHMENT(
    CONTACT_INVITATION_ID bigint not null,
    ATTACHMENT_REFERENCE_TYPE_ID smallint not null,
    ATTACHMENT_REFERENCE_ID varchar(264) not null,
    primary key(CONTACT_INVITATION_ID,ATTACHMENT_REFERENCE_TYPE_ID,ATTACHMENT_REFERENCE_ID),
    foreign key(CONTACT_INVITATION_ID) references TPSD_CONTACT_INVITATION(CONTACT_INVITATION_ID)
);
create index TPSD_CONTACT_INVITATION_ATTACHMENT_XI_0 on TPSD_CONTACT_INVITATION_ATTACHMENT(ATTACHMENT_REFERENCE_TYPE_ID);
create index TPSD_CONTACT_INVITATION_ATTACHMENT_XI_1 on TPSD_CONTACT_INVITATION_ATTACHMENT(ATTACHMENT_REFERENCE_ID);
create table TPSD_CONTACT_INVITATION_INCOMING_EMAIL(
    CONTACT_INVITATION_ID bigint not null,
    USER_ID bigint not null,
    EMAIL_ID bigint not null,
    EXTENDED_BY_USER_ID bigint not null,
    primary key(CONTACT_INVITATION_ID),
    unique(USER_ID,EMAIL_ID,EXTENDED_BY_USER_ID),
    foreign key(CONTACT_INVITATION_ID) references TPSD_CONTACT_INVITATION(CONTACT_INVITATION_ID),
    foreign key(EMAIL_ID) references TPSD_EMAIL(EMAIL_ID),
    foreign key(USER_ID) references TPSD_USER(USER_ID),
    foreign key(EXTENDED_BY_USER_ID) references TPSD_USER(USER_ID)
);
create table TPSD_CONTACT_INVITATION_INCOMING_USER(
    CONTACT_INVITATION_ID bigint not null,
    USER_ID bigint not null,
    EXTENDED_BY_USER_ID bigint not null,
    primary key(CONTACT_INVITATION_ID),
    unique(USER_ID,EXTENDED_BY_USER_ID),
    foreign key(CONTACT_INVITATION_ID) references TPSD_CONTACT_INVITATION(CONTACT_INVITATION_ID),
    foreign key(USER_ID) references TPSD_USER(USER_ID),
    foreign key(EXTENDED_BY_USER_ID) references TPSD_USER(USER_ID)
);
create table TPSD_CONTACT_INVITATION_OUTGOING_EMAIL(
    CONTACT_INVITATION_ID bigint not null,
    USER_ID bigint not null,
    INVITATION_EMAIL_ID bigint not null,
    primary key(CONTACT_INVITATION_ID),
    unique(USER_ID,INVITATION_EMAIL_ID),
    foreign key(CONTACT_INVITATION_ID) references TPSD_CONTACT_INVITATION(CONTACT_INVITATION_ID),
    foreign key(USER_ID) references TPSD_USER(USER_ID),
    foreign key(INVITATION_EMAIL_ID) references TPSD_EMAIL(EMAIL_ID)
);
create table TPSD_CONTACT_INVITATION_OUTGOING_USER(
    CONTACT_INVITATION_ID bigint not null,
    USER_ID bigint not null,
    INVITATION_USER_ID bigint not null,
    primary key(CONTACT_INVITATION_ID),
    unique(USER_ID,INVITATION_USER_ID),
    foreign key(CONTACT_INVITATION_ID) references TPSD_CONTACT_INVITATION(CONTACT_INVITATION_ID),
    foreign key(USER_ID) references TPSD_USER(USER_ID),
    foreign key(INVITATION_USER_ID) references TPSD_USER(USER_ID)
);

create table TPSD_ARTIFACT(
    ARTIFACT_ID bigint generated always as identity(start with 2000),
    ARTIFACT_UNIQUE_ID varchar(256) not null,
    ARTIFACT_DRAFT_OWNER bigint not null,
    ARTIFACT_LATEST_VERSION_ID bigint not null,
    CREATED_BY bigint not null,
    CREATED_ON timestamp not null,
    UPDATED_BY bigint not null,
    UPDATED_ON timestamp not null,
    primary key(ARTIFACT_ID),
    unique(ARTIFACT_UNIQUE_ID),
    foreign key(ARTIFACT_DRAFT_OWNER) references TPSD_USER(USER_ID),
    foreign key(CREATED_BY) references TPSD_USER(USER_ID),
    foreign key(UPDATED_BY) references TPSD_USER(USER_ID)
);
create table TPSD_ARTIFACT_TEAM_REL(
    ARTIFACT_ID bigint not null,
    USER_ID bigint not null,
    primary key (ARTIFACT_ID, USER_ID),
    foreign key (ARTIFACT_ID) references TPSD_ARTIFACT(ARTIFACT_ID),
    foreign key (USER_ID) references TPSD_USER(USER_ID)
);

create table TPSD_PRODUCT(
    PRODUCT_ID bigint generated always as identity(start with 1000),
    PRODUCT_NAME varchar(64) not null,
    primary key(PRODUCT_ID),
    unique(PRODUCT_NAME)
);
create table TPSD_PRODUCT_FEATURE(
    PRODUCT_ID bigint not null,
    FEATURE_ID bigint generated always as identity(start with 6000),
    FEATURE_NAME varchar(16) not null,
    primary key(FEATURE_ID),
    unique(PRODUCT_ID,FEATURE_NAME),
    foreign key(PRODUCT_ID) references TPSD_PRODUCT(PRODUCT_ID)
);
create table TPSD_PRODUCT_RELEASE(
    PRODUCT_ID bigint not null,
    RELEASE_ID bigint generated always as identity(start with 3000),
    RELEASE_NAME varchar(64) not null,
    RELEASE_OS varchar(32) not null,
    RELEASE_DATE timestamp not null,
    primary key(RELEASE_ID),
    unique(PRODUCT_ID,RELEASE_NAME,RELEASE_OS),
    unique(PRODUCT_ID,RELEASE_OS,RELEASE_DATE),
    foreign key(PRODUCT_ID) references TPSD_PRODUCT(PRODUCT_ID)
);
create index TPSD_PRODUCT_RELEASE_IX_0 on TPSD_PRODUCT_RELEASE(RELEASE_DATE);

create table TPSD_PRODUCT_RELEASE_ERROR(
    ERROR_ID bigint generated always as identity(start with 9000),
    RELEASE_ID bigint not null,
    USER_ID bigint not null,
    ERROR_DATE timestamp not null,
    ERROR_XML clob not null,
    primary key(ERROR_ID),
    foreign key(USER_ID) references TPSD_USER(USER_ID),
    foreign key(RELEASE_ID) references TPSD_PRODUCT_RELEASE(RELEASE_ID)
);
create table TPSD_PRODUCT_RELEASE_RESOURCE(
    RESOURCE_ID bigint generated always as identity(start with 4000),
    RESOURCE_CHECKSUM varchar(256) not null,
    RESOURCE_CHECKSUM_ALGORITHM varchar(16) not null,
    RESOURCE_SIZE bigint not null,
    RESOURCE blob not null,
    primary key(RESOURCE_ID)
);
create table TPSD_PRODUCT_RELEASE_RESOURCE_REL(
    RELEASE_ID bigint not null,
    RESOURCE_ID bigint not null,
    RESOURCE_PATH varchar(256) not null,
    primary key(RELEASE_ID,RESOURCE_ID),
    unique(RELEASE_ID,RESOURCE_PATH),
    foreign key(RELEASE_ID) references TPSD_PRODUCT_RELEASE(RELEASE_ID),
    foreign key(RESOURCE_ID) references TPSD_PRODUCT_RELEASE_RESOURCE(RESOURCE_ID)
);
create table TPSD_USER_FEATURE_REL(
    USER_ID bigint not null,
    FEATURE_ID bigint not null,
    primary key(USER_ID,FEATURE_ID),
    foreign key(USER_ID) references TPSD_USER(USER_ID),
    foreign key(FEATURE_ID) references TPSD_PRODUCT_FEATURE(FEATURE_ID)
);

create table TPSD_BACKUP_USER_ARCHIVE(
    USER_ID bigint not null,
    ARTIFACT_ID bigint not null,
    primary key(USER_ID,ARTIFACT_ID),
    foreign key(USER_ID) references TPSD_USER(USER_ID),
    foreign key(ARTIFACT_ID) references TPSD_ARTIFACT(ARTIFACT_ID)
);