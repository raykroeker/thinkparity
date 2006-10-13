insert into jiveProperty (NAME,PROPVALUE)
    values('xmpp.auth.anonymous', 'true');
insert into jiveProperty (NAME,PROPVALUE)
    values('xmpp.domain', 'thinkparity.dyndns.org');
insert into jiveProperty (NAME,PROPVALUE)
    values ('xmpp.server.socket.port', '5269');
insert into jiveProperty (NAME,PROPVALUE)
    values('xmpp.socket.plain.port', '5222');
insert into jiveProperty (NAME,PROPVALUE)
    values('xmpp.socket.ssl.port', '5223');
insert into jiveProperty (NAME,PROPVALUE)
    values('thinkparity.environment', 'PRODUCTION');
insert into PARITY_FEATURE (FEATURE)
    values('ARCHIVE');

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('thinkparity','parity',0,0);
insert into jiveVCard (USERNAME,VALUE)
    values('thinkparity', '<vCard xmlns="vcard-temp"><FN>thinkParity Solutions Inc.</FN><N><FAMILY>thinkParity Solutions Inc.</FAMILY><GIVEN></GIVEN></N><ORG><ORGNAME>thinkParity Solutions Inc.</ORGNAME></ORG></vCard>');
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('thinkparity', 'thinkParity@thinkparity.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED)
    values('thinkparity', 'What is my username?', 'thinkparity',false);
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
	values ('thinkparity', 1000);

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('asahebjam','parity',0,0);
insert into jiveVCard (USERNAME,VALUE)
    values('asahebjam', '<vCard xmlns="vcard-temp"><FN>Amir Sahebjam</FN><N><FAMILY>Sahebjam</FAMILY><GIVEN>Amir</GIVEN></N><ORG><ORGNAME>thinkParity Solutions Inc.</ORGNAME></ORG></vCard>');
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('asahebjam', 'asahebjam@gmail.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED)
    values('asahebjam', 'What is my username?', 'asahebjam',false);
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
	values ('asahebjam', 1000);

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('omid','parity',0,0);
insert into jiveVCard (USERNAME,VALUE)
    values('omid', '<vCard xmlns="vcard-temp"><FN>Omid Ejtemai</FN><N><FAMILY>Ejtemai</FAMILY><GIVEN>Omid</GIVEN></N><ORG><ORGNAME>thinkParity Solutions Inc.</ORGNAME></ORG></vCard>');
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('omid', 'omid@thinkparity.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED)
    values('omid', 'What is my username?', 'omid',false);
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
	values ('omid', 1000);
insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('thinkparity.archive.0','parity',0,0);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED)
    values('thinkparity.archive.0','What is my username?','thinkparity.archive.0',false);
insert into jiveVCard (USERNAME,VALUE)
    values('thinkparity.archive.0', '<vCard xmlns="vcard-temp"><FN>Archive</FN><N><FAMILY>Archive</FAMILY><GIVEN></GIVEN></N><ORG><ORGNAME>thinkParity Solutions Inc.</ORGNAME></ORG></vCard>');
insert into PARITY_USER_ARCHIVE_REL (USERNAME,ARCHIVENAME)
    values('omid', 'thinkparity.archive.0');

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('rkroeker','parity',0,0);
insert into jiveVCard (USERNAME,VALUE)
    values('rkroeker', '<vCard xmlns="vcard-temp"><FN>Raymond Kroeker</FN><N><FAMILY>Kroeker</FAMILY><GIVEN>Raymond</GIVEN></N><ORG><ORGNAME>thinkParity Solutions Inc.</ORGNAME></ORG></vCard>');
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('rkroeker', 'raymond@thinkparity.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED)
    values('rkroeker', 'What is my username?', 'rkroeker',false);
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
	values ('rkroeker', 1000);

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('robert','parity',0,0);
insert into jiveVCard (USERNAME,VALUE)
    values('robert', '<vCard xmlns="vcard-temp"><FN>Robert MacMartin</FN><N><FAMILY>MacMartin</FAMILY><GIVEN>Robert</GIVEN></N><ORG><ORGNAME>thinkParity Solutions Inc.</ORGNAME></ORG></vCard>');
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('robert', 'robert@thinkparity.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED)
    values('robert', 'What is my username?', 'robert',false);
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
	values ('robert', 1000);

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('djohnson','parity',0,0);
insert into jiveVCard (USERNAME,VALUE)
    values('djohnson', '<vCard xmlns="vcard-temp"><FN>Don Johnson</FN><N><FAMILY>Johnson</FAMILY><GIVEN>Don</GIVEN></N><ORG><ORGNAME>thinkParity Solutions Inc.</ORGNAME></ORG></vCard>');
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('djohnson', 'djohnson@thinkparity.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED)
    values('djohnson', 'What is my username?', 'djohnson',false);
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
	values ('djohnson', 1000);

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('emorrison','parity',0,0);
insert into jiveVCard (USERNAME,VALUE)
    values('emorrison', '<vCard xmlns="vcard-temp"><FN>Emily Morrison</FN><N><FAMILY>Morrison</FAMILY><GIVEN>Emily</GIVEN></N><ORG><ORGNAME>thinkParity Solutions Inc.</ORGNAME></ORG></vCard>');
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('emorrison', 'emorrison@thinkparity.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED)
    values('emorrison', 'What is my username?', 'emorrison',false);
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
	values ('emorrison', 1000);

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('kmfarland','parity',0,0);
insert into jiveVCard (USERNAME,VALUE)
    values('kmfarland', '<vCard xmlns="vcard-temp"><FN>Kevin MacFarland</FN><N><FAMILY>MacFarland</FAMILY><GIVEN>Kevin</GIVEN></N><ORG><ORGNAME>thinkParity Solutions Inc.</ORGNAME></ORG></vCard>');
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('kmfarland', 'kmfarland@thinkparity.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED)
    values('kmfarland', 'What is my username?', 'kmfarland',false);
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
	values ('kmfarland', 1000);

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('strimnell','parity',0,0);
insert into jiveVCard (USERNAME,VALUE)
    values('strimnell', '<vCard xmlns="vcard-temp"><FN>Sarah Trimnell</FN><N><FAMILY>Trimnell</FAMILY><GIVEN>Sarah</GIVEN></N><ORG><ORGNAME>thinkParity Solutions Inc.</ORGNAME></ORG></vCard>');
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('strimnell', 'strimnell@thinkparity.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED)
    values('strimnell', 'What is my username?', 'strimnell',false);
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
	values ('strimnell', 1000);

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('kenjiro','parity',0,0);
insert into jiveVCard (USERNAME,VALUE)
    values('kenjiro', '<vCard xmlns="vcard-temp"><FN>Kenjiro MacMartin</FN><N><FAMILY>MacMartin</FAMILY><GIVEN>Kenjiro</GIVEN></N><ORG><ORGNAME>thinkParity Solutions Inc.</ORGNAME></ORG></vCard>');
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('kenjiro', 'kenjiro@thinkparity.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED)
    values('kenjiro', 'What is my username?', 'kenjiro',false);
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
	values ('kenjiro', 1000);

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('masako','parity',0,0);
insert into jiveVCard (USERNAME,VALUE)
    values('masako', '<vCard xmlns="vcard-temp"><FN>Masako</FN><N><FAMILY></FAMILY><GIVEN>Masako</GIVEN></N><ORG><ORGNAME>thinkParity Solutions Inc.</ORGNAME></ORG></vCard>');
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('masako', 'masako@thinkparity.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED)
    values('masako', 'What is my username?', 'masako',false);
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
	values ('masako', 1000);

insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('asahebjam', 'omid@thinkparity.dyndns.org', 'asahebjam', NOW(), 'asahebjam', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('asahebjam', 'rkroeker@thinkparity.dyndns.org', 'asahebjam', NOW(), 'asahebjam', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('asahebjam', 'robert@thinkparity.dyndns.org', 'asahebjam', NOW(), 'asahebjam', NOW());

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
