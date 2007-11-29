
-- update release
insert into TPSD_PRODUCT_RELEASE (PRODUCT_ID,RELEASE_NAME,RELEASE_OS,RELEASE_DATE) select PRODUCT_ID,'20071204','LINUX',current_timestamp from TPSD_PRODUCT where PRODUCT_NAME='DesdemonaProduct';

