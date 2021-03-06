
insert into TPSD_NODE(NODE_USERNAME,NODE_PASSWORD) values('tpsd-localhost-0','9hq1sKw+RLjLc1UdtEQRcA==');

insert into TPSD_PAYMENT_CURRENCY(CURRENCY_NAME) values('CAD');

insert into TPSD_PAYMENT_PROVIDER(PROVIDER_NAME,PROVIDER_CLASS) values('Moneris','com.thinkparity.desdemona.model.profile.payment.MonerisProvider');
insert into TPSD_PAYMENT_PROVIDER_CONFIGURATION(PROVIDER_ID,CONFIGURATION_KEY,CONFIGURATION_VALUE)
	values(100,'com.thinkparity.payment.moneris.apitoken','yesguy');
insert into TPSD_PAYMENT_PROVIDER_CONFIGURATION(PROVIDER_ID,CONFIGURATION_KEY,CONFIGURATION_VALUE)
	values(100,'com.thinkparity.payment.moneris.encryption','7');
insert into TPSD_PAYMENT_PROVIDER_CONFIGURATION(PROVIDER_ID,CONFIGURATION_KEY,CONFIGURATION_VALUE)
	values(100,'com.thinkparity.payment.moneris.host','esqa.moneris.com');
insert into TPSD_PAYMENT_PROVIDER_CONFIGURATION(PROVIDER_ID,CONFIGURATION_KEY,CONFIGURATION_VALUE)
	values(100,'com.thinkparity.payment.moneris.storeid','store3');
insert into TPSD_PAYMENT_PLAN(PLAN_NAME,PLAN_BILLABLE) values('thinkparity.com','0');
insert into TPSD_PAYMENT_PLAN_FILTER(FILTER_TYPE,FILTER_CRITERIA,FILTER_CARDINALITY,FILTER_MATCH,FILTER_EXPRESSION)
    values(0,0,0,0,'raymond+ashaftoe@thinkparity.com');
insert into TPSD_PAYMENT_PLAN_FILTER(FILTER_TYPE,FILTER_CRITERIA,FILTER_CARDINALITY,FILTER_MATCH,FILTER_EXPRESSION)
    values(0,0,0,0,'raymond+rkroeker@thinkparity.com');
insert into TPSD_PAYMENT_PLAN_FILTER(FILTER_TYPE,FILTER_CRITERIA,FILTER_CARDINALITY,FILTER_MATCH,FILTER_EXPRESSION)
    values(0,0,0,0,'raymond+rwaterhouse@thinkparity.com');

insert into TPSD_PRODUCT(PRODUCT_NAME) values('OpheliaProduct');
insert into TPSD_PRODUCT(PRODUCT_NAME) values('DesdemonaProduct');
insert into TPSD_PRODUCT_RELEASE(PRODUCT_ID,RELEASE_NAME,RELEASE_OS,RELEASE_DATE)
    values(1000,'DEVELOPMENT','LINUX',CURRENT_TIMESTAMP);
insert into TPSD_PRODUCT_RELEASE(PRODUCT_ID,RELEASE_NAME,RELEASE_OS,RELEASE_DATE)
    values(1000,'DEVELOPMENT','WINDOWS_XP',CURRENT_TIMESTAMP);
insert into TPSD_PRODUCT_FEATURE(PRODUCT_ID,FEATURE_NAME) values(1000,'CORE');
insert into TPSD_PRODUCT_FEATURE(PRODUCT_ID,FEATURE_NAME) values(1000,'BACKUP');
insert into TPSD_PRODUCT_FEATURE_FEE(FEATURE_ID,CURRENCY_ID,FEE_DESCRIPTION,FEE_PERIOD,FEE_AMOUNT) values(6000,300,'thinkParity Sign-up:  {0}',null,8400);
insert into TPSD_PRODUCT_FEATURE_FEE(FEATURE_ID,CURRENCY_ID,FEE_DESCRIPTION,FEE_PERIOD,FEE_AMOUNT) values(6000,300,'thinkParity Sign-up:  {0}','M',8400);

