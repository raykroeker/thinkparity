insert into TPSD_PRODUCT(PRODUCT_NAME) values('OpheliaProduct');
insert into TPSD_PRODUCT(PRODUCT_NAME) values('DesdemonaProduct');
insert into TPSD_PRODUCT_RELEASE(PRODUCT_ID,RELEASE_NAME,RELEASE_OS,RELEASE_DATE)
    select PRODUCT_ID,'DEMO','LINUX',CURRENT_TIMESTAMP from TPSD_PRODUCT where PRODUCT_NAME='OpheliaProduct';
insert into TPSD_PRODUCT_RELEASE(PRODUCT_ID,RELEASE_NAME,RELEASE_OS,RELEASE_DATE)
    select PRODUCT_ID,'DEMO','WINDOWS_XP',CURRENT_TIMESTAMP from TPSD_PRODUCT where PRODUCT_NAME='OpheliaProduct';
insert into TPSD_PRODUCT_FEATURE(PRODUCT_ID,FEATURE_NAME)
    select PRODUCT_ID,'CORE' from TPSD_PRODUCT where PRODUCT_NAME='OpheliaProduct';
insert into TPSD_PRODUCT_FEATURE(PRODUCT_ID,FEATURE_NAME)
    select PRODUCT_ID,'BACKUP' from TPSD_PRODUCT where PRODUCT_NAME='OpheliaProduct';

-- thinkParity
insert into TPSD_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD,CREATED_ON)
    values('thinkparity','DMnBhTS/KlVqMZTwRqwTNw==','What is the city where you were born?','p0B7MKs7hs21lMFBgHe/+g==','0','iHt2ZotgbGgfDUZmdRUWf2FWWSzqlCzVgQxyhqrBElNEaamLxMcfRX+Si3bcB/WxzZsAmPDs3j3Pmg21I07RI9ubyF63bmfCQM2bt1ThCX6qNR2T713kQqFDXx4GaMcUXux7ARX4FEAcs2XfkR4zqro7orioIW8NRIJayAxL+lPY/htSHicuiOjMxTOnXBaJLJVAPQGRkP1Jb7YaW0nzFXSeAjnSao2T3LELL5ZTNaOLWb6UN69yyeWyCUR8657ehR8bWKlPIqmimRhNklWskK81Tjwlu3qGkknJ/VHPPqR6HvQitZNpbxRbwFQDWvel1pAOd9822NjbWN6JjqJzfRi+vy6yqY3LVZwy2RGZ9DWuzMeaSDVu9hszdH1NfLZcqOpNQFALTpUYPYKb8PEdikr2Q2iebbCJCcVXdlFQlLGye33qZubt7RLyBYqd6ZEB',CURRENT_TIMESTAMP);
insert into TPSD_EMAIL(EMAIL)
    values('thinkparity@thinkparity.com');
insert into TPSD_USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    select USER_ID,EMAIL_ID,'1' from TPSD_USER,TPSD_EMAIL where USERNAME='thinkparity' and EMAIL='thinkparity@thinkparity.com';

-- thinkParity Backup
insert into TPSD_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD,CREATED_ON)
    values('backup','DMnBhTS/KlVqMZTwRqwTNw==','What is the city where you were born?','p0B7MKs7hs21lMFBgHe/+g==','0','iHt2ZotgbGgfDUZmdRUWf2FWWSzqlCzVgQxyhqrBElNEaamLxMcfRX+Si3bcB/WxzZsAmPDs3j3Pmg21I07RI9ubyF63bmfCQM2bt1ThCX6qNR2T713kQqFDXx4GaMcUXux7ARX4FEAcs2XfkR4zqmyt5TQ6aLpbBAtgvyTwEXLE5mfCB3jF86asppABKQvQR2MpIGUOKmmGGXmUAOKWFegfceYI3MUZJRKS2ML2f+xbFsO6DJmQjyIwcUCz4W4p+SxhLOG2BPci7ctnlVEmnjns0bfBV1yYeGpK2YAjKsr/PpE5V+JwQsPcADt2lHs1qf45xWhrxBEfQfM3s7GMtW0pj7n8EodmbTECmkAt/MKyXqKw6A/yeWeHwjR0y+Z+2dB9zozhmURhQYugiEoEBSVFLS4ZJIadb+K5VBw3WaWcLYXFssyDTWuWjmG0xQu6',CURRENT_TIMESTAMP);
insert into TPSD_EMAIL(EMAIL)
    values('backup@thinkparity.com');
insert into TPSD_USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    select USER_ID,EMAIL_ID,'1' from TPSD_USER,TPSD_EMAIL where USERNAME='backup' and EMAIL='backup@thinkparity.com';

-- thinkParity Support
insert into TPSD_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD,CREATED_ON)
    values('support','DMnBhTS/KlVqMZTwRqwTNw==','What is the city where you were born?','p0B7MKs7hs21lMFBgHe/+g==','0','iHt2ZotgbGgfDUZmdRUWf2FWWSzqlCzVgQxyhqrBElNEaamLxMcfRX+Si3bcB/WxzZsAmPDs3j3Pmg21I07RI9ubyF63bmfCQM2bt1ThCX6qNR2T713kQqFDXx4GaMcUXux7ARX4FEAcs2XfkR4zqradpTtqKvDMRniVx+0j+ctO9M3mmmsI8vGdGBsvDBi4mPQQztzsA+1UsOJOvRsfsvmKllO8YO5+AFjdlb/YPGGkKcDT0wg5ZeRGXVobgpjGMfPd1+vvi/NgzqjouXb47dxVTfEBM8ZLXvaGJ0i+/M/gYcaOPmmcPYmkN+jA6eQa15SWKRHJcGc6KXYhUar2ap8kn9822yeLei49Thl1XxBzTHw+cA6UiD4q3ZQ6HV22hn4VJ+7pw3lX2bdyNIyXtBTCxlHBowne636tiv4mVItlilpH2Fq0FTZO1/PiE4DW',CURRENT_TIMESTAMP);
insert into TPSD_EMAIL(EMAIL)
    values('support@thinkparity.com');
