
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
    values('backup','DMnBhTS/KlVqMZTwRqwTNw==','What is the city where you were born?','p0B7MKs7hs21lMFBgHe/+g==','0','iHt2ZotgbGgfDUZmdRUWf2FWWSzqlCzVgQxyhqrBElNEaamLxMcfRX+Si3bcB/WxzZsAmPDs3j3Pmg21I07RI9ubyF63bmfCQM2bt1ThCX6qNR2T713kQqFDXx4GaMcUXux7ARX4FEAcs2XfkR4zqmyt5TQ6aLpbBAtgvyTwEXLE5mfCB3jF86asppABKQvQR2MpIGUOKmmGGXmUAOKWFegfceYI3MUZJRKS2ML2f+xbFsO6DJmQjyIwcUCz4W4p+SxhLOG2BPci7ctnlVEmnjns0bfBV1yYeGpK2YAjKsr/PpE5V+JwQsPcADt2lHs1qf45xWhrxBEfQfM3s7GMtW0pj7n8EodmbTECmkAt/MKyXqKw6A/yeWeHwjR0y+Z+2dB9zozhmURhQYugiEoEBSVFLS4ZJIadb+K5VBw3WaWcLYXFssyDTWuWjmG0xQu6',current_timestamp);
insert into TPSD_EMAIL(EMAIL)
    values('backup@thinkparity.com');
insert into TPSD_USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    values(7001,5001,'1');

insert into TPSD_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD,CREATED_ON)
    values('support','DMnBhTS/KlVqMZTwRqwTNw==','What is the city where you were born?','p0B7MKs7hs21lMFBgHe/+g==','0',' iHt2ZotgbGgfDUZmdRUWf2FWWSzqlCzVgQxyhqrBElNEaamLxMcfRX+Si3bcB/WxzZsAmPDs3j3Pmg21I07RI9ubyF63bmfCQM2bt1ThCX6qNR2T713kQqFDXx4GaMcUXux7ARX4FEAcs2XfkR4zqradpTtqKvDMRniVx+0j+ctO9M3mmmsI8vGdGBsvDBi4mPQQztzsA+1UsOJOvRsfsvmKllO8YO5+AFjdlb/YPGGkKcDT0wg5ZeRGXVobgpjGMfPd1+vvi/NgzqjouXb47dxVTfEBM8ZLXvaGJ0i+/M/gYcaOPmmcPYmkN+jA6eQa15SWKRHJcGc6KXYhUar2ap8kn9822yeLei49Thl1XxBzTHw+cA6UiD4q3ZQ6HV22hn4VJ+7pw3lX2bdyNIyXtBTCxlHBowne636tiv4mVItlilpH2Fq0FTZO1/PiE4DW',current_timestamp);
insert into TPSD_EMAIL(EMAIL)
    values('support@thinkparity.com');
insert into TPSD_USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    values(7002,5002,'1');
insert into TPSD_USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7002,6000);
insert into TPSD_USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7002,6001);

insert into TPSD_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD,CREATED_ON)
    values('junit','pWzSbOSXbDAeKK2N20BskQ==','What is my username?','etnNzBdB15n1RAGrb3/AuA==','0','iHt2ZotgbGgfDUZmdRUWf2FWWSzqlCzVgQxyhqrBElNEaamLxMcfRX+Si3bcB/WxzZsAmPDs3j3Pmg21I07RI9ubyF63bmfCQM2bt1ThCX6qNR2T713kQqFDXx4GaMcUMxfT2penjvQSkGx22aGnB7XJAb2OJgecNb7wSvfufZvVmeN54Sxb1tOTzHKErFkiA+boyUcJVI9MBGCuLomq4qQpwNPTCDll5EZdWhuCmMYx893X6++L82DOqOi5dvjt3FVN8QEzxkte9oYnSL78z+Bhxo4+aZw9iaQ36MDp5BrXlJYpEclwZzopdiFRqvZqjkN/J0fsaZe1GW4bLAGTxSbS0QkVJ/sbldL2QBY/J8QB8mvwUptrdCrP78wzutbPssIFig2lhRJnc7PS9/JMoioGRi6RykvRc2PqzO6Ptso=',current_timestamp);