insert into TPSD_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD,CREATED_ON)
    values('thinkparity','DMnBhTS/KlVqMZTwRqwTNw==','What is the city where you were born?','p0B7MKs7hs21lMFBgHe/+g==','0','iHt2ZotgbGgfDUZmdRUWf2FWWSzqlCzVgQxyhqrBElNEaamLxMcfRX+Si3bcB/WxzZsAmPDs3j3Pmg21I07RI9ubyF63bmfCQM2bt1ThCX6qNR2T713kQqFDXx4GaMcUXux7ARX4FEAcs2XfkR4zqro7orioIW8NRIJayAxL+lPY/htSHicuiOjMxTOnXBaJLJVAPQGRkP1Jb7YaW0nzFXSeAjnSao2T3LELL5ZTNaOLWb6UN69yyeWyCUR8657ehR8bWKlPIqmimRhNklWskK81Tjwlu3qGkknJ/VHPPqR6HvQitZNpbxRbwFQDWvel1pAOd9822NjbWN6JjqJzfRi+vy6yqY3LVZwy2RGZ9DWuzMeaSDVu9hszdH1NfLZcqOpNQFALTpUYPYKb8PEdikr2Q2iebbCJCcVXdlFQlLGye33qZubt7RLyBYqd6ZEB',current_timestamp);
insert into TPSD_EMAIL(EMAIL)
    values('thinkparity@thinkparity.com');
insert into TPSD_USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    values(7000,5000,'1');

insert into TPSD_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD,CREATED_ON)
    values('backup','DMnBhTS/KlVqMZTwRqwTNw==','What is the city where you were born?','p0B7MKs7hs21lMFBgHe/+g==','0','iHt2ZotgbGgfDUZmdRUWf2FWWSzqlCzVgQxyhqrBElNEaamLxMcfRX+Si3bcB/WxzZsAmPDs3j3Pmg21I07RI9ubyF63bmfCQM2bt1ThCX6qNR2T713kQqFDXx4GaMcUXux7ARX4FEAcs2XfkR4zqmyt5TQ6aLpbBAtgvyTwEXLE5mfCB3jF86asppABKQvQR2MpIGUOKmmGGXmUAOKWFegfceYI3MUZJRKS2ML2f+xbFsO6DJmQjyIwcUCz4W4p+SxhLOG2BPci7ctnlVEmnjns0bfBV1yYeGpK2YAjKsr/PpE5V+JwQsPcADt2lHs1qf45xWhrxBEfQfM3s7GMtW0pj7n8EodmbTECmkAt/MKyXqKw6A/yeWeHwjR0y+Z+2dB9zozhmURhQYugiEoEBSVFLS4ZJIadb+K5VBw3WaWcLYXFssyDTWuWjmG0xQu6',current_timestamp);
insert into TPSD_EMAIL(EMAIL)
    values('backup@thinkparity.com');
insert into TPSD_USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    values(7001,5001,'1');

insert into TPSD_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD,CREATED_ON)
    values('support','DMnBhTS/KlVqMZTwRqwTNw==','What is the city where you were born?','iHt2ZotgbGgfDUZmdRUWf2FWWSzqlCzVgQxyhqrBElNEaamLxMcfRX+Si3bcB/WxnAgRqR/RV/G3S5ucUFu4THviogl9qb7Pzsku+GnG1UZMxOitf+Qu7HDQ8Fdb/d3kRprXV5bS11dsZa50igOCNO1nCXzCbE8wh/ZrSLzAGwn7msCwpEIWLqY4VTpaHXKM/mTZDZZpGkEjMGOqH7UCZwHya/BSm2t0Ks/vzDO61s+ywgWKDaWFEmdzs9L38kyiKgZGLpHKS9FzY+rM7o+2yg==',current_timestamp);
insert into TPSD_EMAIL(EMAIL)
    values('support@thinkparity.com');
insert into TPSD_USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    values(7002,5002,'1');
insert into TPSD_USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7002,6000);
insert into TPSD_USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7002,6001);