insert into TPSD_USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    select USER_ID,EMAIL_ID,'1' from TPSD_USER,TPSD_EMAIL where USERNAME='support' and EMAIL='support@thinkparity.com';

-- George Johnson (CFO, ServiceSoft Inc)
insert into TPSD_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD,CREATED_ON)
    values('gjohnson.servicesoft','IrgYfGO2Ark1JQF7zrBcXg==','What is the city where you were born?','p0B7MKs7hs21lMFBgHe/+g==','0','iHt2ZotgbGgfDUZmdRUWf2FWWSzqlCzVgQxyhqrBElNEaamLxMcfRX+Si3bcB/Wxgo8V5E3+CwiQCO24SvoUAhm/Oud9N8GJjYeL1oEivt5kt6uM15Gjg1wY6+R6AGlccJky7P8LDxgTF6iiBA6ogeieRf8jv5kMuqOyr3GAWzRdJBAAzxMK+GuabmN4lbVIJMLHHTgTS1x/H6wcpHfECyO2fDvkvK2MQfLcyZw92Fb1WY2XC166uo/8njTpkSnDTqeZCUlHH6jqnsUM+0EKhRNKEVlkoWsNYlux2ua8nThRO1E1pus95wVPJ7ooY+OamiDHQMVY08+CB8/zeFNiML7SAjtnN+D0bd21qbtrM4+9QMzH1SGTTYKaPPYc4k/cXoubHpiM2Rgc8m0cbyXF6cb+jqE030OOCbLoUYndX3BJ4KmlOVNK8i+Ex14djdplmsqyJwxe+7lnTv2+UiK0QFfVLQnOx1u9LU/867xzJJoTkpBvrN2Gnu1KhKRmKFQe5JzbFOCJxdTaKMRyMRRERQ==',CURRENT_TIMESTAMP);
insert into TPSD_EMAIL(EMAIL)
    values('gjohnson@servicesoft.com');
insert into TPSD_USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    select USER_ID,EMAIL_ID,'1' from TPSD_USER, TPSD_EMAIL where USERNAME='gjohnson.servicesoft' and EMAIL='gjohnson@servicesoft.com';
    
-- John Bruce (Director Human Resources, ServiceSoft Inc)
insert into TPSD_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD,CREATED_ON)
    values('jbruce.servicesoft','IrgYfGO2Ark1JQF7zrBcXg==','What is the city where you were born?','p0B7MKs7hs21lMFBgHe/+g==','0','iHt2ZotgbGgfDUZmdRUWf2FWWSzqlCzVgQxyhqrBElNEaamLxMcfRX+Si3bcB/Wxgo8V5E3+CwiQCO24SvoUAhm/Oud9N8GJjYeL1oEivt5kt6uM15Gjg1wY6+R6AGlccJky7P8LDxgTF6iiBA6ogZFzP0rEEgygv86SuMTm2o2P0jGpuSscEbH8xJsg34kYq4aiT73851N11jlxiuMQhdu8MUnux84V4G3CAnWapYGohQd3OObnuv9DrwVlw/tZ4qkdM9FvZI9uOVfwJhi3Pal9VrZqefBGBqjJ3W1YT3++e6kaEzvtWYv7jxVCZIwu33UaXTJPAbCh2vCamWoWY1G8EyP89alWWhULv96gxKJZlMgzpNxUo4uwTHxgyxePi/PejPoejj5ukTxsTYJ6e/N3018t4Mf1nmI5Q8M9itiMMTM32TpFN6fYz0Gz5kE28D2H/gySY6Z602BdmxKh6dsFgKCMwcPpMcSskegt35/3AxOIwbNP66CxbZHcr1A0tjl1MNP7bUG16E9bTPbIZKkFBDIuKfSm6KcqL+E2qBs=',CURRENT_TIMESTAMP);
insert into TPSD_EMAIL(EMAIL)
    values('jbruce@servicesoft.com');
insert into TPSD_USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    select USER_ID,EMAIL_ID,'1' from TPSD_USER, TPSD_EMAIL where USERNAME='jbruce.servicesoft' and EMAIL='jbruce@servicesoft.com';

-- Robert Bay (CEO, ServiceSoft Inc)
insert into TPSD_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD,CREATED_ON)
    values('rbay.servicesoft','IrgYfGO2Ark1JQF7zrBcXg==','What is the city where you were born?','p0B7MKs7hs21lMFBgHe/+g==','0','iHt2ZotgbGgfDUZmdRUWf2FWWSzqlCzVgQxyhqrBElNEaamLxMcfRX+Si3bcB/Wxgo8V5E3+CwiQCO24SvoUAhm/Oud9N8GJjYeL1oEivt5kt6uM15Gjg1wY6+R6AGlccJky7P8LDxgTF6iiBA6ogXG1OLRzjhN/c2rL4kV2kRi1GQ+WpuTGyEBbDfhp51Etq4aiT73851N11jlxiuMQhdu8MUnux84V4G3CAnWapYGohQd3OObnuv9DrwVlw/tZ4qkdM9FvZI9uOVfwJhi3Pal9VrZqefBGBqjJ3W1YT3++e6kaEzvtWYv7jxVCZIwu33UaXTJPAbCh2vCamWoWY1G8EyP89alWWhULv96gxKJuIyS8oNh2ByU+zmY+FvUatCXmQm0l0yG1lEPAQI3QSkfVNma2V8naKIBLSwCkfmPWsbe9srIGwfb9VGtIEW/HAFA0EysY9NZj3JYtdjgKgbTWB2//uP5XwRz/IUzN3SPiQm3QDvDzOrQQ54Tg+U+l86NFgsVRsgnVbTk3pzEsjQ==',CURRENT_TIMESTAMP);
insert into TPSD_EMAIL(EMAIL)
    values('rbay@servicesoft.com');
insert into TPSD_USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    select USER_ID,EMAIL_ID,'1' from TPSD_USER, TPSD_EMAIL where USERNAME='rbay.servicesoft' and EMAIL='rbay@servicesoft.com';

