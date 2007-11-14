
-- update release
insert into TPSD_PRODUCT_RELEASE (PRODUCT_ID,RELEASE_NAME,RELEASE_OS,RELEASE_DATE) select PRODUCT_ID,null,'LINUX',current_timestamp from TPSD_PRODUCT where PRODUCT_NAME='DesdemonaProduct';

create table TPSD_PRODUCT_CFG(
    PRODUCT_ID bigint not null,
    CFG_ID smallint generated always as identity(start with 400),
    CFG_KEY varchar(64) not null,
    CFG_VALUE varchar(128),
    primary key(CFG_ID),
    unique(CFG_KEY),
    foreign key(PRODUCT_ID) references TPSD_PRODUCT(PRODUCT_ID)
);
create table TPSD_USER_PRODUCT_CFG(
    USER_ID bigint not null,
    CFG_ID smallint not null,
    CFG_VALUE varchar(128),
    primary key(USER_ID,CFG_ID),
    foreign key(USER_ID) references TPSD_USER(USER_ID),
    foreign key(CFG_ID) references TPSD_PRODUCT_CFG(CFG_ID)
);
insert into TPSD_PRODUCT_CFG (PRODUCT_ID,CFG_KEY,CFG_VALUE) select PRODUCT_ID,'com.thinkparity.session.reaper.timeout','14400000' from TPSD_PRODUCT where PRODUCT_NAME='OpheliaProduct';
insert into TPSD_PRODUCT_CFG (PRODUCT_ID,CFG_KEY,CFG_VALUE) select PRODUCT_ID,'com.thinkparity.session.reaper.firstexecutiondelay','14400000' from TPSD_PRODUCT where PRODUCT_NAME='OpheliaProduct';
insert into TPSD_PRODUCT_CFG (PRODUCT_ID,CFG_KEY,CFG_VALUE) select PRODUCT_ID,'com.thinkparity.session.reaper.recurringexecutionperiod','14400000' from TPSD_PRODUCT where PRODUCT_NAME='OpheliaProduct';
