
insert into TPSD_PRODUCT(PRODUCT_NAME) values('OpheliaProduct');
insert into TPSD_PRODUCT(PRODUCT_NAME) values('DesdemonaProduct');
insert into TPSD_PRODUCT_RELEASE(PRODUCT_ID,RELEASE_NAME,RELEASE_OS,RELEASE_DATE)
    values(1000,'DEVELOPMENT','LINUX',CURRENT_TIMESTAMP);
insert into TPSD_PRODUCT_RELEASE(PRODUCT_ID,RELEASE_NAME,RELEASE_OS,RELEASE_DATE)
    values(1000,'DEVELOPMENT','WINDOWS_XP',CURRENT_TIMESTAMP);
insert into TPSD_PRODUCT_FEATURE(PRODUCT_ID,FEATURE_NAME) values(1000,'CORE');
insert into TPSD_PRODUCT_FEATURE(PRODUCT_ID,FEATURE_NAME) values(1000,'BACKUP');

insert into TPSD_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD,CREATED_ON)
    values('thinkparity','DMnBhTS/KlVqMZTwRqwTNw==','What is the city where you were born?','p0B7MKs7hs21lMFBgHe/+g==','0','iHt2ZotgbGgfDUZmdRUWf2FWWSzqlCzVgQxyhqrBElNEaamLxMcfRX+Si3bcB/WxzZsAmPDs3j3Pmg21I07RI9ubyF63bmfCQM2bt1ThCX6qNR2T713kQqFDXx4GaMcUXux7ARX4FEAcs2XfkR4zqro7orioIW8NRIJayAxL+lPY/htSHicuiOjMxTOnXBaJLJVAPQGRkP1Jb7YaW0nzFXSeAjnSao2T3LELL5ZTNaOLWb6UN69yyeWyCUR8657ehR8bWKlPIqmimRhNklWskK81Tjwlu3qGkknJ/VHPPqR6HvQitZNpbxRbwFQDWvel1pAOd9822NjbWN6JjqJzfRi+vy6yqY3LVZwy2RGZ9DWuzMeaSDVu9hszdH1NfLZcqOpNQFALTpUYPYKb8PEdikr2Q2iebbCJCcVXdlFQlLGye33qZubt7RLyBYqd6ZEB',current_timestamp);
insert into TPSD_EMAIL(EMAIL)
    values('thinkparity@thinkparity.com');
insert into TPSD_USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    values(7000,5000,'1');

insert into TPSD_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD,CREATED_ON)
    values('thinkparity-backup','DMnBhTS/KlVqMZTwRqwTNw==','What is the city where you were born?','p0B7MKs7hs21lMFBgHe/+g==','0','iHt2ZotgbGgfDUZmdRUWf2FWWSzqlCzVgQxyhqrBElNEaamLxMcfRX+Si3bcB/WxzZsAmPDs3j3Pmg21I07RI9ubyF63bmfCQM2bt1ThCX6qNR2T713kQqFDXx4GaMcUXux7ARX4FEAcs2XfkR4zqmyt5TQ6aLpbBAtgvyTwEXLE5mfCB3jF86asppABKQvQR2MpIGUOKmmGGXmUAOKWFegfceYI3MUZJRKS2ML2f+xbFsO6DJmQjyIwcUCz4W4p+SxhLOG2BPci7ctnlVEmnjns0bfBV1yYeGpK2YAjKsr/PpE5V+JwQsPcADt2lHs1qf45xWhrxBEfQfM3s7GMtW0pj7n8EodmbTECmkAt/MKyXqKw6A/yeWeHwjR0y+Z+2dB9zozhmURhQYugiEoEBSVFLS4ZJIadb+K5VBw3WaWcLYXFssyDTWuWjmG0xQu6',current_timestamp);
insert into TPSD_EMAIL(EMAIL)
    values('thinkparity+backup@thinkparity.com');
insert into TPSD_USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    values(7001,5001,'1');

insert into TPSD_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD,CREATED_ON)
    values('thinkparity-support','DMnBhTS/KlVqMZTwRqwTNw==','What is the city where you were born?','iHt2ZotgbGgfDUZmdRUWf2FWWSzqlCzVgQxyhqrBElNEaamLxMcfRX+Si3bcB/WxzZsAmPDs3j3Pmg21I07RI9ubyF63bmfCQM2bt1ThCX6qNR2T713kQqFDXx4GaMcUXux7ARX4FEAcs2XfkR4zqradpTtqKvDMRniVx+0j+ctO9M3mmmsI8vGdGBsvDBi4mPQQztzsA+1UsOJOvRsfsvmKllO8YO5+AFjdlb/YPGGkKcDT0wg5ZeRGXVobgpjGMfPd1+vvi/NgzqjouXb47dxVTfEBM8ZLXvaGJ0i+/M/gYcaOPmmcPYmkN+jA6eQa15SWKRHJcGc6KXYhUar2ap8kn9822yeLei49Thl1XxBzTHw+cA6UiD4q3ZQ6HV22hn4VJ+7pw3lX2bdyNIyXtBTCxlHBowne636tiv4mVItlilpH2Fq0FTZO1/PiE4DW',current_timestamp);
insert into TPSD_EMAIL(EMAIL)
    values('thinkparity+support@thinkparity.com');
insert into TPSD_USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    values(7002,5002,'1');
insert into TPSD_USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7002,6000);
insert into TPSD_USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7002,6001);