-- Rick Wales (CEO, Digital Solutions)
insert into TPSD_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD,CREATED_ON)
    values('rwales.digitalsolutions','IrgYfGO2Ark1JQF7zrBcXg==','What is the city where you were born?','p0B7MKs7hs21lMFBgHe/+g==','0','iHt2ZotgbGgfDUZmdRUWf2FWWSzqlCzVgQxyhqrBElNEaamLxMcfRX+Si3bcB/Wxgo8V5E3+CwiQCO24SvoUAhm/Oud9N8GJjYeL1oEivt5kt6uM15Gjg1wY6+R6AGlccJky7P8LDxgTF6iiBA6ogRpDgL1AAEtIbhY/m5xhX41V2yG1Kp5HzxMAKE78eEBaL4+VZz88rgQApdQtV7OkDVKQqps9/NyJpXZI4Pc3oEOCQmkQBPfqXGy48d4Jpp2hLxZNlwtyRBhGcOUwNCgj2T1AnvpGpIxAlgTcF6o4gEiUDbspHKo/4oSb2mYu9sp5fHbFxWVbwRxldEOdMJp7TYdkrRkZpz3Rd5h5m0Cxoprw3R7FuNxOTm6DuvFf39VSSFTQCOwZqvrkNdhBdQWRmXNZL/R2o6zVVqXWgzMmSj4WA/GB7oXCSc5nWlu3dWhxS0acxjMagRwIFHAYHOQvaA59RNJqqWwMXUeLYjg3RsxMosk72hk/J5SWb8YRIcr/+J8vttbfokaZEEx0hp7tnw==',CURRENT_TIMESTAMP);
insert into TPSD_EMAIL(EMAIL)
    values('rwales@digitalsolutions.com');
insert into TPSD_USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    select USER_ID,EMAIL_ID,'1' from TPSD_USER, TPSD_EMAIL where USERNAME='rwales.digitalsolutions' and EMAIL='rwales@digitalsolutions.com';

-- Sean Hogan (Lawyer, MacKay & Associates)
insert into TPSD_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD,CREATED_ON)
    values('shogan.mackay','IrgYfGO2Ark1JQF7zrBcXg==','What is the city where you were born?','p0B7MKs7hs21lMFBgHe/+g==','0','iHt2ZotgbGgfDUZmdRUWf2FWWSzqlCzVgQxyhqrBElNEaamLxMcfRX+Si3bcB/WxJTZth1pzuH43aCsXmBNE5srdeGJs0JZppZQY085hf/KkTRXxLZJj9Z2UV2IiX9vrpPOi8grGR+gFuF4GRkZQoURJ1+ul8xPW1510KXb1DDLPZmqJebmra2is6C5+8VuqPsJ+w5umPQItN+x/tRSf5cgG4rMJ/BtVqcPr8UcNnXGv9Ai2RytPeN4U8r+9oNvjbPb2GYEYgzHiFft3wbFD0KU9Sei2jgF2TnYxlgPm816EO5ZgrWuzIXK+0IcQZHl4FrB1zPgZ0GvIHk9QlmamBqiFB3c45ue6/0OvBWXD+1kSXDSte2te7i7KUf/F+OxUqX1Wtmp58EYGqMndbVhPf7nlIUR1Ntifr8HL3L/rhIyCoLpBisqKUdWTqlSP5S7t2NclAx0OHiNHaUlsaP6TZxOIQ+SYJFcVOwH4M8uH8JHrufgEtq11tYZPtZg7uXonX3UhK1VllfspCWIJI8a2jYuedfnBpCAeE3tW78RUfX4kBJV/f/bG0MUuXxxutARqXbbhoPu9FA05y1GsK6L//URMjg0bQ7CwHEDSoGds/UZhVlks6pQs1YEMcoaqwRJTmjEzd8eyUe2k6QMN+RMelQ==',CURRENT_TIMESTAMP);
insert into TPSD_EMAIL(EMAIL)
    values('shogan@mackay.com');
insert into TPSD_USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    select USER_ID,EMAIL_ID,'1' from TPSD_USER, TPSD_EMAIL where USERNAME='shogan.mackay' and EMAIL='shogan@mackay.com';

-- Paul Stevenson (CEO, OnCorp Inc)
insert into TPSD_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD,CREATED_ON)
    values('pstevenson.oncorp','IrgYfGO2Ark1JQF7zrBcXg==','What is the city where you were born?','p0B7MKs7hs21lMFBgHe/+g==','0','iHt2ZotgbGgfDUZmdRUWf2FWWSzqlCzVgQxyhqrBElNEaamLxMcfRX+Si3bcB/Wxgo8V5E3+CwiQCO24SvoUAhm/Oud9N8GJjYeL1oEivt5kt6uM15Gjg1wY6+R6AGlccJky7P8LDxgTF6iiBA6ogbGisj+anVUEFSj/DGCZY3Fkbq8LJnVxxvsaa/THn8wxWva7+ldoBPplCrwUBHqw46Kqe55MR+EbeubZy2PthrJbFsO6DJmQjyIwcUCz4W4p+SxhLOG2BPci7ctnlVEmnjns0bfBV1yYeGpK2YAjKsr/PpE5V+JwQsPcADt2lHs1qf45xWhrxBEfQfM3s7GMtW0pj7n8EodmbTECmkAt/MIsxjPlxwjn/QOF5P7DDZomBSdljvHGsUIq5RCeSbTn9kuulNNTmyhu9bzZTkCderTi11GVY3yup8XNpqSKWJE8J2KQezo6ND4NSYWSIPbjytEy0i6Cw+57dX55Um2PmI5LDDj06s74G0p+4maZn45HhbnHWnflEio1Uu5tD79YIw==',CURRENT_TIMESTAMP);
insert into TPSD_EMAIL(EMAIL)
    values('pstevenson@oncorp.com');
insert into TPSD_USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    select USER_ID,EMAIL_ID,'1' from TPSD_USER, TPSD_EMAIL where USERNAME='pstevenson.oncorp' and EMAIL='pstevenson@oncorp.com';