insert into TPSD_EMAIL(EMAIL)
    values('junit@thinkparity.com');
insert into TPSD_USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    values(7003,5003,'1');
insert into TPSD_USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7003,6000);
insert into TPSD_USER_PRODUCT_RELEASE_REL(USER_ID,PRODUCT_ID,RELEASE_ID)
    values(7003,1000,3000);

insert into TPSD_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD,CREATED_ON)
    values('junit.w','pWzSbOSXbDAeKK2N20BskQ==','What is my username?','4yTKRRhSdnsGZQGHfQF5CA==','0','iHt2ZotgbGgfDUZmdRUWf2FWWSzqlCzVgQxyhqrBElNEaamLxMcfRX+Si3bcB/WxzZsAmPDs3j3Pmg21I07RI9ubyF63bmfCQM2bt1ThCX6qNR2T713kQqFDXx4GaMcU9CDzUneQ+CrTlJmtT0qLxauJg2/ClOYSfOtR7jnOZ1UVhKW/5XybEDTh70qVjCb8Br+9RfsbqGqsXATyyuYyc6iFB3c45ue6/0OvBWXD+1niqR0z0W9kj245V/AmGLc9qX1Wtmp58EYGqMndbVhPf757qRoTO+1Zi/uPFUJkjC7fdRpdMk8BsKHa8JqZahZjUbwTI/z1qVZaFQu/3qDEogec9H2W8dL0Uyu3LJmTokGGfhUn7unDeVfZt3I0jJe0FMLGUcGjCd7rfq2K/iZUi2WKWkfYWrQVNk7X8+ITgNY=',current_timestamp);
insert into TPSD_EMAIL(EMAIL)
    values('junit.w@thinkparity.com');
insert into TPSD_USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    values(7004,5004,'1');
insert into TPSD_USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7004,6000);
insert into TPSD_USER_PRODUCT_RELEASE_REL(USER_ID,PRODUCT_ID,RELEASE_ID)
    values(7004,1000,3000);

insert into TPSD_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD,CREATED_ON)
    values('junit.x','pWzSbOSXbDAeKK2N20BskQ==','What is my username?','zZ/EVyqyRndnXuR7nu4TiA==','0','iHt2ZotgbGgfDUZmdRUWf2FWWSzqlCzVgQxyhqrBElNEaamLxMcfRX+Si3bcB/WxzZsAmPDs3j3Pmg21I07RI9ubyF63bmfCQM2bt1ThCX6qNR2T713kQqFDXx4GaMcUhqInGn+H4QFIw8r+P3xp9KuJg2/ClOYSfOtR7jnOZ1UVhKW/5XybEDTh70qVjCb8Br+9RfsbqGqsXATyyuYyc6iFB3c45ue6/0OvBWXD+1niqR0z0W9kj245V/AmGLc9qX1Wtmp58EYGqMndbVhPf757qRoTO+1Zi/uPFUJkjC7fdRpdMk8BsKHa8JqZahZjUbwTI/z1qVZaFQu/3qDEogec9H2W8dL0Uyu3LJmTokGGfhUn7unDeVfZt3I0jJe0FMLGUcGjCd7rfq2K/iZUi2WKWkfYWrQVNk7X8+ITgNY=',current_timestamp);
insert into TPSD_EMAIL(EMAIL)
    values('junit.x@thinkparity.com');
insert into TPSD_USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    values(7005,5005,'1');
insert into TPSD_USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7005,6000);
insert into TPSD_USER_PRODUCT_RELEASE_REL(USER_ID,PRODUCT_ID,RELEASE_ID)
    values(7005,1000,3000);

