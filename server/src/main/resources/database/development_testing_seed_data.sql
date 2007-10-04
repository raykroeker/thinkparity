insert into TPSD_NODE(NODE_USERNAME,NODE_PASSWORD) values('tps-yxx-0.thinkparity.org-0','9hq1sKw+RLjLc1UdtEQRcA==');

insert into TPSD_PAYMENT_CURRENCY(CURRENCY_NAME) values('USD');

insert into TPSD_PAYMENT_PROVIDER(PROVIDER_NAME,PROVIDER_CLASS) values('Moneris','com.thinkparity.payment.moneris.PaymentProvider');
insert into TPSD_PAYMENT_PROVIDER_CONFIGURATION(PROVIDER_ID,CONFIGURATION_KEY,CONFIGURATION_VALUE)
    select PROVIDER_ID,'com.thinkparity.payment.moneris.apitoken','yesguy' from TPSD_PAYMENT_PROVIDER where PROVIDER_NAME='Moneris';
insert into TPSD_PAYMENT_PROVIDER_CONFIGURATION(PROVIDER_ID,CONFIGURATION_KEY,CONFIGURATION_VALUE)
    select PROVIDER_ID,'com.thinkparity.payment.moneris.encryption','7' from TPSD_PAYMENT_PROVIDER where PROVIDER_NAME='Moneris';
insert into TPSD_PAYMENT_PROVIDER_CONFIGURATION(PROVIDER_ID,CONFIGURATION_KEY,CONFIGURATION_VALUE)
    select PROVIDER_ID,'com.thinkparity.payment.moneris.host','esqa.moneris.com' from TPSD_PAYMENT_PROVIDER where PROVIDER_NAME='Moneris';
insert into TPSD_PAYMENT_PROVIDER_CONFIGURATION(PROVIDER_ID,CONFIGURATION_KEY,CONFIGURATION_VALUE)
    select PROVIDER_ID,'com.thinkparity.payment.moneris.storeid','store3' from TPSD_PAYMENT_PROVIDER where PROVIDER_NAME='Moneris';
insert into TPSD_PAYMENT_PLAN(PLAN_NAME,PLAN_UNIQUE_ID,PLAN_BILLABLE,PLAN_CURRENCY,PLAN_OWNER,PLAN_ARREARS,INVOICE_PERIOD,INVOICE_PERIOD_OFFSET,PLAN_PASSWORD) select 'thinkParity Solutions Inc.','0edf761f-bbae-4ea0-b856-bde1f68820e5','0',CURRENCY_ID,USER_ID,'0','MONTH',1,'password' from TPSD_PAYMENT_CURRENCY,TPSD_USER where CURRENCY_NAME='USD' and USERNAME='thinkparity';
insert into TPSD_PAYMENT_PLAN_LOCK(PLAN_ID) select PLAN_ID from TPSD_PAYMENT_PLAN where PLAN_UNIQUE_ID='0edf761f-bbae-4ea0-b856-bde1f68820e5';

insert into TPSD_PRODUCT(PRODUCT_NAME) values('OpheliaProduct');
insert into TPSD_PRODUCT(PRODUCT_NAME) values('DesdemonaProduct');
insert into TPSD_PRODUCT_FEATURE(PRODUCT_ID,FEATURE_NAME) select PRODUCT_ID,'CORE' from TPSD_PRODUCT where PRODUCT_NAME='OpheliaProduct';
insert into TPSD_PRODUCT_FEATURE(PRODUCT_ID,FEATURE_NAME) select PRODUCT_ID,'BACKUP' from TPSD_PRODUCT where PRODUCT_NAME='OpheliaProduct';
insert into TPSD_PRODUCT_FEATURE_FEE(FEATURE_ID,CURRENCY_ID,FEE_DESCRIPTION,FEE_PERIOD,FEE_AMOUNT) select FEATURE_ID,CURRENCY_ID,'thinkParity:  {0}','MONTH',8400 from TPSD_PRODUCT_FEATURE,TPSD_PAYMENT_CURRENCY where FEATURE_NAME='CORE' and CURRENCY_NAME='USD';

insert into TPSD_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,ACTIVE,DISABLED,VCARD,CREATED_ON)
    values('thinkparity','DMnBhTS/KlVqMZTwRqwTNw==','What is the city where you were born?','p0B7MKs7hs21lMFBgHe/+g==','1','0','iHt2ZotgbGgfDUZmdRUWf2FWWSzqlCzVgQxyhqrBElNEaamLxMcfRX+Si3bcB/WxzZsAmPDs3j3Pmg21I07RI9ubyF63bmfCQM2bt1ThCX6qNR2T713kQqFDXx4GaMcUXux7ARX4FEAcs2XfkR4zqro7orioIW8NRIJayAxL+lPY/htSHicuiOjMxTOnXBaJLJVAPQGRkP1Jb7YaW0nzFXSeAjnSao2T3LELL5ZTNaOLWb6UN69yyeWyCUR8657ehR8bWKlPIqmimRhNklWskK81Tjwlu3qGkknJ/VHPPqR6HvQitZNpbxRbwFQDWvel1pAOd9822NjbWN6JjqJzfRi+vy6yqY3LVZwy2RGZ9DWuzMeaSDVu9hszdH1NfLZcqOpNQFALTpUYPYKb8PEdikr2Q2iebbCJCcVXdlFQlLGye33qZubt7RLyBYqd6ZEB',current_timestamp);