-- Susan Race (Vice President Services, SCI Solutions)
insert into TPSD_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD,CREATED_ON)
    values('srace.sci','IrgYfGO2Ark1JQF7zrBcXg==','What is the city where you were born?','p0B7MKs7hs21lMFBgHe/+g==','0','iHt2ZotgbGgfDUZmdRUWf2FWWSzqlCzVgQxyhqrBElNEaamLxMcfRX+Si3bcB/Wxgo8V5E3+CwiQCO24SvoUAhm/Oud9N8GJjYeL1oEivt5kt6uM15Gjg1wY6+R6AGlccJky7P8LDxgTF6iiBA6ogeFm0iSCjLxa7XHSMIgd6JfpCbbtic3HrlWmIl0Xo8vRnRb+hA4KW95hs5tKZYBLMNYV+OG9sl/ZHHbGTJh2C4KkKcDT0wg5ZeRGXVobgpjGMfPd1+vvi/NgzqjouXb47dxVTfEBM8ZLXvaGJ0i+/M/gYcaOPmmcPYmkN+jA6eQa15SWKRHJcGc6KXYhUar2am054APpxdB/XU301P67kGMeIWr8Cq7QRVok6+xoqFokSxBxMyHFsyPIG4XpNQITa3Lo0SmMOg1SQv4yvnupe1HGDJjmvfLHShIA+LaExh3veNqK2yye/UhDTs9T0LExyc9kTqIGS9ZXe3SYE/JZUR3UA0q+BOkRxa5Y2x6eQf3OJuw7V1Nqoaa5jgz3/pr1Qz0oioD5j5ed22sc4rhRn8Q=',CURRENT_TIMESTAMP);
insert into TPSD_EMAIL(EMAIL)
    values('srace@sci.com');
insert into TPSD_USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    select USER_ID,EMAIL_ID,'1' from TPSD_USER, TPSD_EMAIL where USERNAME='srace.sci' and EMAIL='srace@sci.com';

-- Craig Clare (CFO, SCI Solutions)
insert into TPSD_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD,CREATED_ON)
    values('cclare.sci','IrgYfGO2Ark1JQF7zrBcXg==','What is the city where you were born?','p0B7MKs7hs21lMFBgHe/+g==','0','iHt2ZotgbGgfDUZmdRUWf2FWWSzqlCzVgQxyhqrBElNEaamLxMcfRX+Si3bcB/Wxgo8V5E3+CwiQCO24SvoUAhm/Oud9N8GJjYeL1oEivt5kt6uM15Gjg1wY6+R6AGlccJky7P8LDxgTF6iiBA6ogSnnTEPvVEtKH01mRRX5ImlHoLbJD/nEvL8GPnOMF0GK6ieCz/d3iDnqss/bIsjTqG20u0OyQemFfJZP7SLKNcZbFsO6DJmQjyIwcUCz4W4p+SxhLOG2BPci7ctnlVEmnjns0bfBV1yYeGpK2YAjKsr/PpE5V+JwQsPcADt2lHs1qf45xWhrxBEfQfM3s7GMtW0pj7n8EodmbTECmkAt/MJNhwiuOrxQa+G+7VaC5X/EBSdljvHGsUIq5RCeSbTn9kuulNNTmyhu9bzZTkCderTi11GVY3yup8XNpqSKWJE8J2KQezo6ND4NSYWSIPbjytEy0i6Cw+57dX55Um2PmI5LDDj06s74G0p+4maZn45HhbnHWnflEio1Uu5tD79YIw==',CURRENT_TIMESTAMP);
insert into TPSD_EMAIL(EMAIL)
    values('cclare@sci.com');
insert into TPSD_USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    select USER_ID,EMAIL_ID,'1' from TPSD_USER, TPSD_EMAIL where USERNAME='cclare.sci' and EMAIL='cclare@sci.com';

-- Simon Bolin (Vice President Marketing, SCI Solutions)
insert into TPSD_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD,CREATED_ON)
    values('sbolin.sci','IrgYfGO2Ark1JQF7zrBcXg==','What is the city where you were born?','p0B7MKs7hs21lMFBgHe/+g==','0','iHt2ZotgbGgfDUZmdRUWf2FWWSzqlCzVgQxyhqrBElNEaamLxMcfRX+Si3bcB/Wxgo8V5E3+CwiQCO24SvoUAhm/Oud9N8GJjYeL1oEivt5kt6uM15Gjg1wY6+R6AGlccJky7P8LDxgTF6iiBA6ogSUInhlkq/S0uRuXs4MmMi5reOAL7dhiiYL6gk66R9kf6ieCz/d3iDnqss/bIsjTqG20u0OyQemFfJZP7SLKNcZbFsO6DJmQjyIwcUCz4W4p+SxhLOG2BPci7ctnlVEmnjns0bfBV1yYeGpK2YAjKsr/PpE5V+JwQsPcADt2lHs1qf45xWhrxBEfQfM3s7GMtW0pj7n8EodmbTECmkAt/MKvBcJlljVeFgv1ISlzqCnYK8XwCB9va+8bLIjNIM8/wn6toto8RuNkuwaaGs37uIZmVhhrGsCpL1XTpUT/g/KXb8xt8wgbbrbdMfr8urJjbi+dG+DRHN5Iwa/gvsTjySNlIxFwu9ObacXEqCMNgb3U4Lz7ZQOcPiXhpeGO4frmAz0l1ZcEB3+sg6hJySNa500=',CURRENT_TIMESTAMP);
insert into TPSD_EMAIL(EMAIL)
    values('sbolin@sci.com');
insert into TPSD_USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    select USER_ID,EMAIL_ID,'1' from TPSD_USER, TPSD_EMAIL where USERNAME='sbolin.sci' and EMAIL='sbolin@sci.com';

