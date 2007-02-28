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
insert into PARITY_FEATURE (FEATURE)
    values('ARCHIVE');

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('thinkparity','parity',0,0);
insert into PARITY_USER(USERNAME)
    values('thinkparity');
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('thinkparity', 'thinkParity@thinkparity.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('thinkparity','What is my username?','thinkparity',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>thinkParity Solutions Inc.</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('asahebjam','parity',0,0);
insert into PARITY_USER(USERNAME)
    values('asahebjam');
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('asahebjam', 'asahebjam@gmail.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('asahebjam','What is my username?','asahebjam',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Amir Sahebjam</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
    values ('asahebjam', 1000);

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('omid','parity',0,0);
insert into PARITY_USER(USERNAME)
    values('omid');
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('omid', 'omid@thinkparity.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('omid','What is my username?','omid',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Omid Ejtemai</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
    values ('omid', 1000);

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('rkroeker','parity',0,0);
insert into PARITY_USER(USERNAME)
    values('rkroeker');
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('rkroeker', 'raymond@thinkparity.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('rkroeker','What is my username?','rkroeker',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Raymond Kroeker</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
    values ('rkroeker', 1000);
insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('rkroeker-archive','parity',0,0);
insert into PARITY_USER(USERNAME)
    values('rkroeker-archive');
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('rkroeker-archive','What is my username?','archive.rkroeker',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Raymond Kroeker Archive</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into PARITY_USER_ARCHIVE_REL (USERNAME,ARCHIVENAME)
    values('rkroeker', 'rkroeker-archive');

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('robert','parity',0,0);
insert into PARITY_USER(USERNAME)
    values('robert');
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('robert', 'robert@thinkparity.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('robert','What is my username?','robert',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Robert MacMartin</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
	values ('robert', 1000);
insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('robert-archive','parity',0,0);
insert into PARITY_USER(USERNAME)
    values('robert-archive');
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('robert-archive','What is my username?','archive.robert',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Robert MacMartin Archive</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into PARITY_USER_ARCHIVE_REL (USERNAME,ARCHIVENAME)
    values('robert', 'robert-archive');

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('djohnson','parity',0,0);
insert into PARITY_USER(USERNAME)
    values('djohnson');
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('djohnson', 'djohnson@thinkparity.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('djohnson','What is my username?','djohnson',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Don Johnson</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
    values ('djohnson', 1000);

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('emorrison','parity',0,0);
insert into PARITY_USER(USERNAME)
    values('emorrison');
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('emorrison', 'emorrison@thinkparity.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('emorrison','What is my username?','emorrison',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Emily Morrison</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
    values ('emorrison', 1000);

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('kmfarland','parity',0,0);
insert into PARITY_USER(USERNAME)
    values('kmfarland');
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('kmfarland', 'kmfarland@thinkparity.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('kmfarland','What is my username?','kmfarland',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Kevin MacFarland</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
    values ('kmfarland', 1000);

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('strimnell','parity',0,0);
insert into PARITY_USER(USERNAME)
    values('strimnell');
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('strimnell', 'strimnell@thinkparity.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('strimnell','What is my username?','strimnell',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Sarah Trimnell</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
    values ('strimnell', 1000);

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('kenjiro','parity',0,0);
insert into PARITY_USER(USERNAME)
    values('kenjiro');
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('kenjiro', 'kenjiro@thinkparity.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('kenjiro','What is my username?','kenjiro',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Kenjiro MacMartin</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
    values ('kenjiro', 1000);

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('masako','parity',0,0);
insert into PARITY_USER(USERNAME)
    values('masako');
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('masako', 'masako@thinkparity.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('masako','What is my username?','masako',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Masako Saito</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
    values ('masako', 1000);

insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('asahebjam', 'omid@thinkparity.net', 'asahebjam', NOW(), 'asahebjam', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('asahebjam', 'rkroeker@thinkparity.net', 'asahebjam', NOW(), 'asahebjam', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('asahebjam', 'robert@thinkparity.net', 'asahebjam', NOW(), 'asahebjam', NOW());

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('ahalaby','parity',0,0);
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('ahalaby', 'avi@epiphyte.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('ahalaby','What is my username?','ahalaby',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Avi Halaby</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
    values ('ahalaby', 1000);

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('rwaterhouse','parity',0,0);
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('rwaterhouse', 'randy@epiphyte.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('rwaterhouse','What is my username?','rwaterhouse',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Randy Waterhouse</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
    values ('rwaterhouse', 1000);

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('ashaftoe','parity',0,0);
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('ashaftoe', 'amy@sempermarine.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('ashaftoe','What is my username?','ashaftoe',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Amy Shaftoe</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
    values ('ashaftoe', 1000);
insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('ashaftoe-archive','parity',0,0);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('ashaftoe-archive','What is my username?','archive.ashaftoe',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Amy Shaftoe Archive</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into PARITY_USER_ARCHIVE_REL (USERNAME,ARCHIVENAME)
    values('ashaftoe', 'ashaftoe-archive');

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('brussell','parity',0,0);
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('brussell', 'bertrand@thinkparity.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('brussell','What is my username?','brussell',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Bertrand Russell</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
	values ('brussell', 1000);

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('gdengo','parity',0,0);
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('gdengo', 'goto@gotoengineering.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('gdengo','What is my username?','gdengo',false,'<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>Goto Dengo</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
    values ('gdengo', 1000);

insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('omid', 'asahebjam@thinkparity.net', 'omid', NOW(), 'omid', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('omid', 'rkroeker@thinkparity.net', 'omid', NOW(), 'omid', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('omid', 'robert@thinkparity.net', 'omid', NOW(), 'omid', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('omid', 'djohnson@thinkparity.net', 'omid', NOW(), 'omid', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('omid', 'emorrison@thinkparity.net', 'omid', NOW(), 'omid', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('omid', 'kmfarland@thinkparity.net', 'omid', NOW(), 'omid', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('omid', 'strimnell@thinkparity.net', 'omid', NOW(), 'omid', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('omid', 'kenjiro@thinkparity.net', 'omid', NOW(), 'omid', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('omid', 'masako@thinkparity.net', 'omid', NOW(), 'omid', NOW());

insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('rkroeker', 'asahebjam@thinkparity.net', 'rkroeker', NOW(), 'rkroeker', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('rkroeker', 'omid@thinkparity.net', 'rkroeker', NOW(), 'rkroeker', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('rkroeker', 'robert@thinkparity.net', 'rkroeker', NOW(), 'rkroeker', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('rkroeker', 'djohnson@thinkparity.net', 'rkroeker', NOW(), 'rkroeker', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('rkroeker', 'emorrison@thinkparity.net', 'rkroeker', NOW(), 'rkroeker', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('rkroeker', 'kmfarland@thinkparity.net', 'rkroeker', NOW(), 'rkroeker', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('rkroeker', 'strimnell@thinkparity.net', 'rkroeker', NOW(), 'rkroeker', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('rkroeker', 'kenjiro@thinkparity.net', 'rkroeker', NOW(), 'rkroeker', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('rkroeker', 'masako@thinkparity.net', 'rkroeker', NOW(), 'rkroeker', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('rkroeker', 'ahalaby@thinkparity.net', 'rkroeker', NOW(), 'rkroeker', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('rkroeker', 'rwaterhouse@thinkparity.net', 'rkroeker', NOW(), 'rkroeker', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('rkroeker', 'ashaftoe@thinkparity.net', 'rkroeker', NOW(), 'rkroeker', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('rkroeker', 'brussell@thinkparity.net', 'rkroeker', NOW(), 'rkroeker', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('rkroeker', 'gdengo@thinkparity.net', 'rkroeker', NOW(), 'rkroeker', NOW());

insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('robert', 'asahebjam@thinkparity.net', 'robert', NOW(), 'robert', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('robert', 'omid@thinkparity.net', 'robert', NOW(), 'robert', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('robert', 'rkroeker@thinkparity.net', 'robert', NOW(), 'robert', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('robert', 'djohnson@thinkparity.net', 'robert', NOW(), 'robert', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('robert', 'emorrison@thinkparity.net', 'robert', NOW(), 'robert', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('robert', 'kmfarland@thinkparity.net', 'robert', NOW(), 'robert', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('robert', 'strimnell@thinkparity.net', 'robert', NOW(), 'robert', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('robert', 'kenjiro@thinkparity.net', 'robert', NOW(), 'robert', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('robert', 'masako@thinkparity.net', 'robert', NOW(), 'robert', NOW());

insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('djohnson', 'omid@thinkparity.net', 'djohnson', NOW(), 'djohnson', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('djohnson', 'rkroeker@thinkparity.net', 'djohnson', NOW(), 'djohnson', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('djohnson', 'robert@thinkparity.net', 'djohnson', NOW(), 'djohnson', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('djohnson', 'emorrison@thinkparity.net', 'djohnson', NOW(), 'djohnson', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('djohnson', 'kmfarland@thinkparity.net', 'djohnson', NOW(), 'djohnson', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('djohnson', 'strimnell@thinkparity.net', 'djohnson', NOW(), 'djohnson', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('djohnson', 'kenjiro@thinkparity.net', 'djohnson', NOW(), 'djohnson', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('djohnson', 'masako@thinkparity.net', 'djohnson', NOW(), 'djohnson', NOW());

insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('emorrison', 'omid@thinkparity.net', 'emorrison', NOW(), 'emorrison', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('emorrison', 'rkroeker@thinkparity.net', 'emorrison', NOW(), 'emorrison', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('emorrison', 'robert@thinkparity.net', 'emorrison', NOW(), 'emorrison', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('emorrison', 'djohnson@thinkparity.net', 'emorrison', NOW(), 'emorrison', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('emorrison', 'kmfarland@thinkparity.net', 'emorrison', NOW(), 'emorrison', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('emorrison', 'strimnell@thinkparity.net', 'emorrison', NOW(), 'emorrison', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('emorrison', 'kenjiro@thinkparity.net', 'emorrison', NOW(), 'emorrison', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('emorrison', 'masako@thinkparity.net', 'emorrison', NOW(), 'emorrison', NOW());

insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('kmfarland', 'omid@thinkparity.net', 'kmfarland', NOW(), 'kmfarland', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('kmfarland', 'rkroeker@thinkparity.net', 'kmfarland', NOW(), 'kmfarland', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('kmfarland', 'robert@thinkparity.net', 'kmfarland', NOW(), 'kmfarland', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('kmfarland', 'djohnson@thinkparity.net', 'kmfarland', NOW(), 'kmfarland', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('kmfarland', 'emorrison@thinkparity.net', 'kmfarland', NOW(), 'kmfarland', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('kmfarland', 'strimnell@thinkparity.net', 'kmfarland', NOW(), 'kmfarland', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('kmfarland', 'kenjiro@thinkparity.net', 'kmfarland', NOW(), 'kmfarland', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('kmfarland', 'masako@thinkparity.net', 'kmfarland', NOW(), 'kmfarland', NOW());

insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('strimnell', 'omid@thinkparity.net', 'strimnell', NOW(), 'strimnell', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('strimnell', 'rkroeker@thinkparity.net', 'strimnell', NOW(), 'strimnell', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('strimnell', 'robert@thinkparity.net', 'strimnell', NOW(), 'strimnell', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('strimnell', 'djohnson@thinkparity.net', 'strimnell', NOW(), 'strimnell', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('strimnell', 'emorrison@thinkparity.net', 'strimnell', NOW(), 'strimnell', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('strimnell', 'kmfarland@thinkparity.net', 'strimnell', NOW(), 'strimnell', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('strimnell', 'kenjiro@thinkparity.net', 'strimnell', NOW(), 'strimnell', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('strimnell', 'masako@thinkparity.net', 'strimnell', NOW(), 'strimnell', NOW());

insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('kenjiro', 'omid@thinkparity.net', 'kenjiro', NOW(), 'kenjiro', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('kenjiro', 'rkroeker@thinkparity.net', 'kenjiro', NOW(), 'kenjiro', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('kenjiro', 'robert@thinkparity.net', 'kenjiro', NOW(), 'kenjiro', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('kenjiro', 'djohnson@thinkparity.net', 'kenjiro', NOW(), 'kenjiro', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('kenjiro', 'emorrison@thinkparity.net', 'kenjiro', NOW(), 'kenjiro', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('kenjiro', 'kmfarland@thinkparity.net', 'kenjiro', NOW(), 'kenjiro', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('kenjiro', 'strimnell@thinkparity.net', 'kenjiro', NOW(), 'kenjiro', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('kenjiro', 'masako@thinkparity.net', 'kenjiro', NOW(), 'kenjiro', NOW());

insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('masako', 'omid@thinkparity.net', 'masako', NOW(), 'masako', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('masako', 'rkroeker@thinkparity.net', 'masako', NOW(), 'masako', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('masako', 'robert@thinkparity.net', 'masako', NOW(), 'masako', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('masako', 'djohnson@thinkparity.net', 'masako', NOW(), 'masako', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('masako', 'emorrison@thinkparity.net', 'masako', NOW(), 'masako', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('masako', 'kmfarland@thinkparity.net', 'masako', NOW(), 'masako', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('masako', 'strimnell@thinkparity.net', 'masako', NOW(), 'masako', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('masako', 'kenjiro@thinkparity.net', 'masako', NOW(), 'masako', NOW());

insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('rwaterhouse', 'rkroeker@thinkparity.net', 'rwaterhouse', NOW(), 'rwaterhouse', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('rwaterhouse', 'ahalaby@thinkparity.net', 'rwaterhouse', NOW(), 'rwaterhouse', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('rwaterhouse', 'ashaftoe@thinkparity.net', 'rwaterhouse', NOW(), 'rwaterhouse', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('rwaterhouse', 'brussell@thinkparity.net', 'rwaterhouse', NOW(), 'rwaterhouse', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('rwaterhouse', 'gdengo@thinkparity.net', 'rwaterhouse', NOW(), 'rwaterhouse', NOW());

insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('ahalaby', 'rkroeker@thinkparity.net', 'ahalaby', NOW(), 'ahalaby', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('ahalaby', 'rwaterhouse@thinkparity.net', 'ahalaby', NOW(), 'ahalaby', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('ahalaby', 'ashaftoe@thinkparity.net', 'ahalaby', NOW(), 'ahalaby', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('ahalaby', 'brussell@thinkparity.net', 'ahalaby', NOW(), 'ahalaby', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('ahalaby', 'gdengo@thinkparity.net', 'ahalaby', NOW(), 'ahalaby', NOW());

insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('ashaftoe', 'rkroeker@thinkparity.net', 'ashaftoe', NOW(), 'ashaftoe', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('ashaftoe', 'ahalaby@thinkparity.net', 'ashaftoe', NOW(), 'ashaftoe', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('ashaftoe', 'rwaterhouse@thinkparity.net', 'ashaftoe', NOW(), 'ashaftoe', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('ashaftoe', 'brussell@thinkparity.net', 'ashaftoe', NOW(), 'ashaftoe', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('ashaftoe', 'gdengo@thinkparity.net', 'ashaftoe', NOW(), 'ashaftoe', NOW());

insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('brussell', 'rkroeker@thinkparity.net', 'brussell', NOW(), 'brussell', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('brussell', 'ahalaby@thinkparity.net', 'brussell', NOW(), 'brussell', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('brussell', 'rwaterhouse@thinkparity.net', 'brussell', NOW(), 'brussell', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('brussell', 'ashaftoe@thinkparity.net', 'brussell', NOW(), 'brussell', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('brussell', 'gdengo@thinkparity.net', 'brussell', NOW(), 'brussell', NOW());

insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('gdengo', 'rkroeker@thinkparity.net', 'gdengo', NOW(), 'gdengo', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('gdengo', 'ahalaby@thinkparity.net', 'gdengo', NOW(), 'gdengo', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('gdengo', 'rwaterhouse@thinkparity.net', 'gdengo', NOW(), 'gdengo', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('gdengo', 'ashaftoe@thinkparity.net', 'gdengo', NOW(), 'gdengo', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('gdengo', 'brussell@thinkparity.net', 'gdengo', NOW(), 'gdengo', NOW());
