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

insert into PARITY_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('thinkparity','parity','What is my username?','thinkparity',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>thinkParity Services Inc.</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into EMAIL(EMAIL)
    values('thinkparity@thinkparity.com');
insert into USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    values(7000,5000,true);

insert into PRODUCT(PRODUCT_NAME) values('OpheliaProduct');
insert into PRODUCT(PRODUCT_NAME) values('DesdemonaProduct');
insert into PRODUCT_FEATURE(PRODUCT_ID,FEATURE) values(1000,'CORE');
insert into PRODUCT_FEATURE(PRODUCT_ID,FEATURE) values(1000,'BACKUP');

insert into PARITY_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('junit','parity','What is my username?','junit',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>JUnit thinkParity</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into EMAIL(EMAIL)
    values('junit@thinkparity.com');
insert into USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    values(7001,5001,true);
insert into USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7001,6000);

insert into PARITY_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('junit.w','parity','What is my username?','junit.w',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>JUnit.W thinkParity</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into EMAIL(EMAIL)
    values('junit.w@thinkparity.com');
insert into USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    values(7002,5002,true);
insert into USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7002,6000);

insert into PARITY_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('junit.x','parity','What is my username?','junit.x',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>JUnit.X thinkParity</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into EMAIL(EMAIL)
    values('junit.x@thinkparity.com');
insert into USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    values(7003,5003,true);
insert into USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7003,6000);

insert into PARITY_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('junit.y','parity','What is my username?','junit.y',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>JUnit.Y thinkParity</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into EMAIL(EMAIL)
    values('junit.y@thinkparity.com');
insert into USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    values(7004,5004,true);
insert into USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7004,6000);

insert into PARITY_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('junit.z','parity','What is my username?','junit.z',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>JUnit.Z thinkParity</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into EMAIL(EMAIL)
    values('junit.z@thinkparity.com');
insert into USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    values(7005,5005,true);
insert into USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7005,6000);
insert into USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7005,6001);

insert into PARITY_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('junit.z-backup','parity','What is my username?','junit.z-archive',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>JUnit.Z thinkParity Archive</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into USER_BACKUP_REL(USER_ID,BACKUP_ID) 
    values(7005,7006);

insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON) 
    values(7001,7003,7001,NOW(),7001,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON) 
    values(7001,7004,7001,NOW(),7001,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON) 
    values(7001,7005,7001,NOW(),7001,NOW());

insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON) 
    values(7003,7001,7003,NOW(),7003,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON) 
    values(7003,7004,7003,NOW(),7003,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON) 
    values(7003,7005,7003,NOW(),7003,NOW());

insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON) 
    values(7004,7001,7004,NOW(),7004,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON) 
    values(7004,7003,7004,NOW(),7004,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON) 
    values(7004,7005,7004,NOW(),7004,NOW());

insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON) 
    values(7005,7001,7005,NOW(),7005,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON) 
    values(7005,7003,7005,NOW(),7005,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON) 
    values(7005,7004,7005,NOW(),7005,NOW());
