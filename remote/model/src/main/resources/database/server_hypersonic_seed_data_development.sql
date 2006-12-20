insert into jiveProperty (NAME,PROPVALUE)
    values('xmpp.auth.anonymous','true');
insert into jiveProperty (NAME,PROPVALUE)
    values('xmpp.domain','thinkparity.dyndns.org');
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
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('thinkparity', 'thinkParity@thinkparity.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('thinkparity','What is my username?','thinkparity',false,'<com.thinkparity.codebase.model.user.UserVCard><name>thinkParity Solutions Inc.</name></com.thinkparity.codebase.model.user.UserVCard>');

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('asahebjam','parity',0,0);
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('asahebjam', 'asahebjam@gmail.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('asahebjam','What is my username?','asahebjam',false,'<com.thinkparity.codebase.model.user.UserVCard><name>Amir Sahebjam</name></com.thinkparity.codebase.model.user.UserVCard>');
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
    values ('asahebjam', 1000);

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('omid','parity',0,0);
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('omid', 'omid@thinkparity.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('omid','What is my username?','omid',false,'<com.thinkparity.codebase.model.user.UserVCard><name>Omid Ejtemai</name></com.thinkparity.codebase.model.user.UserVCard>');
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
    values ('omid', 1000);

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('rkroeker','parity',0,0);
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('rkroeker', 'raymond@thinkparity.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('rkroeker','What is my username?','rkroeker',false,'<com.thinkparity.codebase.model.user.UserVCard><name>Raymond Kroeker</name></com.thinkparity.codebase.model.user.UserVCard>');
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
    values ('rkroeker', 1000);
insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('rkroeker-archive','parity',0,0);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('rkroeker-archive','What is my username?','archive.rkroeker',false,'<com.thinkparity.codebase.model.user.UserVCard><name>Raymond Kroeker Archive</name></com.thinkparity.codebase.model.user.UserVCard>');
insert into PARITY_USER_ARCHIVE_REL (USERNAME,ARCHIVENAME)
    values('rkroeker', 'rkroeker-archive');

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('robert','parity',0,0);
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('robert', 'robert@thinkparity.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('robert','What is my username?','robert',false,'<com.thinkparity.codebase.model.user.UserVCard><name>Robert MacMartin</name></com.thinkparity.codebase.model.user.UserVCard>');
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
	values ('robert', 1000);
insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('robert-archive','parity',0,0);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('robert-archive','What is my username?','archive.robert',false,'<com.thinkparity.codebase.model.user.UserVCard><name>Robert MacMartin Archive</name></com.thinkparity.codebase.model.user.UserVCard>');
insert into PARITY_USER_ARCHIVE_REL (USERNAME,ARCHIVENAME)
    values('robert', 'robert-archive');

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('djohnson','parity',0,0);
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('djohnson', 'djohnson@thinkparity.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('djohnson','What is my username?','djohnson',false,'<com.thinkparity.codebase.model.user.UserVCard><name>Don Johnson</name></com.thinkparity.codebase.model.user.UserVCard>');
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
    values ('djohnson', 1000);

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('emorrison','parity',0,0);
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('emorrison', 'emorrison@thinkparity.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('emorrison','What is my username?','emorrison',false,'<com.thinkparity.codebase.model.user.UserVCard><name>Emily Morrison</name></com.thinkparity.codebase.model.user.UserVCard>');
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
    values ('emorrison', 1000);

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('kmfarland','parity',0,0);
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('kmfarland', 'kmfarland@thinkparity.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('kmfarland','What is my username?','kmfarland',false,'<com.thinkparity.codebase.model.user.UserVCard><name>Kevin MacFarland</name></com.thinkparity.codebase.model.user.UserVCard>');
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
    values ('kmfarland', 1000);

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('strimnell','parity',0,0);
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('strimnell', 'strimnell@thinkparity.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('strimnell','What is my username?','strimnell',false,'<com.thinkparity.codebase.model.user.UserVCard><name>Sarah Trimnell</name></com.thinkparity.codebase.model.user.UserVCard>');
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
    values ('strimnell', 1000);

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('kenjiro','parity',0,0);
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('kenjiro', 'kenjiro@thinkparity.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('kenjiro','What is my username?','kenjiro',false,'<com.thinkparity.codebase.model.user.UserVCard><name>Kenjiro MacMartin</name></com.thinkparity.codebase.model.user.UserVCard>');
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
    values ('kenjiro', 1000);

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('masako','parity',0,0);
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('masako', 'masako@thinkparity.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('masako','What is my username?','masako',false,'<com.thinkparity.codebase.model.user.UserVCard><name>Masako Saito</name></com.thinkparity.codebase.model.user.UserVCard>');
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
    values ('masako', 1000);

insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('asahebjam', 'omid@thinkparity.dyndns.org', 'asahebjam', NOW(), 'asahebjam', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('asahebjam', 'rkroeker@thinkparity.dyndns.org', 'asahebjam', NOW(), 'asahebjam', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('asahebjam', 'robert@thinkparity.dyndns.org', 'asahebjam', NOW(), 'asahebjam', NOW());

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('ahalaby','parity',0,0);
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('ahalaby', 'avi@epiphyte.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('ahalaby','What is my username?','ahalaby',false,'<com.thinkparity.codebase.model.user.UserVCard><name>Avi Halaby</name></com.thinkparity.codebase.model.user.UserVCard>');
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
    values ('ahalaby', 1000);

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('rwaterhouse','parity',0,0);
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('rwaterhouse', 'randy@epiphyte.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('rwaterhouse','What is my username?','rwaterhouse',false,'<com.thinkparity.codebase.model.user.UserVCard><name>Randy Waterhouse</name></com.thinkparity.codebase.model.user.UserVCard>');
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
    values ('rwaterhouse', 1000);

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('ashaftoe','parity',0,0);
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('ashaftoe', 'amy@sempermarine.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('ashaftoe','What is my username?','ashaftoe',false,'<com.thinkparity.codebase.model.user.UserVCard><name>Amy Shaftoe</name></com.thinkparity.codebase.model.user.UserVCard>');
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
    values ('ashaftoe', 1000);
insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('ashaftoe-archive','parity',0,0);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('ashaftoe-archive','What is my username?','archive.ashaftoe',false,'<com.thinkparity.codebase.model.user.UserVCard><name>Amy Shaftoe Archive</name></com.thinkparity.codebase.model.user.UserVCard>');
insert into PARITY_USER_ARCHIVE_REL (USERNAME,ARCHIVENAME)
    values('ashaftoe', 'ashaftoe-archive');

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('brussell','parity',0,0);
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('brussell', 'bertrand@thinkparity.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('brussell','What is my username?','brussell',false,'<com.thinkparity.codebase.model.user.UserVCard><name>Bertrand Russell</name></com.thinkparity.codebase.model.user.UserVCard>');
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
	values ('brussell', 1000);

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('gdengo','parity',0,0);
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('gdengo', 'goto@gotoengineering.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('gdengo','What is my username?','gdengo',false,'<com.thinkparity.codebase.model.user.UserVCard><name>Goto Dengo</name></com.thinkparity.codebase.model.user.UserVCard>');
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
    values ('gdengo', 1000);

insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('omid', 'asahebjam@thinkparity.dyndns.org', 'omid', NOW(), 'omid', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('omid', 'rkroeker@thinkparity.dyndns.org', 'omid', NOW(), 'omid', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('omid', 'robert@thinkparity.dyndns.org', 'omid', NOW(), 'omid', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('omid', 'djohnson@thinkparity.dyndns.org', 'omid', NOW(), 'omid', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('omid', 'emorrison@thinkparity.dyndns.org', 'omid', NOW(), 'omid', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('omid', 'kmfarland@thinkparity.dyndns.org', 'omid', NOW(), 'omid', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('omid', 'strimnell@thinkparity.dyndns.org', 'omid', NOW(), 'omid', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('omid', 'kenjiro@thinkparity.dyndns.org', 'omid', NOW(), 'omid', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('omid', 'masako@thinkparity.dyndns.org', 'omid', NOW(), 'omid', NOW());

insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('rkroeker', 'asahebjam@thinkparity.dyndns.org', 'rkroeker', NOW(), 'rkroeker', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('rkroeker', 'omid@thinkparity.dyndns.org', 'rkroeker', NOW(), 'rkroeker', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('rkroeker', 'robert@thinkparity.dyndns.org', 'rkroeker', NOW(), 'rkroeker', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('rkroeker', 'djohnson@thinkparity.dyndns.org', 'rkroeker', NOW(), 'rkroeker', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('rkroeker', 'emorrison@thinkparity.dyndns.org', 'rkroeker', NOW(), 'rkroeker', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('rkroeker', 'kmfarland@thinkparity.dyndns.org', 'rkroeker', NOW(), 'rkroeker', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('rkroeker', 'strimnell@thinkparity.dyndns.org', 'rkroeker', NOW(), 'rkroeker', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('rkroeker', 'kenjiro@thinkparity.dyndns.org', 'rkroeker', NOW(), 'rkroeker', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('rkroeker', 'masako@thinkparity.dyndns.org', 'rkroeker', NOW(), 'rkroeker', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('rkroeker', 'ahalaby@thinkparity.dyndns.org', 'rkroeker', NOW(), 'rkroeker', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('rkroeker', 'rwaterhouse@thinkparity.dyndns.org', 'rkroeker', NOW(), 'rkroeker', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('rkroeker', 'ashaftoe@thinkparity.dyndns.org', 'rkroeker', NOW(), 'rkroeker', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('rkroeker', 'brussell@thinkparity.dyndns.org', 'rkroeker', NOW(), 'rkroeker', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('rkroeker', 'gdengo@thinkparity.dyndns.org', 'rkroeker', NOW(), 'rkroeker', NOW());

insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('robert', 'asahebjam@thinkparity.dyndns.org', 'robert', NOW(), 'robert', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('robert', 'omid@thinkparity.dyndns.org', 'robert', NOW(), 'robert', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('robert', 'rkroeker@thinkparity.dyndns.org', 'robert', NOW(), 'robert', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('robert', 'djohnson@thinkparity.dyndns.org', 'robert', NOW(), 'robert', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('robert', 'emorrison@thinkparity.dyndns.org', 'robert', NOW(), 'robert', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('robert', 'kmfarland@thinkparity.dyndns.org', 'robert', NOW(), 'robert', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('robert', 'strimnell@thinkparity.dyndns.org', 'robert', NOW(), 'robert', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('robert', 'kenjiro@thinkparity.dyndns.org', 'robert', NOW(), 'robert', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('robert', 'masako@thinkparity.dyndns.org', 'robert', NOW(), 'robert', NOW());

insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('djohnson', 'omid@thinkparity.dyndns.org', 'djohnson', NOW(), 'djohnson', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('djohnson', 'rkroeker@thinkparity.dyndns.org', 'djohnson', NOW(), 'djohnson', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('djohnson', 'robert@thinkparity.dyndns.org', 'djohnson', NOW(), 'djohnson', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('djohnson', 'emorrison@thinkparity.dyndns.org', 'djohnson', NOW(), 'djohnson', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('djohnson', 'kmfarland@thinkparity.dyndns.org', 'djohnson', NOW(), 'djohnson', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('djohnson', 'strimnell@thinkparity.dyndns.org', 'djohnson', NOW(), 'djohnson', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('djohnson', 'kenjiro@thinkparity.dyndns.org', 'djohnson', NOW(), 'djohnson', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('djohnson', 'masako@thinkparity.dyndns.org', 'djohnson', NOW(), 'djohnson', NOW());

insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('emorrison', 'omid@thinkparity.dyndns.org', 'emorrison', NOW(), 'emorrison', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('emorrison', 'rkroeker@thinkparity.dyndns.org', 'emorrison', NOW(), 'emorrison', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('emorrison', 'robert@thinkparity.dyndns.org', 'emorrison', NOW(), 'emorrison', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('emorrison', 'djohnson@thinkparity.dyndns.org', 'emorrison', NOW(), 'emorrison', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('emorrison', 'kmfarland@thinkparity.dyndns.org', 'emorrison', NOW(), 'emorrison', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('emorrison', 'strimnell@thinkparity.dyndns.org', 'emorrison', NOW(), 'emorrison', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('emorrison', 'kenjiro@thinkparity.dyndns.org', 'emorrison', NOW(), 'emorrison', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('emorrison', 'masako@thinkparity.dyndns.org', 'emorrison', NOW(), 'emorrison', NOW());

insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('kmfarland', 'omid@thinkparity.dyndns.org', 'kmfarland', NOW(), 'kmfarland', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('kmfarland', 'rkroeker@thinkparity.dyndns.org', 'kmfarland', NOW(), 'kmfarland', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('kmfarland', 'robert@thinkparity.dyndns.org', 'kmfarland', NOW(), 'kmfarland', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('kmfarland', 'djohnson@thinkparity.dyndns.org', 'kmfarland', NOW(), 'kmfarland', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('kmfarland', 'emorrison@thinkparity.dyndns.org', 'kmfarland', NOW(), 'kmfarland', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('kmfarland', 'strimnell@thinkparity.dyndns.org', 'kmfarland', NOW(), 'kmfarland', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('kmfarland', 'kenjiro@thinkparity.dyndns.org', 'kmfarland', NOW(), 'kmfarland', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('kmfarland', 'masako@thinkparity.dyndns.org', 'kmfarland', NOW(), 'kmfarland', NOW());

insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('strimnell', 'omid@thinkparity.dyndns.org', 'strimnell', NOW(), 'strimnell', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('strimnell', 'rkroeker@thinkparity.dyndns.org', 'strimnell', NOW(), 'strimnell', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('strimnell', 'robert@thinkparity.dyndns.org', 'strimnell', NOW(), 'strimnell', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('strimnell', 'djohnson@thinkparity.dyndns.org', 'strimnell', NOW(), 'strimnell', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('strimnell', 'emorrison@thinkparity.dyndns.org', 'strimnell', NOW(), 'strimnell', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('strimnell', 'kmfarland@thinkparity.dyndns.org', 'strimnell', NOW(), 'strimnell', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('strimnell', 'kenjiro@thinkparity.dyndns.org', 'strimnell', NOW(), 'strimnell', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('strimnell', 'masako@thinkparity.dyndns.org', 'strimnell', NOW(), 'strimnell', NOW());

insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('kenjiro', 'omid@thinkparity.dyndns.org', 'kenjiro', NOW(), 'kenjiro', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('kenjiro', 'rkroeker@thinkparity.dyndns.org', 'kenjiro', NOW(), 'kenjiro', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('kenjiro', 'robert@thinkparity.dyndns.org', 'kenjiro', NOW(), 'kenjiro', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('kenjiro', 'djohnson@thinkparity.dyndns.org', 'kenjiro', NOW(), 'kenjiro', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('kenjiro', 'emorrison@thinkparity.dyndns.org', 'kenjiro', NOW(), 'kenjiro', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('kenjiro', 'kmfarland@thinkparity.dyndns.org', 'kenjiro', NOW(), 'kenjiro', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('kenjiro', 'strimnell@thinkparity.dyndns.org', 'kenjiro', NOW(), 'kenjiro', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('kenjiro', 'masako@thinkparity.dyndns.org', 'kenjiro', NOW(), 'kenjiro', NOW());

insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('masako', 'omid@thinkparity.dyndns.org', 'masako', NOW(), 'masako', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('masako', 'rkroeker@thinkparity.dyndns.org', 'masako', NOW(), 'masako', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('masako', 'robert@thinkparity.dyndns.org', 'masako', NOW(), 'masako', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('masako', 'djohnson@thinkparity.dyndns.org', 'masako', NOW(), 'masako', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('masako', 'emorrison@thinkparity.dyndns.org', 'masako', NOW(), 'masako', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('masako', 'kmfarland@thinkparity.dyndns.org', 'masako', NOW(), 'masako', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('masako', 'strimnell@thinkparity.dyndns.org', 'masako', NOW(), 'masako', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('masako', 'kenjiro@thinkparity.dyndns.org', 'masako', NOW(), 'masako', NOW());

insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('rwaterhouse', 'rkroeker@thinkparity.dyndns.org', 'rwaterhouse', NOW(), 'rwaterhouse', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('rwaterhouse', 'ahalaby@thinkparity.dyndns.org', 'rwaterhouse', NOW(), 'rwaterhouse', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('rwaterhouse', 'ashaftoe@thinkparity.dyndns.org', 'rwaterhouse', NOW(), 'rwaterhouse', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('rwaterhouse', 'brussell@thinkparity.dyndns.org', 'rwaterhouse', NOW(), 'rwaterhouse', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('rwaterhouse', 'gdengo@thinkparity.dyndns.org', 'rwaterhouse', NOW(), 'rwaterhouse', NOW());

insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('ahalaby', 'rkroeker@thinkparity.dyndns.org', 'ahalaby', NOW(), 'ahalaby', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('ahalaby', 'rwaterhouse@thinkparity.dyndns.org', 'ahalaby', NOW(), 'ahalaby', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('ahalaby', 'ashaftoe@thinkparity.dyndns.org', 'ahalaby', NOW(), 'ahalaby', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('ahalaby', 'brussell@thinkparity.dyndns.org', 'ahalaby', NOW(), 'ahalaby', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('ahalaby', 'gdengo@thinkparity.dyndns.org', 'ahalaby', NOW(), 'ahalaby', NOW());

insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('ashaftoe', 'rkroeker@thinkparity.dyndns.org', 'ashaftoe', NOW(), 'ashaftoe', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('ashaftoe', 'ahalaby@thinkparity.dyndns.org', 'ashaftoe', NOW(), 'ashaftoe', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('ashaftoe', 'rwaterhouse@thinkparity.dyndns.org', 'ashaftoe', NOW(), 'ashaftoe', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('ashaftoe', 'brussell@thinkparity.dyndns.org', 'ashaftoe', NOW(), 'ashaftoe', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('ashaftoe', 'gdengo@thinkparity.dyndns.org', 'ashaftoe', NOW(), 'ashaftoe', NOW());

insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('brussell', 'rkroeker@thinkparity.dyndns.org', 'brussell', NOW(), 'brussell', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('brussell', 'ahalaby@thinkparity.dyndns.org', 'brussell', NOW(), 'brussell', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('brussell', 'rwaterhouse@thinkparity.dyndns.org', 'brussell', NOW(), 'brussell', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('brussell', 'ashaftoe@thinkparity.dyndns.org', 'brussell', NOW(), 'brussell', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('brussell', 'gdengo@thinkparity.dyndns.org', 'brussell', NOW(), 'brussell', NOW());

insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('gdengo', 'rkroeker@thinkparity.dyndns.org', 'gdengo', NOW(), 'gdengo', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('gdengo', 'ahalaby@thinkparity.dyndns.org', 'gdengo', NOW(), 'gdengo', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('gdengo', 'rwaterhouse@thinkparity.dyndns.org', 'gdengo', NOW(), 'gdengo', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('gdengo', 'ashaftoe@thinkparity.dyndns.org', 'gdengo', NOW(), 'gdengo', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('gdengo', 'brussell@thinkparity.dyndns.org', 'gdengo', NOW(), 'gdengo', NOW());
