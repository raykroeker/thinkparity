
-- increase username size to fix within name/organization/suffix constraints
alter table TPSD_USERNAME_RESERVATION alter column USERNAME set data type varchar(136);
alter table TPSD_USER alter column USERNAME set data type varchar(136);
alter table TPSD_PAYMENT_PLAN alter column PLAN_NAME set data type varchar(160);

-- update release
insert into TPSD_PRODUCT_RELEASE (PRODUCT_ID,RELEASE_NAME,RELEASE_OS,RELEASE_DATE) select PRODUCT_ID,'20071231','LINUX',current_timestamp from TPSD_PRODUCT where PRODUCT_NAME='DesdemonaProduct';

