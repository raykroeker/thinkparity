insert into jiveProperty (NAME,PROPVALUE)
    values('xmpp.auth.anonymous','true');
insert into jiveProperty (NAME,PROPVALUE)
    values('xmpp.domain','thinkparity.net');
insert into jiveProperty (NAME,PROPVALUE)
    values('xmpp.server.socket.port','5272');
insert into jiveProperty (NAME,PROPVALUE)
    values('xmpp.socket.plain.port','5228');
insert into jiveProperty (NAME,PROPVALUE)
    values('xmpp.socket.ssl.active','true');
insert into jiveProperty (NAME,PROPVALUE)
    values('xmpp.socket.ssl.keypass','password');
insert into jiveProperty (NAME,PROPVALUE)
    values('xmpp.socket.ssl.port','5229');

insert into jiveProperty (NAME,PROPVALUE)
    values('thinkparity.environment','DEVELOPMENT_ROBERT');
insert into jiveProperty (NAME,PROPVALUE)
    values('thinkparity.mode','DEVELOPMENT');
insert into FEATURE(FEATURE) values('BACKUP');

insert into jiveUser(USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('thinkparity','parity',0,0);
insert into PARITY_USER(USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('thinkparity','What is my username?','thinkparity',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>thinkParity Services Inc.</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into USER_EMAIL(USER_ID,EMAIL,VERIFIED)
    values(7000,'thinkParity@thinkparity.com',true);

insert into jiveUser(USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('asahebjam','parity',0,0);
insert into PARITY_USER(USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('asahebjam','What is my username?','asahebjam',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Amir Sahebjam</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into USER_EMAIL(USER_ID,EMAIL,VERIFIED)
    values(7001,'asahebjam@gmail.com',true);

insert into jiveUser(USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('omid','parity',0,0);
insert into PARITY_USER(USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('omid','What is my username?','omid',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Omid Ejtemai</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into USER_EMAIL(USER_ID,EMAIL,VERIFIED)
    values(7002,'omid@thinkparity.com',true);

insert into jiveUser(USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('rkroeker','parity',0,0);
insert into PARITY_USER(USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('rkroeker','What is my username?','rkroeker',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Raymond Kroeker</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into USER_EMAIL(USER_ID,EMAIL,VERIFIED)
    values(7003,'raymond@thinkparity.com',true);
insert into USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7003,1000);
insert into jiveUser(USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('rkroeker-backup','parity',0,0);
insert into PARITY_USER(USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('rkroeker-backup','What is my username?','rkroeker-backup',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Raymond Kroeker Archive</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into USER_BACKUP_REL(USER_ID,BACKUP_ID)
    values(7003,7004);

insert into jiveUser(USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('robert','parity',0,0);
insert into PARITY_USER(USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('robert','What is my username?','robert',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Robert MacMartin</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into USER_EMAIL(USER_ID,EMAIL,VERIFIED)
    values(7005,'robert@thinkparity.com',true);
insert into USER_FEATURE_REL(USER_ID,FEATURE_ID)
	values(7005,1000);
insert into jiveUser(USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('robert-backup','parity',0,0);
insert into PARITY_USER(USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('robert-backup','What is my username?','robert-backup',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Robert MacMartin Archive</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into USER_BACKUP_REL(USER_ID,BACKUP_ID)
    values(7005,7006);

insert into jiveUser(USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('djohnson','parity',0,0);
insert into PARITY_USER(USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('djohnson','What is my username?','djohnson',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Don Johnson</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into USER_EMAIL(USER_ID,EMAIL,VERIFIED)
    values(7007,'djohnson@thinkparity.com',true);

insert into jiveUser(USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('emorrison','parity',0,0);
insert into PARITY_USER(USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('emorrison','What is my username?','emorrison',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Emily Morrison</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into USER_EMAIL(USER_ID,EMAIL,VERIFIED)
    values(7008,'emorrison@thinkparity.com',true);

insert into jiveUser(USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('kmfarland','parity',0,0);
insert into PARITY_USER(USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('kmfarland','What is my username?','kmfarland',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Kevin MacFarland</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into USER_EMAIL(USER_ID,EMAIL,VERIFIED)
    values(7009,'kmfarland@thinkparity.com',true);

insert into jiveUser(USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('strimnell','parity',0,0);
insert into PARITY_USER(USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('strimnell','What is my username?','strimnell',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Sarah Trimnell</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into USER_EMAIL(USER_ID,EMAIL,VERIFIED)
    values(7010,'strimnell@thinkparity.com',true);

insert into jiveUser(USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('kenjiro','parity',0,0);
insert into PARITY_USER(USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('kenjiro','What is my username?','kenjiro',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Kenjiro MacMartin</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into USER_EMAIL(USER_ID,EMAIL,VERIFIED)
    values(7011,'kenjiro@thinkparity.com',true);

insert into jiveUser(USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('masako','parity',0,0);
insert into PARITY_USER(USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('masako','What is my username?','masako',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Masako Saito</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into USER_EMAIL(USER_ID,EMAIL,VERIFIED)
    values(7012,'masako@thinkparity.com',true);

insert into jiveUser(USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('ahalaby','parity',0,0);
insert into PARITY_USER(USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('ahalaby','What is my username?','ahalaby',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Avi Halaby</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into USER_EMAIL(USER_ID,EMAIL,VERIFIED)
    values(7013,'avi@epiphyte.com',true);

insert into jiveUser(USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('rwaterhouse','parity',0,0);
insert into PARITY_USER(USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('rwaterhouse','What is my username?','rwaterhouse',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Randy Waterhouse</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into USER_EMAIL(USER_ID,EMAIL,VERIFIED)
    values(7014,'randy@epiphyte.com',true);

insert into jiveUser(USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('ashaftoe','parity',0,0);
insert into PARITY_USER(USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('ashaftoe','What is my username?','ashaftoe',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Amy Shaftoe</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into USER_EMAIL(USER_ID,EMAIL,VERIFIED)
    values(7015,'amy@sempermarine.com',true);
insert into USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7015,1000);
insert into jiveUser(USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('ashaftoe-backup','parity',0,0);
insert into PARITY_USER(USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('ashaftoe-backup','What is my username?','ashaftoe-backup',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Amy Shaftoe Archive</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into USER_BACKUP_REL(USER_ID,BACKUP_ID)
    values(7015,7016);

insert into jiveUser(USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('brussell','parity',0,0);
insert into PARITY_USER(USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('brussell','What is my username?','brussell',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Bertrand Russell</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into USER_EMAIL(USER_ID,EMAIL,VERIFIED)
    values(7017,'bertrand@thinkparity.com',true);

insert into jiveUser(USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('gdengo','parity',0,0);
insert into PARITY_USER(USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('gdengo','What is my username?','gdengo',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Goto Dengo</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into USER_EMAIL(USER_ID,EMAIL,VERIFIED)
    values(7018,'goto@gotoengineering.com',true);

insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7001,7002,7001,NOW(),7001,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7001,7003,7001,NOW(),7001,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7001,7005,7001,NOW(),7001,NOW());

insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7002,7001,7002,NOW(),7002,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7002,7003,7002,NOW(),7002,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7002,7005,7002,NOW(),7002,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7002,7007,7002,NOW(),7002,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7002,7008,7002,NOW(),7002,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7002,7009,7002,NOW(),7002,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7002,7010,7002,NOW(),7002,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7002,7011,7002,NOW(),7002,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7002,7012,7002,NOW(),7002,NOW());

insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7003,7001,7003,NOW(),7003,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7003,7002,7003,NOW(),7003,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7003,7005,7003,NOW(),7003,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7003,7007,7003,NOW(),7003,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7003,7008,7003,NOW(),7003,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7003,7009,7003,NOW(),7003,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7003,7010,7003,NOW(),7003,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7003,7011,7003,NOW(),7003,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7003,7012,7003,NOW(),7003,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7003,7013,7003,NOW(),7003,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7003,7014,7003,NOW(),7003,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7003,7015,7003,NOW(),7003,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7003,7017,7003,NOW(),7003,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7003,7018,7003,NOW(),7003,NOW());

insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7005,7001,7005,NOW(),7005,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7005,7002,7005,NOW(),7005,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7005,7003,7005,NOW(),7005,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7005,7007,7005,NOW(),7005,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7005,7008,7005,NOW(),7005,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7005,7009,7005,NOW(),7005,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7005,7010,7005,NOW(),7005,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7005,7011,7005,NOW(),7005,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7005,7012,7005,NOW(),7005,NOW());

insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7007,7002,7007,NOW(),7007,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7007,7003,7007,NOW(),7007,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7007,7005,7007,NOW(),7007,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7007,7008,7007,NOW(),7007,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7007,7009,7007,NOW(),7007,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7007,7010,7007,NOW(),7007,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7007,7011,7007,NOW(),7007,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7007,7012,7007,NOW(),7007,NOW());

insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7008,7002,7008,NOW(),7008,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7008,7003,7008,NOW(),7008,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7008,7005,7008,NOW(),7008,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7008,7007,7008,NOW(),7008,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7008,7009,7008,NOW(),7008,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7008,7010,7008,NOW(),7008,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7008,7011,7008,NOW(),7008,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7008,7012,7008,NOW(),7008,NOW());

insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7009,7002,7009,NOW(),7009,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7009,7003,7009,NOW(),7009,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7009,7005,7009,NOW(),7009,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7009,7007,7009,NOW(),7009,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7009,7008,7009,NOW(),7009,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7009,7010,7009,NOW(),7009,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7009,7011,7009,NOW(),7009,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7009,7012,7009,NOW(),7009,NOW());

insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7010,7002,7010,NOW(),7010,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7010,7003,7010,NOW(),7010,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7010,7005,7010,NOW(),7010,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7010,7007,7010,NOW(),7010,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7010,7008,7010,NOW(),7010,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7010,7009,7010,NOW(),7010,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7010,7011,7010,NOW(),7010,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7010,7012,7010,NOW(),7010,NOW());

insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7011,7002,7011,NOW(),7011,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7011,7003,7011,NOW(),7011,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7011,7005,7011,NOW(),7011,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7011,7007,7011,NOW(),7011,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7011,7008,7011,NOW(),7011,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7011,7009,7011,NOW(),7011,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7011,7010,7011,NOW(),7011,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7011,7012,7011,NOW(),7011,NOW());

insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7012,7002,7012,NOW(),7012,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7012,7003,7012,NOW(),7012,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7012,7005,7012,NOW(),7012,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7012,7007,7012,NOW(),7012,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7012,7008,7012,NOW(),7012,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7012,7009,7012,NOW(),7012,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7012,7010,7012,NOW(),7012,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7012,7011,7012,NOW(),7012,NOW());

insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7014,7003,7014,NOW(),7014,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7014,7013,7014,NOW(),7014,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7014,7015,7014,NOW(),7014,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7014,7017,7014,NOW(),7014,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7014,7018,7014,NOW(),7014,NOW());

insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7013,7003,7013,NOW(),7013,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7013,7014,7013,NOW(),7013,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7013,7015,7013,NOW(),7013,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7013,7017,7013,NOW(),7013,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7013,7018,7013,NOW(),7013,NOW());

insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7015,7003,7015,NOW(),7015,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7015,7013,7015,NOW(),7015,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7015,7014,7015,NOW(),7015,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7015,7017,7015,NOW(),7015,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7015,7018,7015,NOW(),7015,NOW());

insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7017,7003,7017,NOW(),7017,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7017,7013,7017,NOW(),7017,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7017,7014,7017,NOW(),7017,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7017,7015,7017,NOW(),7017,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7017,7018,7017,NOW(),7017,NOW());

insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7018,7003,7018,NOW(),7018,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7018,7013,7018,NOW(),7018,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7018,7014,7018,NOW(),7018,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7018,7015,7018,NOW(),7018,NOW());
insert into USER_CONTACT(USER_ID,CONTACT_ID,CREATED_BY,CREATED_ON,UPDATED_BY,UPDATED_ON)
    values(7018,7017,7018,NOW(),7018,NOW());
