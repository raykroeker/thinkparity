insert into jiveProperty(NAME,PROPVALUE) values('xmpp.auth.anonymous','true');
insert into jiveProperty(NAME,PROPVALUE) values('xmpp.domain','thinkparity.net');
insert into jiveProperty(NAME,PROPVALUE) values('xmpp.server.socket.port','5270');
insert into jiveProperty(NAME,PROPVALUE) values('xmpp.socket.plain.port','5224');
insert into jiveProperty(NAME,PROPVALUE) values('xmpp.socket.ssl.active','true');
insert into jiveProperty(NAME,PROPVALUE) values('xmpp.socket.ssl.keypass','password');
insert into jiveProperty(NAME,PROPVALUE) values('xmpp.socket.ssl.port','5225');
insert into jiveProperty(NAME,PROPVALUE) values('thinkparity.environment','TESTING_LOCALHOST');
insert into jiveProperty(NAME,PROPVALUE) values('thinkparity.mode','TESTING');
insert into jiveProperty(NAME,PROPVALUE) values('thinkparity.product-name','DesdemonaProduct');
insert into jiveProperty(NAME,PROPVALUE) values('thinkparity.release-name','20070308');
insert into jiveProperty(NAME,PROPVALUE) values('thinkparity.migrator.logerror.notify','false');

insert into TPSD_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('thinkparity','parity','What is my username?','thinkparity','0','<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>thinkParity Services Inc.</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into TPSD_EMAIL(EMAIL)
    values('thinkparity@thinkparity.com');
insert into TPSD_USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    values(7000,5000,'1');

insert into TPSD_PRODUCT(PRODUCT_NAME) values('OpheliaProduct');
insert into TPSD_PRODUCT(PRODUCT_NAME) values('DesdemonaProduct');
insert into TPSD_PRODUCT_RELEASE(PRODUCT_ID,RELEASE_NAME,RELEASE_OS,RELEASE_DATE)
    values(1000,'TESTING','LINUX',CURRENT_TIMESTAMP);
insert into TPSD_PRODUCT_RELEASE(PRODUCT_ID,RELEASE_NAME,RELEASE_OS,RELEASE_DATE)
    values(1000,'TESTING','WINDOWS_XP',CURRENT_TIMESTAMP);
insert into TPSD_PRODUCT_FEATURE(PRODUCT_ID,FEATURE_NAME) values(1000,'CORE');
insert into TPSD_PRODUCT_FEATURE(PRODUCT_ID,FEATURE_NAME) values(1000,'BACKUP');

insert into TPSD_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('thinkparity-backup','parity','What is my username?','thinkparity-backup','0','<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>thinkParity Software</name><organization>thinkParity Solutions Inc.</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into TPSD_EMAIL(EMAIL)
    values('thinkparity+backup@thinkparity.com');
insert into TPSD_USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    values(7001,5001,'1');

insert into TPSD_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('thinkparity-support','parity','What is my username?','thinkparity-support','0','<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>thinkParity Software</name><organization>thinkParity Solutions Inc.</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into TPSD_EMAIL(EMAIL)
    values('thinkparity+support@thinkparity.com');
insert into TPSD_USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    values(7002,5002,'1');
insert into TPSD_USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7002,6000);
insert into TPSD_USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7002,6001);

insert into TPSD_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('junit','parity','What is my username?','junit','0','<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>JUnit thinkParity</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into TPSD_EMAIL(EMAIL)
    values('junit@thinkparity.com');
insert into TPSD_USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    values(7003,5003,'1');
insert into TPSD_USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7003,6000);

insert into TPSD_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('junit.w','parity','What is my username?','junit.w','0','<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>JUnit.W thinkParity</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into TPSD_EMAIL(EMAIL)
    values('junit.w@thinkparity.com');
insert into TPSD_USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    values(7004,5004,'1');
insert into TPSD_USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7004,6000);

insert into TPSD_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('junit.x','parity','What is my username?','junit.x','0','<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>JUnit.X thinkParity</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into TPSD_EMAIL(EMAIL)
    values('junit.x@thinkparity.com');
insert into TPSD_USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    values(7005,5005,'1');
insert into TPSD_USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7005,6000);

insert into TPSD_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('junit.y','parity','What is my username?','junit.y','0','<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>JUnit.Y thinkParity</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into TPSD_EMAIL(EMAIL)
    values('junit.y@thinkparity.com');
insert into TPSD_USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    values(7006,5006,'1');
insert into TPSD_USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7006,6000);

insert into TPSD_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('junit.z','parity','What is my username?','junit.z','0','<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>JUnit.Z thinkParity</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into TPSD_EMAIL(EMAIL)
    values('junit.z@thinkparity.com');
insert into TPSD_USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    values(7007,5007,'1');
insert into TPSD_USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7007,6000);
insert into TPSD_USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7007,6001);

insert into TPSD_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7002,7003,7000,CURRENT_TIMESTAMP,7000,CURRENT_TIMESTAMP);
insert into TPSD_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7002,7004,7000,CURRENT_TIMESTAMP,7000,CURRENT_TIMESTAMP);
insert into TPSD_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7002,7005,7000,CURRENT_TIMESTAMP,7000,CURRENT_TIMESTAMP);
insert into TPSD_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7002,7006,7000,CURRENT_TIMESTAMP,7000,CURRENT_TIMESTAMP);
insert into TPSD_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7002,7007,7000,CURRENT_TIMESTAMP,7000,CURRENT_TIMESTAMP);

insert into TPSD_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7003,7002,7000,CURRENT_TIMESTAMP,7000,CURRENT_TIMESTAMP);
insert into TPSD_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7003,7005,7000,CURRENT_TIMESTAMP,7000,CURRENT_TIMESTAMP);
insert into TPSD_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7003,7006,7000,CURRENT_TIMESTAMP,7000,CURRENT_TIMESTAMP);
insert into TPSD_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7003,7007,7000,CURRENT_TIMESTAMP,7000,CURRENT_TIMESTAMP);

insert into TPSD_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7004,7002,7000,CURRENT_TIMESTAMP,7000,CURRENT_TIMESTAMP);

insert into TPSD_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7005,7002,7000,CURRENT_TIMESTAMP,7000,CURRENT_TIMESTAMP);
insert into TPSD_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7005,7003,7000,CURRENT_TIMESTAMP,7000,CURRENT_TIMESTAMP);
insert into TPSD_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7005,7006,7000,CURRENT_TIMESTAMP,7000,CURRENT_TIMESTAMP);
insert into TPSD_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7005,7007,7000,CURRENT_TIMESTAMP,7000,CURRENT_TIMESTAMP);

insert into TPSD_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7006,7002,7000,CURRENT_TIMESTAMP,7000,CURRENT_TIMESTAMP);
insert into TPSD_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7006,7003,7000,CURRENT_TIMESTAMP,7000,CURRENT_TIMESTAMP);
insert into TPSD_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7006,7005,7000,CURRENT_TIMESTAMP,7000,CURRENT_TIMESTAMP);
insert into TPSD_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7006,7007,7000,CURRENT_TIMESTAMP,7000,CURRENT_TIMESTAMP);

insert into TPSD_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7007,7002,7000,CURRENT_TIMESTAMP,7000,CURRENT_TIMESTAMP);
insert into TPSD_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7007,7003,7000,CURRENT_TIMESTAMP,7000,CURRENT_TIMESTAMP);
insert into TPSD_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7007,7005,7000,CURRENT_TIMESTAMP,7000,CURRENT_TIMESTAMP);
insert into TPSD_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7007,7006,7000,CURRENT_TIMESTAMP,7000,CURRENT_TIMESTAMP);
