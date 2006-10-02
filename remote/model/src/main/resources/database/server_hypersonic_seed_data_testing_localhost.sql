insert into jiveProperty (NAME,PROPVALUE)
    values('xmpp.auth.anonymous', 'true');
insert into jiveProperty (NAME,PROPVALUE)
    values('xmpp.domain', 'thinkparity.dyndns.org');
insert into jiveProperty (NAME,PROPVALUE)
    values ('xmpp.server.socket.port', '5270');
insert into jiveProperty (NAME,PROPVALUE)
    values('xmpp.socket.plain.port', '5224');
insert into jiveProperty (NAME,PROPVALUE)
    values('xmpp.socket.ssl.port', '5225');
insert into jiveProperty (NAME,PROPVALUE)
    values('thinkparity.environment', 'TESTING_LOCALHOST');
insert into PARITY_FEATURE (FEATURE)
	values ('ARCHIVE');

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('thinkparity','parity',0,0);
insert into jiveVCard (USERNAME,VALUE)
    values('thinkparity', '<vCard xmlns="vcard-temp"><FN>thinkParity Solutions Inc.</FN><N><FAMILY>thinkParity Solutions Inc.</FAMILY><GIVEN></GIVEN></N><ORG><ORGNAME>thinkParity Solutions Inc.</ORGNAME></ORG></vCard>');
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('thinkparity', 'thinkParity@thinkparity.com', true);
insert into parityUserProfile (USERNAME,SECURITYQUESTION,SECURITYANSWER)
    values('thinkparity', 'What is my username?', 'thinkparity');
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
	values ('thinkparity', 1000);

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('junit','parity',0,0);
insert into jiveVCard (USERNAME,VALUE)
    values('junit', '<vCard xmlns="vcard-temp"><FN>JUnit thinkParity</FN><N><FAMILY>thinkParity</FAMILY><GIVEN>JUnit</GIVEN></N><ORG><ORGNAME>thinkParity Solutions Inc.</ORGNAME></ORG><TITLE>Test User</TITLE></vCard>');
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('junit', 'junit@thinkparity.com', true);
insert into parityUserProfile (USERNAME,SECURITYQUESTION,SECURITYANSWER)
    values('junit', 'What is my username?', 'junit');
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
	values ('junit', 1000);
insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('thinkparity.archive.0','parity',0,0);
insert into jiveVCard (USERNAME,VALUE)
    values('thinkparity.archive.0', '<vCard xmlns="vcard-temp"><FN>Archive</FN><N><FAMILY>Archive</FAMILY><GIVEN></GIVEN></N><ORG><ORGNAME>thinkParity Solutions Inc.</ORGNAME></ORG></vCard>');
insert into PARITY_USER_ARCHIVE_REL (USERNAME,ARCHIVENAME)
    values('junit', 'thinkparity.archive.0');

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('junit.x','parity',0,0);
insert into jiveVCard (USERNAME,VALUE)
    values('junit.x', '<vCard xmlns="vcard-temp"><FN>JUnit.X thinkParity</FN><N><FAMILY>thinkParity</FAMILY><GIVEN>JUnit.X</GIVEN></N><ORG><ORGNAME>thinkParity Solutions Inc.</ORGNAME></ORG><TITLE>Test User</TITLE></vCard>');
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('junit.x', 'junit.x@thinkparity.com', true);
insert into parityUserProfile (USERNAME,SECURITYQUESTION,SECURITYANSWER)
    values('junit.x', 'What is my username?', 'junit.x');

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('junit.y','parity',0,0);
insert into jiveVCard (USERNAME,VALUE)
    values('junit.y', '<vCard xmlns="vcard-temp"><FN>JUnit.Y thinkParity</FN><N><FAMILY>thinkParity</FAMILY><GIVEN>JUnit.Y</GIVEN></N><ORG><ORGNAME>thinkParity Solutions Inc.</ORGNAME></ORG><TITLE>Test User</TITLE></vCard>');
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('junit.y', 'junit.y@thinkparity.com', true);
insert into parityUserProfile (USERNAME,SECURITYQUESTION,SECURITYANSWER)
    values('junit.y', 'What is my username?', 'junit.y');

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('junit.z','parity',0,0);
insert into jiveVCard (USERNAME,VALUE)
    values('junit.z', '<vCard xmlns="vcard-temp"><FN>JUnit.Z thinkParity</FN><N><FAMILY>thinkParity</FAMILY><GIVEN>JUnit.Z</GIVEN></N><ORG><ORGNAME>thinkParity Solutions Inc.</ORGNAME></ORG><TITLE>Test User</TITLE></vCard>');
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('junit.z', 'junit.z@thinkparity.com', true);
insert into parityUserProfile (USERNAME,SECURITYQUESTION,SECURITYANSWER)
    values('junit.z', 'What is my username?', 'junit.z');

insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('junit', 'junit.x@thinkparity.dyndns.org', 'junit', NOW(), 'junit', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('junit', 'junit.y@thinkparity.dyndns.org', 'junit', NOW(), 'junit', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('junit', 'junit.z@thinkparity.dyndns.org', 'junit', NOW(), 'junit', NOW());

insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('junit.x', 'junit@thinkparity.dyndns.org', 'junit.x', NOW(), 'junit.x', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('junit.x', 'junit.y@thinkparity.dyndns.org', 'junit.x', NOW(), 'junit.x', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('junit.x', 'junit.z@thinkparity.dyndns.org', 'junit.x', NOW(), 'junit.x', NOW());

insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('junit.y', 'junit@thinkparity.dyndns.org', 'junit.y', NOW(), 'junit.y', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('junit.y', 'junit.x@thinkparity.dyndns.org', 'junit.y', NOW(), 'junit.y', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('junit.y', 'junit.z@thinkparity.dyndns.org', 'junit.y', NOW(), 'junit.y', NOW());

insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('junit.z', 'junit@thinkparity.dyndns.org', 'junit.z', NOW(), 'junit.z', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('junit.z', 'junit.x@thinkparity.dyndns.org', 'junit.z', NOW(), 'junit.z', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('junit.z', 'junit.y@thinkparity.dyndns.org', 'junit.z', NOW(), 'junit.z', NOW());
