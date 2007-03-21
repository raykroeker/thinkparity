insert into jiveProperty(NAME,PROPVALUE) values('xmpp.auth.anonymous','true');
insert into jiveProperty(NAME,PROPVALUE) values('xmpp.domain','thinkparity.net');
insert into jiveProperty(NAME,PROPVALUE) values('xmpp.server.socket.port','5269');
insert into jiveProperty(NAME,PROPVALUE) values('xmpp.socket.plain.port','5222');
insert into jiveProperty(NAME,PROPVALUE) values('xmpp.socket.ssl.active','true');
insert into jiveProperty(NAME,PROPVALUE) values('xmpp.socket.ssl.keypass','password');
insert into jiveProperty(NAME,PROPVALUE) values('xmpp.socket.ssl.port','5223');
insert into jiveProperty(NAME,PROPVALUE) values('thinkparity.environment','PRODUCTION');
insert into jiveProperty(NAME,PROPVALUE) values('thinkparity.mode','TESTING');
insert into jiveProperty(NAME,PROPVALUE) values('thinkparity.product-name','DesdemonaProduct');
insert into jiveProperty(NAME,PROPVALUE) values('thinkparity.release-name','20070308');
insert into jiveProperty(NAME,PROPVALUE) values('thinkparity.migrator.logerror.notify','true');
insert into jiveProperty(NAME,PROPVALUE) values('thinkparity.migrator.logerror.notify.to','raymond+fatal-error@thinkparity.com');
insert into jiveProperty(NAME,PROPVALUE) values('thinkparity.migrator.logerror.notify.cc','omid+fatal-error@thinkparity.com');

insert into PARITY_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('thinkparity','parity','What is my username?','thinkparity',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>thinkParity Services Inc.</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into EMAIL(EMAIL)
    values('thinkparity@thinkparity.com');
insert into USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    values(7000,5000,true);

insert into PRODUCT(PRODUCT_NAME) values('OpheliaProduct');
insert into PRODUCT(PRODUCT_NAME) values('DesdemonaProduct');
insert into PRODUCT_FEATURE(PRODUCT_ID,FEATURE_NAME) values(1000,'CORE');
insert into PRODUCT_FEATURE(PRODUCT_ID,FEATURE_NAME) values(1000,'BACKUP');

insert into PARITY_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('asahebjam','parity','What is my username?','asahebjam',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Amir Sahebjam</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into EMAIL(EMAIL)
    values('asahebjam@gmail.com');
insert into USER_EMAIL(USER_ID,EMAIL,VERIFIED)
    values(7001,5001,true);
insert into USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7001,6000);

insert into PARITY_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('omid','parity','What is my username?','omid',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Omid Ejtemai</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into EMAIL(EMAIL)
    values('omid@thinkparity.com');
insert into USER_EMAIL(USER_ID,EMAIL,VERIFIED)
    values(7002,5002,true);
insert into USER_FEATURE_REL(USER_ID,FEATURE_ID)
	values(7002,6000);
insert into USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7002,6001);

insert into PARITY_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('omid-backup','parity','What is my username?','omid-backup',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Omid Ejtemai Archive</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into USER_BACKUP_REL(USER_ID,BACKUP_ID)
    values(7002,7003);

insert into PARITY_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('rkroeker','parity','What is my username?','rkroeker',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Raymond Kroeker</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into EMAIL(EMAIL)
    values('raymond@thinkparity.com');
insert into USER_EMAIL(USER_ID,EMAIL,VERIFIED)
    values(7004,5003,true);
insert into USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7004,6000);

insert into PARITY_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('robert','parity','What is my username?','robert',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Robert MacMartin</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into EMAIL(EMAIL)
    values('robert@thinkparity.com');
insert into USER_EMAIL(USER_ID,EMAIL,VERIFIED)
    values(7005,5004,true);
insert into USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7005,6000);

insert into PARITY_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('djohnson','parity','What is my username?','djohnson',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Don Johnson</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into EMAIL(EMAIL)
    values('djohnson@thinkparity.com');
insert into USER_EMAIL(USER_ID,EMAIL,VERIFIED)
    values(7006,5005,true);
insert into USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7006,6000);

insert into PARITY_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('emorrison','parity','What is my username?','emorrison',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Emily Morrison</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into EMAIL(EMAIL)
    values('emorrison@thinkparity.com');
insert into USER_EMAIL(USER_ID,EMAIL,VERIFIED)
    values(7007,5006,true);
insert into USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7007,6000);

insert into PARITY_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('kmfarland','parity','What is my username?','kmfarland',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Kevin MacFarland</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into EMAIL(EMAIL)
    values('kmfarland@thinkparity.com');
insert into USER_EMAIL(USER_ID,EMAIL,VERIFIED)
    values(7008,5007,true);
insert into USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7008,6000);

insert into PARITY_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('strimnell','parity','What is my username?','strimnell',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Sarah Trimnell</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into EMAIL(EMAIL)
    values('strimnell@thinkparity.com');
insert into USER_EMAIL(USER_ID,EMAIL,VERIFIED)
    values(7009,5008,true);
insert into USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7009,6000);

insert into PARITY_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('kenjiro','parity','What is my username?','kenjiro',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Kenjiro MacMartin</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into EMAIL(EMAIL)
    values('kenjiro@thinkparity.com');
insert into USER_EMAIL(USER_ID,EMAIL,VERIFIED)
    values(7010,5009,true);
insert into USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7010,6000);

insert into PARITY_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('masako','parity','What is my username?','masako',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Masako Saito</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into EMAIL(EMAIL)
    values('masako@thinkparity.com');
insert into USER_EMAIL(USER_ID,EMAIL,VERIFIED)
    values(7011,5010,true);
insert into USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7011,6000);

insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7001,7002,7001,NOW(),7001,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7001,7004,7001,NOW(),7001,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7001,7005,7001,NOW(),7001,NOW());

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
    values(7011,7010,7011,NOW(),7011,NOW());