-- David Stevenson (President, SCI Solutions)
insert into TPSD_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD,CREATED_ON)
    values('dstevenson.sci','IrgYfGO2Ark1JQF7zrBcXg==','What is the city where you were born?','p0B7MKs7hs21lMFBgHe/+g==','0','iHt2ZotgbGgfDUZmdRUWf2FWWSzqlCzVgQxyhqrBElNEaamLxMcfRX+Si3bcB/Wxgo8V5E3+CwiQCO24SvoUAhm/Oud9N8GJjYeL1oEivt5kt6uM15Gjg1wY6+R6AGlccJky7P8LDxgTF6iiBA6ogVfBcl4Vr7nhsL9ZcL0yAHcbInCm29RapLU9jMo38L9E86Pdm9Qye8+akD/WsZ4SnJ3CsRJDI5chN9QHXcfWwBcc/vFhFMqwopCxYFbHTtXijxbmL318AvXccdRz0MaPugNmTWe6N4VgQEJt8QSAOcFvr0iYL3wVQg6PkYeTgRcHBQvtwE+J+CGbJCbXRuyWLddJBVhcD8PpREeObUpuv/JrRqISJQcS6sRWfq9VN3jj3ZchfEX7x89Fx17HoOTUIp9rH8P4VNBChVyBLf+CjeJ2tpIRpeNDMe5vL2aklmEY2rBKUyv0izOKI+x6vAyMCwHya/BSm2t0Ks/vzDO61s+ywgWKDaWFEmdzs9L38kyiKgZGLpHKS9FzY+rM7o+2yg==',CURRENT_TIMESTAMP);
insert into TPSD_EMAIL(EMAIL)
    values('dstevenson@sci.com');
insert into TPSD_USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    select USER_ID,EMAIL_ID,'1' from TPSD_USER, TPSD_EMAIL where USERNAME='dstevenson.sci' and EMAIL='dstevenson@sci.com';

-- Jennifer Stone (Vice President Marketing, BioSpace Inc)
insert into TPSD_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD,CREATED_ON)
    values('jstone.biospace','IrgYfGO2Ark1JQF7zrBcXg==','What is the city where you were born?','p0B7MKs7hs21lMFBgHe/+g==','0','iHt2ZotgbGgfDUZmdRUWf2FWWSzqlCzVgQxyhqrBElNEaamLxMcfRX+Si3bcB/Wxgo8V5E3+CwiQCO24SvoUAhm/Oud9N8GJjYeL1oEivt5kt6uM15Gjg1wY6+R6AGlccJky7P8LDxgTF6iiBA6ogeJZPsepgKiXRFzO1uh9ovHeoisWbLA66DMhVUtVk+6IUEeLNh4EeVNfUoGD3VuLqjDNWWmwQz3UXPF4NkOVGhyF6KcxOEwlhpeyPgaBaiOk2/0NmfmZ1hrO8N0I8Pry+yfoQE+vwTXsKkSU8h8aO1rMJeLVfxrgCXYdKes4RRmkMonKsHXgDetcnagPU1n9WwFBtO814rOyrZ4tqB/Pxj0HGEaVi2mqzQeKagg9N+A6/gIhccEY3g5yGbY1Fkl8hCV2gWCJNifQj9ITfdlEkg9305Asoj7P+/+dzjFy7A0UlXHAO0ZbdCOuJqjMbiMB3T3aLG6FTAJmNlcfCXXTPkDrqdZ+nXBbmXaaG7O69/wdvCDFAnqk8SIUIS2HHjXlg9R0puERvhHx5R5+Fp5C2g4=',CURRENT_TIMESTAMP);
insert into TPSD_EMAIL(EMAIL)
    values('jstone@biospace.com');
insert into TPSD_USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    select USER_ID,EMAIL_ID,'1' from TPSD_USER, TPSD_EMAIL where USERNAME='jstone.biospace' and EMAIL='jstone@biospace.com';

-- Mark Francis (CFO, BioSpace)
insert into TPSD_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD,CREATED_ON)
    values('mfrancis.biospace','IrgYfGO2Ark1JQF7zrBcXg==','What is the city where you were born?','p0B7MKs7hs21lMFBgHe/+g==','0','iHt2ZotgbGgfDUZmdRUWf2FWWSzqlCzVgQxyhqrBElNEaamLxMcfRX+Si3bcB/Wxgo8V5E3+CwiQCO24SvoUAhm/Oud9N8GJjYeL1oEivt5kt6uM15Gjg1wY6+R6AGlccJky7P8LDxgTF6iiBA6ogYdZTm/fQ4OFkZZ4gHuvwJFTS++1R9saSJGFvQTmzXBFbzIJmrMiDTeTgmLwAlneWl7m9UWg8gFFw4TvvX8oHRVbFsO6DJmQjyIwcUCz4W4p+SxhLOG2BPci7ctnlVEmnjns0bfBV1yYeGpK2YAjKsr/PpE5V+JwQsPcADt2lHs1qf45xWhrxBEfQfM3s7GMtW0pj7n8EodmbTECmkAt/MJNhwiuOrxQa+G+7VaC5X/EBSdljvHGsUIq5RCeSbTn9kuulNNTmyhu9bzZTkCderTi11GVY3yup8XNpqSKWJE8J2KQezo6ND4NSYWSIPbjytEy0i6Cw+57dX55Um2PmI5LDDj06s74G0p+4maZn45HhbnHWnflEio1Uu5tD79YIw==',CURRENT_TIMESTAMP);
insert into TPSD_EMAIL(EMAIL)
    values('mfrancis@biospace.com');
insert into TPSD_USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    select USER_ID,EMAIL_ID,'1' from TPSD_USER, TPSD_EMAIL where USERNAME='mfrancis.biospace' and EMAIL='mfrancis@biospace.com';

-- Peter Chen (Controller, BioSpace Inc)
insert into TPSD_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD,CREATED_ON)
    values('pchen.biospace','IrgYfGO2Ark1JQF7zrBcXg==','What is the city where you were born?','p0B7MKs7hs21lMFBgHe/+g==','0','iHt2ZotgbGgfDUZmdRUWf2FWWSzqlCzVgQxyhqrBElNEaamLxMcfRX+Si3bcB/Wxgo8V5E3+CwiQCO24SvoUAhm/Oud9N8GJjYeL1oEivt5kt6uM15Gjg1wY6+R6AGlccJky7P8LDxgTF6iiBA6ogRvM/Zm6qud8fBugxzjLVLifW4Kwgdyyx0ViM+oLS02aVdwd07DO2c9LKUG25Sjt68ljo1oBtbCnp1l4D0ewrwMmKBf8Cmll9nkxr9HBvbxeNfJKcW8tq2wYpHoSBTlmOItZvpQ3r3LJ5bIJRHzrnt7x6qhB5NndMnvDiFJpdmW9Suca/b7KeGOGkBaKtw8Guk7FHy6n1ugvPf1rZ4lStShImcPhgR4/9HcZ7cPh0/oiXoubHpiM2Rgc8m0cbyXF6cb+jqE030OOCbLoUYndX3BJ4KmlOVNK8i+Ex14djdplmsqyJwxe+7lnTv2+UiK0QFfVLQnOx1u9LU/867xzJJoTkpBvrN2Gnu1KhKRmKFQe5JzbFOCJxdTaKMRyMRRERQ==',CURRENT_TIMESTAMP);
