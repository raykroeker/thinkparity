insert into jiveProperty(NAME,PROPVALUE) values('xmpp.auth.anonymous','true');
insert into jiveProperty(NAME,PROPVALUE) values('xmpp.domain','thinkparity.net');
insert into jiveProperty(NAME,PROPVALUE) values('xmpp.server.socket.port','5272');
insert into jiveProperty(NAME,PROPVALUE) values('xmpp.socket.plain.port','5228');
insert into jiveProperty(NAME,PROPVALUE) values('xmpp.socket.ssl.active','true');
insert into jiveProperty(NAME,PROPVALUE) values('xmpp.socket.ssl.keypass','password');
insert into jiveProperty(NAME,PROPVALUE) values('xmpp.socket.ssl.port','5229');
insert into jiveProperty(NAME,PROPVALUE) values('thinkparity.environment','DEVELOPMENT_ROBERT');
insert into jiveProperty(NAME,PROPVALUE) values('thinkparity.mode','DEVELOPMENT');
insert into jiveProperty(NAME,PROPVALUE) values('thinkparity.product-name','DesdemonaProduct');
insert into jiveProperty(NAME,PROPVALUE) values('thinkparity.release-name','20070308');

insert into PARITY_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('thinkparity','parity','What is my username?','thinkparity',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>thinkParity Services Inc.</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into EMAIL(EMAIL)
    values('thinkparity@thinkparity.com');
insert into USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    values(7000,5000,true);

insert into PRODUCT(PRODUCT_NAME) values('OpheliaProduct');
insert into PRODUCT_FEATURE(PRODUCT_ID,FEATURE_NAME) values(1000,'CORE');
insert into PRODUCT_FEATURE(PRODUCT_ID,FEATURE_NAME) values(1000,'BACKUP');

insert into PARITY_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('asahebjam','parity','What is my username?','asahebjam',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Amir Sahebjam</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into EMAIL(EMAIL)
    values('asahebjam@gmail.com');
insert into USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    values(7001,5001,true);
insert into USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7001,6000);

insert into PARITY_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('omid','parity','What is my username?','omid',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Omid Ejtemai</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into EMAIL(EMAIL)
    values('omid@thinkparity.com');
insert into USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    values(7002,5002,true);
insert into USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7002,6000);

insert into PARITY_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('rkroeker','parity','What is my username?','rkroeker',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Raymond Kroeker</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into EMAIL(EMAIL)
    values('raymond@thinkparity.com');
insert into USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    values(7003,5003,true);
insert into USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7003,6000);
insert into USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7003,6001);

insert into PARITY_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('rkroeker-backup','parity','What is my username?','rkroeker-backup',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Raymond Kroeker Archive</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into USER_BACKUP_REL(USER_ID,BACKUP_ID)
    values(7003,7004);

insert into PARITY_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('robert','parity','What is my username?','robert',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Robert MacMartin</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into EMAIL(EMAIL)
    values('robert@thinkparity.com');
insert into USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    values(7005,5004,true);
insert into USER_FEATURE_REL(USER_ID,FEATURE_ID)
	values(7005,6000);
insert into USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7005,6001);

insert into PARITY_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('robert-backup','parity','What is my username?','robert-backup',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Robert MacMartin Archive</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into USER_BACKUP_REL(USER_ID,BACKUP_ID)
    values(7005,7006);

insert into PARITY_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('djohnson','parity','What is my username?','djohnson',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Don Johnson</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into EMAIL(EMAIL)
    values('djohnson@thinkparity.com');
insert into USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    values(7007,5005,true);

insert into PARITY_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('emorrison','parity','What is my username?','emorrison',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Emily Morrison</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into EMAIL(EMAIL)
    values('emorrison@thinkparity.com');
insert into USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    values(7008,5006,true);
insert into USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7008,6000);

insert into PARITY_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('kmfarland','parity','What is my username?','kmfarland',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Kevin MacFarland</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into EMAIL(EMAIL)
    values('kmfarland@thinkparity.com');
insert into USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    values(7009,5007,true);
insert into USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7009,6000);

insert into PARITY_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('strimnell','parity','What is my username?','strimnell',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Sarah Trimnell</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into EMAIL(EMAIL)
    values('strimnell@thinkparity.com');
insert into USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    values(7010,5008,true);
insert into USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7010,6000);

insert into PARITY_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('kenjiro','parity','What is my username?','kenjiro',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Kenjiro MacMartin</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into EMAIL(EMAIL)
    values('kenjiro@thinkparity.com');
insert into USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    values(7011,5009,true);
insert into USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7011,6000);

insert into PARITY_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('masako','parity','What is my username?','masako',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Masako Saito</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into EMAIL(EMAIL)
    values('masako@thinkparity.com');
insert into USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    values(7012,5010,true);
insert into USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7012,6000);

insert into PARITY_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('ahalaby','parity','What is my username?','ahalaby',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Avi Halaby</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into EMAIL(EMAIL)
    values('ahalaby@thinkparity.com');
insert into USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    values(7013,5011,true);
insert into USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7013,6000);

insert into PARITY_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('rwaterhouse','parity','What is my username?','rwaterhouse',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Randy Waterhouse</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into EMAIL(EMAIL)
    values('rwaterhouse@thinkparity.com');
insert into USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    values(7014,5012,true);
insert into USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7014,6000);

insert into PARITY_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('ashaftoe','parity','What is my username?','ashaftoe',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Amy Shaftoe</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into EMAIL(EMAIL)
    values('ashaftoe@thinkparity.com');
insert into USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    values(7015,5013,true);
insert into USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7015,6000);
insert into USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7015,6001);

insert into PARITY_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('ashaftoe-backup','parity','What is my username?','ashaftoe-backup',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Amy Shaftoe Archive</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into USER_BACKUP_REL(USER_ID,BACKUP_ID)
    values(7015,7016);

insert into PARITY_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('brussell','parity','What is my username?','brussell',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Bertrand Russell</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into EMAIL(EMAIL)
    values('brussell@thinkparity.com');
insert into USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    values(7017,5014,true);
insert into USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7017,6000);

insert into PARITY_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('gdengo','parity','What is my username?','gdengo',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Goto Dengo</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into EMAIL(EMAIL)
    values('gdengo@thinkparity.com');
insert into USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    values(7018,5015,true);

insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7001,7002,7001,NOW(),7001,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7001,7003,7001,NOW(),7001,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7001,7005,7001,NOW(),7001,NOW());

insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7002,7001,7002,NOW(),7002,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7002,7003,7002,NOW(),7002,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7002,7005,7002,NOW(),7002,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7002,7007,7002,NOW(),7002,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7002,7008,7002,NOW(),7002,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7002,7009,7002,NOW(),7002,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7002,7010,7002,NOW(),7002,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7002,7011,7002,NOW(),7002,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7002,7012,7002,NOW(),7002,NOW());

insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7003,7001,7003,NOW(),7003,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7003,7002,7003,NOW(),7003,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7003,7005,7003,NOW(),7003,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7003,7007,7003,NOW(),7003,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7003,7008,7003,NOW(),7003,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7003,7009,7003,NOW(),7003,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7003,7010,7003,NOW(),7003,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7003,7011,7003,NOW(),7003,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7003,7012,7003,NOW(),7003,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7003,7013,7003,NOW(),7003,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7003,7014,7003,NOW(),7003,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7003,7015,7003,NOW(),7003,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7003,7017,7003,NOW(),7003,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7003,7018,7003,NOW(),7003,NOW());

insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7005,7001,7005,NOW(),7005,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7005,7002,7005,NOW(),7005,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7005,7003,7005,NOW(),7005,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7005,7007,7005,NOW(),7005,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7005,7008,7005,NOW(),7005,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7005,7009,7005,NOW(),7005,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7005,7010,7005,NOW(),7005,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7005,7011,7005,NOW(),7005,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7005,7012,7005,NOW(),7005,NOW());

insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7007,7002,7007,NOW(),7007,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7007,7003,7007,NOW(),7007,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7007,7005,7007,NOW(),7007,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7007,7008,7007,NOW(),7007,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7007,7009,7007,NOW(),7007,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7007,7010,7007,NOW(),7007,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7007,7011,7007,NOW(),7007,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7007,7012,7007,NOW(),7007,NOW());

insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7008,7002,7008,NOW(),7008,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7008,7003,7008,NOW(),7008,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7008,7005,7008,NOW(),7008,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7008,7007,7008,NOW(),7008,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7008,7009,7008,NOW(),7008,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7008,7010,7008,NOW(),7008,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7008,7011,7008,NOW(),7008,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7008,7012,7008,NOW(),7008,NOW());

insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7009,7002,7009,NOW(),7009,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7009,7003,7009,NOW(),7009,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7009,7005,7009,NOW(),7009,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7009,7007,7009,NOW(),7009,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7009,7008,7009,NOW(),7009,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7009,7010,7009,NOW(),7009,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7009,7011,7009,NOW(),7009,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7009,7012,7009,NOW(),7009,NOW());

insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7010,7002,7010,NOW(),7010,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7010,7003,7010,NOW(),7010,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7010,7005,7010,NOW(),7010,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7010,7007,7010,NOW(),7010,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7010,7008,7010,NOW(),7010,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7010,7009,7010,NOW(),7010,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7010,7011,7010,NOW(),7010,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7010,7012,7010,NOW(),7010,NOW());

insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7011,7002,7011,NOW(),7011,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7011,7003,7011,NOW(),7011,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7011,7005,7011,NOW(),7011,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7011,7007,7011,NOW(),7011,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7011,7008,7011,NOW(),7011,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7011,7009,7011,NOW(),7011,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7011,7010,7011,NOW(),7011,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7011,7012,7011,NOW(),7011,NOW());

insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7012,7002,7012,NOW(),7012,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7012,7003,7012,NOW(),7012,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7012,7005,7012,NOW(),7012,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7012,7007,7012,NOW(),7012,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7012,7008,7012,NOW(),7012,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7012,7009,7012,NOW(),7012,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7012,7010,7012,NOW(),7012,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7012,7011,7012,NOW(),7012,NOW());

insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7014,7003,7014,NOW(),7014,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7014,7013,7014,NOW(),7014,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7014,7015,7014,NOW(),7014,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7014,7017,7014,NOW(),7014,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7014,7018,7014,NOW(),7014,NOW());

insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7013,7003,7013,NOW(),7013,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7013,7014,7013,NOW(),7013,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7013,7015,7013,NOW(),7013,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7013,7017,7013,NOW(),7013,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7013,7018,7013,NOW(),7013,NOW());

insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7015,7003,7015,NOW(),7015,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7015,7013,7015,NOW(),7015,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7015,7014,7015,NOW(),7015,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7015,7017,7015,NOW(),7015,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7015,7018,7015,NOW(),7015,NOW());

insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7017,7003,7017,NOW(),7017,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7017,7013,7017,NOW(),7017,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7017,7014,7017,NOW(),7017,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7017,7015,7017,NOW(),7017,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7017,7018,7017,NOW(),7017,NOW());

insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7018,7003,7018,NOW(),7018,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7018,7013,7018,NOW(),7018,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7018,7014,7018,NOW(),7018,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7018,7015,7018,NOW(),7018,NOW());
insert into CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7018,7017,7018,NOW(),7018,NOW());
