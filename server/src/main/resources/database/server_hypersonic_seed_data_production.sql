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
insert into PARITY_FEATURE (FEATURE)
    values('ARCHIVE');

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('thinkparity','parity',0,0);
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('thinkparity', 'thinkParity@thinkparity.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('thinkparity', 'What is my username?', 'thinkparity',false,'<com.thinkparity.codebase.model.user.UserVCard><name>thinkParity Solutions Inc.</name></com.thinkparity.codebase.model.user.UserVCard>');

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('asahebjam','parity',0,0);
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('asahebjam', 'asahebjam@gmail.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('asahebjam', 'What is my username?', 'asahebjam',false,'<com.thinkparity.codebase.model.user.UserVCard><name>Amir Sahebjam</name></com.thinkparity.codebase.model.user.UserVCard>');
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
	values ('asahebjam', 1000);

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('omid','parity',0,0);
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('omid', 'omid@thinkparity.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('omid', 'What is my username?', 'omid',false,'<com.thinkparity.codebase.model.user.UserVCard><name>Omid Ejtemai</name></com.thinkparity.codebase.model.user.UserVCard>');
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
	values ('omid', 1000);
insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('omid-archive','parity',0,0);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('omid-archive','What is my username?','omid-archive',false,'<com.thinkparity.codebase.model.user.UserVCard><name>Omid Ejtemai Archive</name></com.thinkparity.codebase.model.user.UserVCard>');
insert into PARITY_USER_ARCHIVE_REL (USERNAME,ARCHIVENAME)
    values('omid', 'omid-archive');

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('rkroeker','parity',0,0);
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('rkroeker', 'raymond@thinkparity.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('rkroeker', 'What is my username?', 'rkroeker',false,'<com.thinkparity.codebase.model.user.UserVCard><name>Raymond Kroeker</name></com.thinkparity.codebase.model.user.UserVCard>');
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
	values ('rkroeker', 1000);

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('robert','parity',0,0);
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('robert', 'robert@thinkparity.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('robert', 'What is my username?', 'robert',false,'<com.thinkparity.codebase.model.user.UserVCard><name>Robert MacMartin</name></com.thinkparity.codebase.model.user.UserVCard>');
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
	values ('robert', 1000);

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('djohnson','parity',0,0);
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('djohnson', 'djohnson@thinkparity.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('djohnson', 'What is my username?', 'djohnson',false,'<com.thinkparity.codebase.model.user.UserVCard><name>Don Johnson</name></com.thinkparity.codebase.model.user.UserVCard>');
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
	values ('djohnson', 1000);

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('emorrison','parity',0,0);
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('emorrison', 'emorrison@thinkparity.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('emorrison', 'What is my username?', 'emorrison',false,'<com.thinkparity.codebase.model.user.UserVCard><name>Emily Morrison</name></com.thinkparity.codebase.model.user.UserVCard>');
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
	values ('emorrison', 1000);

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('kmfarland','parity',0,0);
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('kmfarland', 'kmfarland@thinkparity.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('kmfarland', 'What is my username?', 'kmfarland',false,'<com.thinkparity.codebase.model.user.UserVCard><name>Kevin MacFarland</name></com.thinkparity.codebase.model.user.UserVCard>');
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
	values ('kmfarland', 1000);

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('strimnell','parity',0,0);
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('strimnell', 'strimnell@thinkparity.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('strimnell', 'What is my username?', 'strimnell',false,'<com.thinkparity.codebase.model.user.UserVCard><name>Sarah Trimnell</name></com.thinkparity.codebase.model.user.UserVCard>');
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
	values ('strimnell', 1000);

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('kenjiro','parity',0,0);
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('kenjiro', 'kenjiro@thinkparity.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('kenjiro', 'What is my username?', 'kenjiro',false,'<com.thinkparity.codebase.model.user.UserVCard><name>Kenjiro MacMartin</name></com.thinkparity.codebase.model.user.UserVCard>');
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
	values ('kenjiro', 1000);

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('masako','parity',0,0);
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('masako', 'masako@thinkparity.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('masako', 'What is my username?', 'masako',false,'<com.thinkparity.codebase.model.user.UserVCard><name>Masako Saito</name></com.thinkparity.codebase.model.user.UserVCard>');
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
	values ('masako', 1000);

insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('asahebjam', 'omid@thinkparity.net', 'asahebjam', NOW(), 'asahebjam', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('asahebjam', 'rkroeker@thinkparity.net', 'asahebjam', NOW(), 'asahebjam', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('asahebjam', 'robert@thinkparity.net', 'asahebjam', NOW(), 'asahebjam', NOW());

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