insert into TPSD_EMAIL(EMAIL)
    values('pchen@biospace.com');
insert into TPSD_USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    select USER_ID,EMAIL_ID,'1' from TPSD_USER, TPSD_EMAIL where USERNAME='pchen.biospace' and EMAIL='pchen@biospace.com';

-- Paul Nash (Corporate Counsel, CBL)
insert into TPSD_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD,CREATED_ON)
    values('pnash.cbl','IrgYfGO2Ark1JQF7zrBcXg==','What is the city where you were born?','p0B7MKs7hs21lMFBgHe/+g==','0','iHt2ZotgbGgfDUZmdRUWf2FWWSzqlCzVgQxyhqrBElNEaamLxMcfRX+Si3bcB/Wxgo8V5E3+CwiQCO24SvoUAhm/Oud9N8GJjYeL1oEivt5kt6uM15Gjg1wY6+R6AGlccJky7P8LDxgTF6iiBA6ogb5HmFp11XFp3lwYA3KoiWdcmnPNZ89IbfGA6vB0s8+p2ZL5tY1pAan1pOFzxQNhsBz+8WEUyrCikLFgVsdO1eKPFuYvfXwC9dxx1HPQxo+6A2ZNZ7o3hWBAQm3xBIA5wW+vSJgvfBVCDo+Rh5OBFwcFC+3AT4n4IZskJtdG7JYt10kFWFwPw+lER45tSm6/8tKDoCq2/MmQzeomPaMYzPzw45dT2WPDJNJOAJAWwkPeqlsAE9Lt2IXdZMyVs+sjjSYb5zEulryeqEWGnG28I2zIS1PLhMmfOdeZhck0Lt+XqGcOqa64leWC6pe0ElklN6jqTUBQC06VGD2Cm/DxHYpK9kNonm2wiQnFV3ZRUJSxsnt96mbm7e0S8gWKnemRAQ==',CURRENT_TIMESTAMP);
insert into TPSD_EMAIL(EMAIL)
    values('pnash@cbl.com');
insert into TPSD_USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    select USER_ID,EMAIL_ID,'1' from TPSD_USER, TPSD_EMAIL where USERNAME='pnash.cbl' and EMAIL='pnash@cbl.com';

-- Greg Johansson (Vice President, Sales Americas, CBL) 
insert into TPSD_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD,CREATED_ON)
    values('gjohansson.cbl','IrgYfGO2Ark1JQF7zrBcXg==','What is the city where you were born?','p0B7MKs7hs21lMFBgHe/+g==','0','iHt2ZotgbGgfDUZmdRUWf2FWWSzqlCzVgQxyhqrBElNEaamLxMcfRX+Si3bcB/Wxgo8V5E3+CwiQCO24SvoUAhm/Oud9N8GJjYeL1oEivt5kt6uM15Gjg1wY6+R6AGlccJky7P8LDxgTF6iiBA6ogUyTtDSvXZEb7M7ru0BEOhGG/0t61n7Vp++RGcWbMflLhkXaCMvdxl7kXCiV8wRkzkJPoaxkeEEDde9aNU20bR457NG3wVdcmHhqStmAIyrKd6hAeu61CwFPcCUxVChYQy0Q8XwIrtsw/P+GVKpraWx3sgTmTFL07IavI0q0vcLCPB0Kyf0T6y48p8mDqQ7or1vIL4tcdOrjQ5KQ7NY70VMavI9dKR2ozvN7LPqMpmIH/+x/4OHr8nklRML1SlJZxXLo0SmMOg1SQv4yvnupe1HGDJjmvfLHShIA+LaExh3veNqK2yye/UhDTs9T0LExyc9kTqIGS9ZXe3SYE/JZUR3UA0q+BOkRxa5Y2x6eQf3OJuw7V1Nqoaa5jgz3/pr1Qz0oioD5j5ed22sc4rhRn8Q=',CURRENT_TIMESTAMP);
insert into TPSD_EMAIL(EMAIL)
    values('gjohansson@cbl.com');
insert into TPSD_USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    select USER_ID,EMAIL_ID,'1' from TPSD_USER, TPSD_EMAIL where USERNAME='gjohansson.cbl' and EMAIL='gjohansson@cbl.com';

-- Cheryl Martin (Lawyer, Daylight Inc)
insert into TPSD_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD,CREATED_ON)
    values('cmartin.daylight','IrgYfGO2Ark1JQF7zrBcXg==','What is the city where you were born?','p0B7MKs7hs21lMFBgHe/+g==','0','iHt2ZotgbGgfDUZmdRUWf2FWWSzqlCzVgQxyhqrBElNEaamLxMcfRX+Si3bcB/Wxgo8V5E3+CwiQCO24SvoUAhm/Oud9N8GJjYeL1oEivt5kt6uM15Gjg1wY6+R6AGlccJky7P8LDxgTF6iiBA6ogcFApbaG6ngTF6LLcHWMsV2GjIeovp3RCpIooAcPk7sql1jBKiDhKDSpSCBGh5lMZ4ykGj8fvhd7w3/2L5+ujNuohQd3OObnuv9DrwVlw/tZ4qkdM9FvZI9uOVfwJhi3Pal9VrZqefBGBqjJ3W1YT3++e6kaEzvtWYv7jxVCZIwu33UaXTJPAbCh2vCamWoWY1G8EyP89alWWhULv96gxKIJoS4JSSHzeeFH/4uVbnGdcujRKYw6DVJC/jK+e6l7UcYMmOa98sdKEgD4toTGHe942orbLJ79SENOz1PQsTHJz2ROogZL1ld7dJgT8llRHdQDSr4E6RHFrljbHp5B/c4m7DtXU2qhprmODPf+mvVDPSiKgPmPl53baxziuFGfxA==',CURRENT_TIMESTAMP);
