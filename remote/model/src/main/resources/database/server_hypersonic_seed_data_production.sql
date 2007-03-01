insert into jiveProperty (NAME,PROPVALUE)
    values('xmpp.auth.anonymous','true');
insert into jiveProperty (NAME,PROPVALUE)
    values('xmpp.domain','thinkparity.net');
insert into jiveProperty (NAME,PROPVALUE)
    values('xmpp.server.socket.port','5269');
insert into jiveProperty (NAME,PROPVALUE)
    values('xmpp.socket.plain.port','5222');
insert into jiveProperty (NAME,PROPVALUE)
    values('xmpp.socket.ssl.active','true');
insert into jiveProperty (NAME,PROPVALUE)
    values('xmpp.socket.ssl.keypass','password');
insert into jiveProperty (NAME,PROPVALUE)
    values('xmpp.socket.ssl.port','5223');
insert into jiveProperty (NAME,PROPVALUE)
    values('thinkparity.environment','PRODUCTION');
insert into jiveProperty (NAME,PROPVALUE)
    values('thinkparity.mode','TESTING');

insert into jiveUser(USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('thinkparity','parity',0,0);
insert into PARITY_USER(USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('thinkparity','What is my username?','thinkparity',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>thinkParity Services Inc.</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into USER_EMAIL(USER_ID,EMAIL,VERIFIED)
    values(7000,'thinkParity@thinkparity.com',true);

insert into PRODUCT(PRODUCT_NAME) values('OpheliaProduct');
insert into PRODUCT_FEATURE(PRODUCT_ID,FEATURE) values(1000,'CORE');
insert into PRODUCT_FEATURE(PRODUCT_ID,FEATURE) values(1000,'BACKUP');

insert into jiveUser(USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('asahebjam','parity',0,0);
insert into PARITY_USER(USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('asahebjam','What is my username?','asahebjam',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Amir Sahebjam</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into USER_EMAIL(USER_ID,EMAIL,VERIFIED)
    values(7001,'asahebjam@gmail.com',true);
insert into USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7001,5000);

insert into jiveUser(USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('omid','parity',0,0);
insert into PARITY_USER(USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('omid','What is my username?','omid',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Omid Ejtemai</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into USER_EMAIL(USER_ID,EMAIL,VERIFIED)
    values(7002,'omid@thinkparity.com',true);
insert into USER_FEATURE_REL(USER_ID,FEATURE_ID)
	values(7002,5000);
insert into USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7002,5001);

insert into jiveUser(USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('omid-archive','parity',0,0);
insert into PARITY_USER(USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('omid-archive','What is my username?','omid-archive',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Omid Ejtemai Archive</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into USER_BACKUP_REL(USER_ID,BACKUP_ID)
    values(7002,7003);

insert into jiveUser(USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('rkroeker','parity',0,0);
insert into PARITY_USER(USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('rkroeker','What is my username?','rkroeker',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Raymond Kroeker</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into USER_EMAIL(USER_ID,EMAIL,VERIFIED)
    values(7004,'raymond@thinkparity.com',true);
insert into USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7004,5000);

insert into jiveUser(USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('robert','parity',0,0);
insert into PARITY_USER(USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('robert','What is my username?','robert',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Robert MacMartin</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into USER_EMAIL(USER_ID,EMAIL,VERIFIED)
    values(7005,'robert@thinkparity.com',true);
insert into USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7005,5000);

insert into jiveUser(USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('djohnson','parity',0,0);
insert into PARITY_USER(USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('djohnson','What is my username?','djohnson',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Don Johnson</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into USER_EMAIL(USER_ID,EMAIL,VERIFIED)
    values(7006,'djohnson@thinkparity.com',true);
insert into USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7006,5000);

insert into jiveUser(USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('emorrison','parity',0,0);
insert into PARITY_USER(USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('emorrison','What is my username?','emorrison',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Emily Morrison</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into USER_EMAIL(USER_ID,EMAIL,VERIFIED)
    values(7007,'emorrison@thinkparity.com',true);
insert into USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7007,5000);

insert into jiveUser(USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('kmfarland','parity',0,0);
insert into PARITY_USER(USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('kmfarland','What is my username?','kmfarland',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Kevin MacFarland</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into USER_EMAIL(USER_ID,EMAIL,VERIFIED)
    values(7008,'kmfarland@thinkparity.com',true);
insert into USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7008,5000);

insert into jiveUser(USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('strimnell','parity',0,0);
insert into PARITY_USER(USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('strimnell','What is my username?','strimnell',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Sarah Trimnell</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into USER_EMAIL(USER_ID,EMAIL,VERIFIED)
    values(7009,'strimnell@thinkparity.com',true);
insert into USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7009,5000);

insert into jiveUser(USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('kenjiro','parity',0,0);
insert into PARITY_USER(USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('kenjiro','What is my username?','kenjiro',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Kenjiro MacMartin</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into USER_EMAIL(USER_ID,EMAIL,VERIFIED)
    values(7010,'kenjiro@thinkparity.com',true);
insert into USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7010,5000);

insert into jiveUser(USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('masako','parity',0,0);
insert into PARITY_USER(USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('masako','What is my username?','masako',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Masako Saito</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into USER_EMAIL(USER_ID,EMAIL,VERIFIED)
    values(7011,'masako@thinkparity.com',true);
insert into USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7011,5000);

insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values('asahebjam','omid@thinkparity.net','asahebjam',NOW(),'asahebjam',NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values('asahebjam','rkroeker@thinkparity.net','asahebjam',NOW(),'asahebjam',NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values('asahebjam','robert@thinkparity.net','asahebjam',NOW(),'asahebjam',NOW());

insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7002,7001,7002,NOW(),7002,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7002,7004,7002,NOW(),7002,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7002,7005,7002,NOW(),7002,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7002,7006,7002,NOW(),7002,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7002,7007,7002,NOW(),7002,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7002,7009,7002,NOW(),7002,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7002,7009,7002,NOW(),7002,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7002,7010,7002,NOW(),7002,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7002,7011,7002,NOW(),7002,NOW());

insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7004,7001,7004,NOW(),7004,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7004,7002,7004,NOW(),7004,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7004,7005,7004,NOW(),7004,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7004,7006,7004,NOW(),7004,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7004,7007,7004,NOW(),7004,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7004,7009,7004,NOW(),7004,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7004,7009,7004,NOW(),7004,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7004,7010,7004,NOW(),7004,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7004,7011,7004,NOW(),7004,NOW());

insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7005,7001,7005,NOW(),7005,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7005,7002,7005,NOW(),7005,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7005,7004,7005,NOW(),7005,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7005,7006,7005,NOW(),7005,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7005,7007,7005,NOW(),7005,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7005,7009,7005,NOW(),7005,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7005,7009,7005,NOW(),7005,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7005,7010,7005,NOW(),7005,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7005,7011,7005,NOW(),7005,NOW());

insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7006,7002,7006,NOW(),7006,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7006,7004,7006,NOW(),7006,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7006,7005,7006,NOW(),7006,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7006,7007,7006,NOW(),7006,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7006,7009,7006,NOW(),7006,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7006,7009,7006,NOW(),7006,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7006,7010,7006,NOW(),7006,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7006,7011,7006,NOW(),7006,NOW());

insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7007,7002,7007,NOW(),7007,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7007,7004,7007,NOW(),7007,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7007,7005,7007,NOW(),7007,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7007,7006,7007,NOW(),7007,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7007,7009,7007,NOW(),7007,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7007,7009,7007,NOW(),7007,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7007,7010,7007,NOW(),7007,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7007,7011,7007,NOW(),7007,NOW());

insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7009,7002,7009,NOW(),7009,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7009,7004,7009,NOW(),7009,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7009,7005,7009,NOW(),7009,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7009,7006,7009,NOW(),7009,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7009,7007,7009,NOW(),7009,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7009,7009,7009,NOW(),7009,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7009,7010,7009,NOW(),7009,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7009,7011,7009,NOW(),7009,NOW());

insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7009,7002,7009,NOW(),7009,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7009,7004,7009,NOW(),7009,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7009,7005,7009,NOW(),7009,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7009,7006,7009,NOW(),7009,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7009,7007,7009,NOW(),7009,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7009,7009,7009,NOW(),7009,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7009,7010,7009,NOW(),7009,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7009,7011,7009,NOW(),7009,NOW());

insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7010,7002,7010,NOW(),7010,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7010,7004,7010,NOW(),7010,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7010,7005,7010,NOW(),7010,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7010,7006,7010,NOW(),7010,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7010,7007,7010,NOW(),7010,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7010,7009,7010,NOW(),7010,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7010,7009,7010,NOW(),7010,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7010,7011,7010,NOW(),7010,NOW());

insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7011,7002,7011,NOW(),7011,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7011,7004,7011,NOW(),7011,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7011,7005,7011,NOW(),7011,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7011,7006,7011,NOW(),7011,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7011,7007,7011,NOW(),7011,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7011,7009,7011,NOW(),7011,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7011,7009,7011,NOW(),7011,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7011,7010,7011,NOW(),7011,NOW());
