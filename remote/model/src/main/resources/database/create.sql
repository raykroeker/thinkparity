create table TPSD_NODE(
    NODE_ID smallint generated always as identity(start with 100),
    NODE_USERNAME varchar(32) not null,
    NODE_PASSWORD varchar(64) not null,
    primary key(NODE_ID),
    unique(NODE_USERNAME)
);
create table TPSD_NODE_SESSION(
    NODE_ID smallint not null,
    TOKEN varchar(64) not null,
    CREATED_ON timestamp not null,
    primary key(NODE_ID),
    unique(TOKEN),
    foreign key(NODE_ID) references TPSD_NODE(NODE_ID)
);
create table TPSD_EMAIL_RESERVATION(
    EMAIL varchar(512) not null,
    TOKEN varchar(64) not null,
    EXPIRES_ON timestamp not null,
    CREATED_ON timestamp not null,
    primary key (EMAIL),
    unique(TOKEN)
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
    unique(TOKEN)
);
create table TPSD_USER(
    USER_ID bigint generated always as identity(start with 7000),
    USERNAME varchar(32) not null,
    PASSWORD varchar(64) not null,
    SECURITY_QUESTION varchar(64) not null,
    SECURITY_ANSWER varchar(128) not null,
    DISABLED char not null,
    ACTIVE char not null,
    TOKEN varchar(64),
    VCARD clob not null,
    CREATED_ON timestamp not null,
    primary key(USER_ID),
    unique(USERNAME)
);
create index TPSD_USER_IX_0 on TPSD_USER(PASSWORD);
create index TPSD_USER_IX_1 on TPSD_USER(SECURITY_QUESTION);
create index TPSD_USER_IX_2 on TPSD_USER(SECURITY_ANSWER);
create index TPSD_USER_IX_3 on TPSD_USER(DISABLED);
create index TPSD_USER_IX_4 on TPSD_USER(ACTIVE);
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
    EVENT_FILTER character not null,
    EVENT_XML clob not null,
    primary key(EVENT_ID),
    foreign key(USER_ID) references TPSD_USER(USER_ID)
);
create index TPSD_USER_EVENT_QUEUE_IX_0 on TPSD_USER_EVENT_QUEUE(EVENT_PRIORITY);
create index TPSD_USER_EVENT_QUEUE_IX_1 on TPSD_USER_EVENT_QUEUE(EVENT_DATE);
create table TPSD_AUDIT_USER_EVENT_QUEUE(
    USER_ID bigint not null,
    EVENT_ID varchar(32) not null,
    EVENT_DATE timestamp not null,
    EVENT_PRIORITY smallint not null,
    EVENT_FILTER character not null,
    EVENT_XML clob not null,
    primary key(EVENT_ID),
    foreign key(USER_ID) references TPSD_USER(USER_ID)
);
create index TPSD_AUDIT_USER_EVENT_QUEUE_IX_0 on TPSD_AUDIT_USER_EVENT_QUEUE(EVENT_PRIORITY);
create index TPSD_AUDIT_USER_EVENT_QUEUE_IX_1 on TPSD_AUDIT_USER_EVENT_QUEUE(EVENT_DATE);
create table TPSD_USER_TEMPORARY_CREDENTIAL(
    USER_ID bigint not null,
    TOKEN varchar(64) not null,
    EXPIRES_ON timestamp not null,
    CREATED_ON timestamp not null,
    primary key (USER_ID),
    unique(TOKEN)
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
create table TPSD_SESSION(
    USER_ID bigint not null,
    TOKEN varchar(64) not null,
    CREATED_ON timestamp not null,
    EXPIRES_ON timestamp not null,
    primary key(USER_ID),
    unique(TOKEN),
    foreign key(USER_ID) references TPSD_USER(USER_ID)
);
create table TPSD_AUDIT_SESSION(
    USER_ID bigint not null,
    CREATED_ON timestamp not null,
    DELETED_ON timestamp,
    primary key(USER_ID,CREATED_ON,DELETED_ON),
    foreign key(USER_ID) references TPSD_USER(USER_ID)
);
create table TPSD_NOTIFICATION_SESSION(
    USER_ID bigint not null,
    TOKEN varchar(64) not null,
    CREATED_ON timestamp not null,
    primary key(USER_ID),
    unique(TOKEN),
    foreign key(USER_ID) references TPSD_USER(USER_ID)
);

create table TPSD_ARTIFACT(
    ARTIFACT_ID bigint generated always as identity(start with 2000),
    ARTIFACT_TYPE_ID smallint not null,
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
create table TPSD_ARTIFACT_VERSION(
    ARTIFACT_ID bigint not null,
    ARTIFACT_VERSION_ID bigint not null,
    ARTIFACT_UNIQUE_ID varchar(256) not null,
    primary key(ARTIFACT_ID, ARTIFACT_VERSION_ID),
    foreign key(ARTIFACT_ID) references TPSD_ARTIFACT(ARTIFACT_ID),
    foreign key(ARTIFACT_UNIQUE_ID) references TPSD_ARTIFACT(ARTIFACT_UNIQUE_ID)
);
create table TPSD_ARTIFACT_VERSION_SECRET(
    ARTIFACT_ID bigint not null,
    ARTIFACT_VERSION_ID bigint not null,
    SECRET blob not null,
    SECRET_ALGORITHM varchar(8) not null,
    SECRET_FORMAT varchar(8) not null,
    primary key(ARTIFACT_ID, ARTIFACT_VERSION_ID),
    foreign key(ARTIFACT_ID, ARTIFACT_VERSION_ID) references TPSD_ARTIFACT_VERSION(ARTIFACT_ID,ARTIFACT_VERSION_ID)
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
create table TPSD_USER_PRODUCT_RELEASE_REL(
    USER_ID bigint not null,
    PRODUCT_ID bigint not null,
    RELEASE_ID bigint not null,
    primary key(USER_ID,PRODUCT_ID,RELEASE_ID),
    foreign key(USER_ID) references TPSD_USER(USER_ID),
    foreign key(PRODUCT_ID) references TPSD_PRODUCT(PRODUCT_ID),
    foreign key(RELEASE_ID) references TPSD_PRODUCT_RELEASE(RELEASE_ID)
);

create table TPSD_PAYMENT_CURRENCY(
    CURRENCY_ID smallint generated always as identity(start with 300),
    CURRENCY_NAME varchar(4) not null,
    primary key(CURRENCY_ID),
    unique(CURRENCY_NAME)
);
create table TPSD_PAYMENT_PROVIDER(
    PROVIDER_ID smallint generated always as identity(start with 200),
    PROVIDER_NAME varchar(8) not null,
    PROVIDER_CLASS varchar(256) not null,
    primary key(PROVIDER_ID),
    unique(PROVIDER_NAME),
    unique(PROVIDER_CLASS)
);
create table TPSD_PAYMENT_PROVIDER_CONFIGURATION(
    PROVIDER_ID smallint not null,
    CONFIGURATION_KEY varchar(64) not null,
    CONFIGURATION_VALUE varchar(256) not null,
    primary key(PROVIDER_ID,CONFIGURATION_KEY)
);
create table TPSD_PAYMENT_INFO(
    PROVIDER_ID smallint not null,
    INFO_ID bigint generated always as identity(start with 12000),
    INFO_CARD_NAME varchar(48) not null,
    INFO_CARD_NUMBER varchar(48) not null,
    INFO_CARD_EXPIRY_MONTH varchar(32) not null,
    INFO_CARD_EXPIRY_YEAR varchar(32) not null,
    primary key(INFO_ID),
    foreign key(PROVIDER_ID) references TPSD_PAYMENT_PROVIDER(PROVIDER_ID)
);
create table TPSD_PAYMENT_PLAN(
    PLAN_ID bigint generated always as identity(start with 2000),
    PLAN_UNIQUE_ID varchar(256) not null,
    PLAN_NAME varchar(64) not null,
    PLAN_CURRENCY smallint not null,
    PLAN_PASSWORD varchar(64),
    PLAN_BILLABLE char not null,
    PLAN_OWNER bigint not null,
    PLAN_ARREARS character not null,
    INVOICE_PERIOD varchar(8) not null,
    INVOICE_PERIOD_OFFSET smallint not null,
    primary key(PLAN_ID),
    unique(PLAN_UNIQUE_ID),
    unique(PLAN_NAME),
    foreign key(PLAN_CURRENCY) references TPSD_PAYMENT_CURRENCY(CURRENCY_ID),
    foreign key(PLAN_OWNER) references TPSD_USER(USER_ID)
);
create table TPSD_PAYMENT_PLAN_LOCK(
    PLAN_ID bigint not null,
    NODE_ID smallint,
    primary key(PLAN_ID),
    foreign key(PLAN_ID) references TPSD_PAYMENT_PLAN(PLAN_ID),
    foreign key(NODE_ID) references TPSD_NODE(NODE_ID)
);
create table TPSD_PAYMENT_PLAN_PAYMENT_INFO(
    PLAN_ID bigint not null,
    INFO_ID bigint not null,
    primary key(PLAN_ID,INFO_ID),
    foreign key(PLAN_ID) references TPSD_PAYMENT_PLAN(PLAN_ID),
    foreign key(INFO_ID) references TPSD_PAYMENT_INFO(INFO_ID)
);
create table TPSD_PAYMENT_PLAN_INVOICE(
    PLAN_ID bigint not null,
    INVOICE_ID bigint generated always as identity(start with 13000),
    INVOICE_NUMBER smallint not null,
    INVOICE_DATE date not null,
    PAYMENT_DATE date,
    primary key(INVOICE_ID),
    unique(PLAN_ID,INVOICE_NUMBER),
    unique(PLAN_ID,INVOICE_DATE),
    foreign key(PLAN_ID) references TPSD_PAYMENT_PLAN(PLAN_ID)
);
create index TPSD_PAYMENT_PLAN_INVOICE_IX_0 on TPSD_PAYMENT_PLAN_INVOICE(INVOICE_DATE);
create table TPSD_PAYMENT_PLAN_INVOICE_ITEM(
    INVOICE_ID bigint not null,
    ITEM_NUMBER smallint not null,
    ITEM_DESCRIPTION varchar(1024) not null,
    ITEM_AMOUNT bigint not null,
    primary key(INVOICE_ID,ITEM_NUMBER),
    foreign key(INVOICE_ID) references TPSD_PAYMENT_PLAN_INVOICE(INVOICE_ID)
);
create table TPSD_PAYMENT_PLAN_INVOICE_LOCK(
    INVOICE_ID bigint not null,
    NODE_ID smallint,
    primary key(INVOICE_ID),
    foreign key(INVOICE_ID) references TPSD_PAYMENT_PLAN_INVOICE(INVOICE_ID),
    foreign key(NODE_ID) references TPSD_NODE(NODE_ID)
);
create table TPSD_PAYMENT_PLAN_INVOICE_TX(
    INVOICE_ID bigint not null,
    TX_ID bigint generated always as identity(start with 15000),
    TX_UNIQUE_ID varchar(256) not null,
    TX_DATE timestamp not null,
    TX_SUCCESS character,
    primary key(TX_ID),
    unique(TX_UNIQUE_ID),
    foreign key(INVOICE_ID) references TPSD_PAYMENT_PLAN_INVOICE(INVOICE_ID)
);
create index TPSD_PAYMENT_PLAN_INVOICE_TX_IX_0 on TPSD_PAYMENT_PLAN_INVOICE_TX(TX_DATE);
create index TPSD_PAYMENT_PLAN_INVOICE_TX_IX_1 on TPSD_PAYMENT_PLAN_INVOICE_TX(TX_SUCCESS);
create table TPSD_PAYMENT_PLAN_INVOICE_TX_ERROR(
    TX_ID bigint not null,
    TX_ERROR clob not null,
    foreign key(TX_ID) references TPSD_PAYMENT_PLAN_INVOICE_TX(TX_ID)
);
create table TPSD_USER_PAYMENT_PLAN(
    USER_ID bigint not null,
    PLAN_ID bigint not null,
    CREATED_ON timestamp not null,
    primary key(USER_ID),
    foreign key(USER_ID) references TPSD_USER(USER_ID),
    foreign key(PLAN_ID) references TPSD_PAYMENT_PLAN(PLAN_ID)
);
create table TPSD_PRODUCT_FEATURE_FEE(
    FEE_ID smallint generated always as identity(start with 200),
    FEATURE_ID bigint not null,
    CURRENCY_ID smallint not null,
    FEE_DESCRIPTION varchar(1024) not null,
    FEE_PERIOD varchar(8),
    FEE_AMOUNT bigint not null,
    primary key(FEE_ID),
    foreign key(FEATURE_ID) references TPSD_PRODUCT_FEATURE(FEATURE_ID),
    foreign key(CURRENCY_ID) references TPSD_PAYMENT_CURRENCY(CURRENCY_ID)
);