insert into TPSD_EMAIL(EMAIL)
    values('cmartin@daylight.com');
insert into TPSD_USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    select USER_ID,EMAIL_ID,'1' from TPSD_USER, TPSD_EMAIL where USERNAME='cmartin.daylight' and EMAIL='cmartin@daylight.com';

-- Rita Thomas (Vice President Services, AgileTech)
insert into TPSD_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD,CREATED_ON)
    values('rthomas.agiletech','IrgYfGO2Ark1JQF7zrBcXg==','What is the city where you were born?','p0B7MKs7hs21lMFBgHe/+g==','0','iHt2ZotgbGgfDUZmdRUWf2FWWSzqlCzVgQxyhqrBElNEaamLxMcfRX+Si3bcB/Wxgo8V5E3+CwiQCO24SvoUAhm/Oud9N8GJjYeL1oEivt5kt6uM15Gjg1wY6+R6AGlccJky7P8LDxgTF6iiBA6ogfnEgqgB2RuoOE13l8PoCOibEWAIvGs7v4kudGZ32K5Z5lT2bec/6JbvZRt2dOyCEowDbH2FWwHHdjVXOy78yDi6bz6Xxafad9aH+77y2R/NwHojjI3jzeO4EZesKBVfGNMhivhbmZuVwgiUr1nlR0vY1yUDHQ4eI0dpSWxo/pNnE4hD5JgkVxU7Afgzy4fwkRT21t1AqMmZm/8slckjjTdLIyiketEi6Rj83VyPEPphyK/iIy+xQIKpCnVqqVN+F7Ql5kJtJdMhtZRDwECN0EpH1TZmtlfJ2iiAS0sApH5j1rG3vbKyBsH2/VRrSBFvxwBQNBMrGPTWY9yWLXY4CoG01gdv/7j+V8Ec/yFMzd0j4kJt0A7w8zq0EOeE4PlPpfOjRYLFUbIJ1W05N6cxLI0=',CURRENT_TIMESTAMP);
insert into TPSD_EMAIL(EMAIL)
    values('rthomas@agiletech.com');
insert into TPSD_USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    select USER_ID,EMAIL_ID,'1' from TPSD_USER, TPSD_EMAIL where USERNAME='rthomas.agiletech' and EMAIL='rthomas@agiletech.com';

-- Keith Cooper (CFO, AgileTech)  
insert into TPSD_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD,CREATED_ON)
    values('kcooper.agiletech','IrgYfGO2Ark1JQF7zrBcXg==','What is the city where you were born?','p0B7MKs7hs21lMFBgHe/+g==','0','iHt2ZotgbGgfDUZmdRUWf2FWWSzqlCzVgQxyhqrBElNEaamLxMcfRX+Si3bcB/Wxgo8V5E3+CwiQCO24SvoUAhm/Oud9N8GJjYeL1oEivt5kt6uM15Gjg1wY6+R6AGlccJky7P8LDxgTF6iiBA6ogexpXBmNhiqgJJYGoiqpL+cxm6c+CEFd2CUlR4GixZQ71/AZI6D7vUIMXBWWj8olUwHHKVUwpPRPydjJwqI4+WdhZxzf2neizLxaGv1r+i5qRu+bteuig8PnR3OnUjEaVmGkDmqp+E8G7A2GCiKFDms4NUvzsyN+TzlWrz+GiOIFQDhlYloMiZEFFTUVZ20iqUmqnHHUqy4x3XyusCeDfQmWo2vj2c8s0u4qLMrgMTJQUjnNLtaHJUDmBM3jIZHC0SQElX9/9sbQxS5fHG60BGra5cth5aOmwALSXgHQwHeyREyODRtDsLAcQNKgZ2z9RmFWWSzqlCzVgQxyhqrBElOaMTN3x7JR7aTpAw35Ex6V',CURRENT_TIMESTAMP);
insert into TPSD_EMAIL(EMAIL)
    values('kcooper@agiletech.com');
insert into TPSD_USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    select USER_ID,EMAIL_ID,'1' from TPSD_USER, TPSD_EMAIL where USERNAME='kcooper.agiletech' and EMAIL='kcooper@agiletech.com';

-- All users get all features
insert into TPSD_USER_FEATURE_REL(USER_ID,FEATURE_ID)
    select USER_ID,FEATURE_ID from TPSD_USER,TPSD_PRODUCT_FEATURE;
delete from TPSD_USER_FEATURE_REL where USER_ID in (select USER_ID from TPSD_USER where USERNAME='thinkparity' or USERNAME='backup');

-- All users get demo release
insert into TPSD_USER_PRODUCT_RELEASE_REL(USER_ID,PRODUCT_ID,RELEASE_ID)
    select USER_ID,PR.PRODUCT_ID,RELEASE_ID from TPSD_USER,TPSD_PRODUCT P inner join TPSD_PRODUCT_RELEASE PR on PR.PRODUCT_ID=P.PRODUCT_ID where PRODUCT_NAME='OpheliaProduct' and RELEASE_NAME='DEMO';
delete from TPSD_USER_PRODUCT_RELEASE_REL where USER_ID in (select USER_ID from TPSD_USER where USERNAME='thinkparity' or USERNAME='backup');
delete from TPSD_USER_PRODUCT_RELEASE_REL where RELEASE_ID in (select RELEASE_ID from TPSD_PRODUCT_RELEASE where RELEASE_OS <> 'WINDOWS_XP');

-- All users are contacts of each other
insert into TPSD_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    select U.USER_ID,C.USER_ID,U.USER_ID,CURRENT_TIMESTAMP,U.USER_ID,CURRENT_TIMESTAMP from TPSD_USER U, TPSD_USER C;
delete from TPSD_CONTACT where CONTACT_ID in (select USER_ID from TPSD_USER where USERNAME='thinkparity' or USERNAME='backup');
delete from TPSD_CONTACT where USER_ID = CONTACT_ID;