insert into TPSD_EMAIL(EMAIL)
    values('thinkparity@thinkparity.com');
insert into TPSD_USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    select USER_ID,EMAIL_ID,'1' from TPSD_USER,TPSD_EMAIL where USERNAME='thinkparity' and EMAIL='thinkparity@thinkparity.com';

insert into TPSD_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,ACTIVE,DISABLED,VCARD,CREATED_ON)
    values('thinkparity-backup','DMnBhTS/KlVqMZTwRqwTNw==','What is the city where you were born?','p0B7MKs7hs21lMFBgHe/+g==','1','0','iHt2ZotgbGgfDUZmdRUWf2FWWSzqlCzVgQxyhqrBElNEaamLxMcfRX+Si3bcB/WxzZsAmPDs3j3Pmg21I07RI9ubyF63bmfCQM2bt1ThCX6qNR2T713kQqFDXx4GaMcUXux7ARX4FEAcs2XfkR4zqmyt5TQ6aLpbBAtgvyTwEXLE5mfCB3jF86asppABKQvQR2MpIGUOKmmGGXmUAOKWFegfceYI3MUZJRKS2ML2f+xbFsO6DJmQjyIwcUCz4W4p+SxhLOG2BPci7ctnlVEmnjns0bfBV1yYeGpK2YAjKsr/PpE5V+JwQsPcADt2lHs1qf45xWhrxBEfQfM3s7GMtW0pj7n8EodmbTECmkAt/MKyXqKw6A/yeWeHwjR0y+Z+2dB9zozhmURhQYugiEoEBSVFLS4ZJIadb+K5VBw3WaWcLYXFssyDTWuWjmG0xQu6',current_timestamp);
insert into TPSD_EMAIL(EMAIL)
    values('thinkparity+backup@thinkparity.com');
insert into TPSD_USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    select USER_ID,EMAIL_ID,'1' from TPSD_USER,TPSD_EMAIL where USERNAME='thinkparity-backup' and EMAIL='thinkparity+backup@thinkparity.com';

insert into TPSD_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,ACTIVE,DISABLED,VCARD,CREATED_ON)
    values('thinkparity-support','DMnBhTS/KlVqMZTwRqwTNw==','What is the city where you were born?','p0B7MKs7hs21lMFBgHe/+g==','1','0','iHt2ZotgbGgfDUZmdRUWf2FWWSzqlCzVgQxyhqrBElNEaamLxMcfRX+Si3bcB/WxzZsAmPDs3j3Pmg21I07RI9ubyF63bmfCQM2bt1ThCX6qNR2T713kQqFDXx4GaMcUXux7ARX4FEAcs2XfkR4zqradpTtqKvDMRniVx+0j+ctO9M3mmmsI8vGdGBsvDBi4mPQQztzsA+1UsOJOvRsfsvmKllO8YO5+AFjdlb/YPGGkKcDT0wg5ZeRGXVobgpjGMfPd1+vvi/NgzqjouXb47dxVTfEBM8ZLXvaGJ0i+/M/gYcaOPmmcPYmkN+jA6eQa15SWKRHJcGc6KXYhUar2ap8kn9822yeLei49Thl1XxBzTHw+cA6UiD4q3ZQ6HV22hn4VJ+7pw3lX2bdyNIyXtBTCxlHBowne636tiv4mVItlilpH2Fq0FTZO1/PiE4DW',current_timestamp);
insert into TPSD_EMAIL(EMAIL)
    values('thinkparity+support@thinkparity.com');
insert into TPSD_USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    select USER_ID,EMAIL_ID,'1' from TPSD_USER,TPSD_EMAIL where USERNAME='thinkparity-support' and EMAIL='thinkparity+support@thinkparity.com';
insert into TPSD_USER_FEATURE_REL(USER_ID,FEATURE_ID)
    select USER_ID,FEATURE_ID from TPSD_USER,TPSD_PRODUCT P inner join TPSD_PRODUCT_FEATURE PF on PF.PRODUCT_ID=P.PRODUCT_ID where USERNAME='thinkparity-support' and P.PRODUCT_NAME='OpheliaProduct' and PF.FEATURE_NAME='CORE';
insert into TPSD_USER_FEATURE_REL(USER_ID,FEATURE_ID)
    select USER_ID,FEATURE_ID from TPSD_USER,TPSD_PRODUCT P inner join TPSD_PRODUCT_FEATURE PF on PF.PRODUCT_ID=P.PRODUCT_ID where USERNAME='thinkparity-support' and P.PRODUCT_NAME='OpheliaProduct' and PF.FEATURE_NAME='BACKUP';
