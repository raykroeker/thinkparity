insert into jiveProperty (NAME,PROPVALUE)
    values('xmpp.auth.anonymous','true');
insert into jiveProperty (NAME,PROPVALUE)
    values('xmpp.domain','thinkparity.net');
insert into jiveProperty (NAME,PROPVALUE)
    values('xmpp.server.socket.port','5270');
insert into jiveProperty (NAME,PROPVALUE)
    values('xmpp.socket.plain.port','5224');
insert into jiveProperty (NAME,PROPVALUE)
    values('xmpp.socket.ssl.active','true');
insert into jiveProperty (NAME,PROPVALUE)
    values('xmpp.socket.ssl.keypass','password');
insert into jiveProperty (NAME,PROPVALUE)
    values('xmpp.socket.ssl.port','5225');

insert into jiveProperty (NAME,PROPVALUE)
    values('thinkparity.environment','TESTING');
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
    values('junit','parity',0,0);
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('junit', 'junit@thinkparity.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('junit', 'What is my username?', 'junit',false,'<com.thinkparity.codebase.model.user.UserVCard><name>JUnit thinkParity</name></com.thinkparity.codebase.model.user.UserVCard>');

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('junit.w','parity',0,0);
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('junit.w', 'junit.w@thinkparity.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('junit.w','What is my username?','junit.w',false,'<com.thinkparity.codebase.model.user.UserVCard><name>JUnit.W thinkParity</name></com.thinkparity.codebase.model.user.UserVCard>');

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('junit.x','parity',0,0);
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('junit.x', 'junit.x@thinkparity.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('junit.x', 'What is my username?', 'junit.x',false,'<com.thinkparity.codebase.model.user.UserVCard><name>JUnit.X thinkParity</name></com.thinkparity.codebase.model.user.UserVCard>');

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('junit.y','parity',0,0);
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('junit.y', 'junit.y@thinkparity.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('junit.y', 'What is my username?', 'junit.y',false,'<com.thinkparity.codebase.model.user.UserVCard><name>JUnit.Y thinkParity</name></com.thinkparity.codebase.model.user.UserVCard>');

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('junit.z','parity',0,0);
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('junit.z', 'junit.z@thinkparity.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('junit.z', 'What is my username?', 'junit.z',false,'<com.thinkparity.codebase.model.user.UserVCard><name>JUnit.Z thinkParity</name></com.thinkparity.codebase.model.user.UserVCard>');
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
    values ('junit.z', 1000);
insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('thinkparity.archive.0','parity',0,0);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('thinkparity.archive.0','What is my username?','thinkparity.archive.0',false,'<com.thinkparity.codebase.model.user.UserVCard><name>JUnit.Z thinkParity Archive</name></com.thinkparity.codebase.model.user.UserVCard>');
insert into PARITY_USER_ARCHIVE_REL (USERNAME,ARCHIVENAME)
    values('junit.z', 'thinkparity.archive.0');

insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('junit', 'junit.x@thinkparity.net', 'junit', NOW(), 'junit', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('junit', 'junit.y@thinkparity.net', 'junit', NOW(), 'junit', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('junit', 'junit.z@thinkparity.net', 'junit', NOW(), 'junit', NOW());

insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('junit.x', 'junit@thinkparity.net', 'junit.x', NOW(), 'junit.x', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('junit.x', 'junit.y@thinkparity.net', 'junit.x', NOW(), 'junit.x', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('junit.x', 'junit.z@thinkparity.net', 'junit.x', NOW(), 'junit.x', NOW());

insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('junit.y', 'junit@thinkparity.net', 'junit.y', NOW(), 'junit.y', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('junit.y', 'junit.x@thinkparity.net', 'junit.y', NOW(), 'junit.y', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('junit.y', 'junit.z@thinkparity.net', 'junit.y', NOW(), 'junit.y', NOW());

insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('junit.z', 'junit@thinkparity.net', 'junit.z', NOW(), 'junit.z', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('junit.z', 'junit.x@thinkparity.net', 'junit.z', NOW(), 'junit.z', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('junit.z', 'junit.y@thinkparity.net', 'junit.z', NOW(), 'junit.z', NOW());