-- <com.thinkparity.codebase.model.user.UserVCard><city>Vancouver</city><country>CAN</country><language>eng</language><name>George Johnson</name><organization>ServiceSoft Inc</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>CFO</title><postalCode>A1A Z9Z</postalCode><province>BA</province></com.thinkparity.codebase.model.user.UserVCard>
-- <com.thinkparity.codebase.model.user.UserVCard><city>Vancouver</city><country>CAN</country><language>eng</language><name>Robert Bay</name><organization>ServiceSoft Inc</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>CEO</title><postalCode>A1A Z9Z</postalCode><province>BA</province></com.thinkparity.codebase.model.user.UserVCard>
-- <com.thinkparity.codebase.model.user.UserVCard><city>Vancouver</city><country>CAN</country><language>eng</language><name>Rick Wales</name><organization>Digital Solutions</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>CEO</title><postalCode>A1A Z9Z</postalCode><province>BA</province></com.thinkparity.codebase.model.user.UserVCard>
-- <com.thinkparity.codebase.model.user.UserVCard><city>Vancouver</city><country>CAN</country><language>eng</language><name>Paul Stevenson</name><organization>OnCorp Inc</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>CEO</title><postalCode>A1A Z9Z</postalCode><province>BA</province></com.thinkparity.codebase.model.user.UserVCard>
-- <com.thinkparity.codebase.model.user.UserVCard><city>Vancouver</city><country>CAN</country><language>eng</language><name>Susan Race</name><organization>SCI Solutions</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Vice President, Services</title><postalCode>A1A Z9Z</postalCode><province>BA</province></com.thinkparity.codebase.model.user.UserVCard>
-- <com.thinkparity.codebase.model.user.UserVCard><city>Vancouver</city><country>CAN</country><language>eng</language><name>Craig Clare</name><organization>SCI Solutions</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>CFO</title><postalCode>A1A Z9Z</postalCode><province>BA</province></com.thinkparity.codebase.model.user.UserVCard>
-- <com.thinkparity.codebase.model.user.UserVCard><city>Vancouver</city><country>CAN</country><language>eng</language><name>Simon Bolin</name><organization>SCI Solutions</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Vice President, Marketing</title><postalCode>A1A Z9Z</postalCode><province>BA</province></com.thinkparity.codebase.model.user.UserVCard>
-- <com.thinkparity.codebase.model.user.UserVCard><city>Vancouver</city><country>CAN</country><language>eng</language><name>Jennifer Stone</name><organization>BioSpace Inc</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Vice President, Marketing</title><postalCode>A1A Z9Z</postalCode><province>BA</province></com.thinkparity.codebase.model.user.UserVCard>
-- <com.thinkparity.codebase.model.user.UserVCard><city>Vancouver</city><country>CAN</country><language>eng</language><name>Mark Francis</name><organization>BioSpace Inc</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>CFO</title><postalCode>A1A Z9Z</postalCode><province>BA</province></com.thinkparity.codebase.model.user.UserVCard>
-- <com.thinkparity.codebase.model.user.UserVCard><city>Vancouver</city><country>CAN</country><language>eng</language><name>Paul Nash</name><organization>CBL</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Corporate Counsel</title><postalCode>A1A Z9Z</postalCode><province>BA</province></com.thinkparity.codebase.model.user.UserVCard>
-- <com.thinkparity.codebase.model.user.UserVCard><city>Vancouver</city><country>CAN</country><language>eng</language><name>Cheryl Martin</name><organization>Daylight Inc</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Lawyer</title><postalCode>A1A Z9Z</postalCode><province>BA</province></com.thinkparity.codebase.model.user.UserVCard>
-- <com.thinkparity.codebase.model.user.UserVCard><city>Vancouver</city><country>CAN</country><language>eng</language><name>Rita Thomas</name><organization>AgileTech</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Vice President, Services</title><postalCode>A1A Z9Z</postalCode><province>BA</province></com.thinkparity.codebase.model.user.UserVCard>
-- <com.thinkparity.codebase.model.user.UserVCard><city>Vancouver</city><country>CAN</country><language>eng</language><name>Keith Cooper</name><organization>AgileTech</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>CFO</title><postalCode>A1A Z9Z</postalCode><province>BA</province></com.thinkparity.codebase.model.user.UserVCard>
-- <com.thinkparity.codebase.model.user.UserVCard><city>Vancouver</city><country>CAN</country><language>eng</language><name>David Stevenson</name><organization>SCI Solutions</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>President</title><postalCode>A1A Z9Z</postalCode><province>BA</province></com.thinkparity.codebase.model.user.UserVCard>
-- <com.thinkparity.codebase.model.user.UserVCard><city>Vancouver</city><country>CAN</country><language>eng</language><name>Peter Chen</name><organization>BioSpace Inc</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Controller</title><postalCode>A1A Z9Z</postalCode><province>BA</province></com.thinkparity.codebase.model.user.UserVCard>
-- <com.thinkparity.codebase.model.user.UserVCard><city>Vancouver</city><country>CAN</country><language>eng</language><name>John Bruce</name><organization>ServiceSoft Inc</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Director, Human Resources</title><postalCode>A1A Z9Z</postalCode><province>BA</province></com.thinkparity.codebase.model.user.UserVCard>
-- <com.thinkparity.codebase.model.user.UserVCard><city>Vancouver</city><country>CAN</country><language>eng</language><name>Greg Johansson</name><organization>CBL</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Vice President, Sales Americas</title><postalCode>A1A Z9Z</postalCode><province>BA</province></com.thinkparity.codebase.model.user.UserVCard>
-- <com.thinkparity.codebase.model.user.UserVCard><address>2965 Hourbour St.</address><city>Boston</city><country>USA</country><language>eng</language><mobilePhone>617.478.5937</mobilePhone><name>Sean Hogan</name><organization>MacKay &amp; Associates</organization><organizationCountry>USA</organizationCountry><phone>617.854.8900</phone><timeZone>America/Vancouver</timeZone><title>Lawyer</title><postalCode>02111</postalCode><province>MA</province></com.thinkparity.codebase.model.user.UserVCard>