insert into TPSD_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD,CREATED_ON)
    values('junit.y','pWzSbOSXbDAeKK2N20BskQ==','What is my username?','bhQd8ChtEVNz2PN8hDjmvw==','0',' iHt2ZotgbGgfDUZmdRUWf2FWWSzqlCzVgQxyhqrBElNEaamLxMcfRX+Si3bcB/WxzZsAmPDs3j3Pmg21I07RI9ubyF63bmfCQM2bt1ThCX6qNR2T713kQqFDXx4GaMcUEyc0Dg8GA3GYayUUAbyIPKuJg2/ClOYSfOtR7jnOZ1UVhKW/5XybEDTh70qVjCb8Br+9RfsbqGqsXATyyuYyc6iFB3c45ue6/0OvBWXD+1niqR0z0W9kj245V/AmGLc9qX1Wtmp58EYGqMndbVhPf757qRoTO+1Zi/uPFUJkjC7fdRpdMk8BsKHa8JqZahZjUbwTI/z1qVZaFQu/3qDEogec9H2W8dL0Uyu3LJmTokGGfhUn7unDeVfZt3I0jJe0FMLGUcGjCd7rfq2K/iZUi2WKWkfYWrQVNk7X8+ITgNY=',current_timestamp);
insert into TPSD_EMAIL(EMAIL)
    values('junit.y@thinkparity.com');
insert into TPSD_USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    values(7006,5006,'1');
insert into TPSD_USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7006,6000);
insert into TPSD_USER_PRODUCT_RELEASE_REL(USER_ID,PRODUCT_ID,RELEASE_ID)
    values(7006,1000,3000);

insert into TPSD_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD,CREATED_ON)
    values('junit.z','pWzSbOSXbDAeKK2N20BskQ==','What is my username?','SUUCWXnav7SRcZSI1MSFBw==','0','iHt2ZotgbGgfDUZmdRUWf2FWWSzqlCzVgQxyhqrBElNEaamLxMcfRX+Si3bcB/WxzZsAmPDs3j3Pmg21I07RI9ubyF63bmfCQM2bt1ThCX6qNR2T713kQqFDXx4GaMcU+DsyVl8a6mGCnz0cKrwXvauJg2/ClOYSfOtR7jnOZ1UVhKW/5XybEDTh70qVjCb8Br+9RfsbqGqsXATyyuYyc6iFB3c45ue6/0OvBWXD+1niqR0z0W9kj245V/AmGLc9qX1Wtmp58EYGqMndbVhPf757qRoTO+1Zi/uPFUJkjC7fdRpdMk8BsKHa8JqZahZjUbwTI/z1qVZaFQu/3qDEogec9H2W8dL0Uyu3LJmTokGGfhUn7unDeVfZt3I0jJe0FMLGUcGjCd7rfq2K/iZUi2WKWkfYWrQVNk7X8+ITgNY=',current_timestamp);
insert into TPSD_EMAIL(EMAIL)
    values('junit.z@thinkparity.com');
insert into TPSD_USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    values(7007,5007,'1');
insert into TPSD_USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7007,6000);
insert into TPSD_USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7007,6001);
insert into TPSD_USER_PRODUCT_RELEASE_REL(USER_ID,PRODUCT_ID,RELEASE_ID)
    values(7007,1000,3000);

insert into TPSD_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7003,7005,7000,current_timestamp,7000,current_timestamp);
insert into TPSD_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7003,7006,7000,current_timestamp,7000,current_timestamp);
insert into TPSD_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7003,7007,7000,current_timestamp,7000,current_timestamp);

insert into TPSD_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7005,7003,7000,current_timestamp,7000,current_timestamp);
insert into TPSD_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7005,7006,7000,current_timestamp,7000,current_timestamp);
insert into TPSD_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7005,7007,7000,current_timestamp,7000,current_timestamp);

insert into TPSD_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7006,7004,7000,current_timestamp,7000,current_timestamp);
insert into TPSD_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7006,7005,7000,current_timestamp,7000,current_timestamp);
insert into TPSD_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7006,7007,7000,current_timestamp,7000,current_timestamp);

insert into TPSD_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7007,7003,7000,current_timestamp,7000,current_timestamp);
insert into TPSD_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7007,7005,7000,current_timestamp,7000,current_timestamp);
insert into TPSD_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7007,7006,7000,current_timestamp,7000,current_timestamp);
