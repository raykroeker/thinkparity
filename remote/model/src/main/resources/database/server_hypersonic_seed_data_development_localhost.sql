insert into jiveProperty (NAME,PROPVALUE)
    values('xmpp.auth.anonymous','true');
insert into jiveProperty (NAME,PROPVALUE)
    values('xmpp.domain','thinkparity.dyndns.org');
insert into jiveProperty (NAME,PROPVALUE)
    values('xmpp.server.socket.port','5271');
insert into jiveProperty (NAME,PROPVALUE)
    values('xmpp.socket.plain.port','5226');
insert into jiveProperty (NAME,PROPVALUE)
    values('xmpp.socket.ssl.active','true');
insert into jiveProperty (NAME,PROPVALUE)
    values('xmpp.socket.ssl.keypass','password');
insert into jiveProperty (NAME,PROPVALUE)
    values('xmpp.socket.ssl.port','5227');

insert into jiveProperty (NAME,PROPVALUE)
    values('thinkparity.environment','DEVELOPMENT_LOCALHOST');
insert into PARITY_FEATURE (FEATURE)
    values('ARCHIVE');

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('thinkparity','parity',0,0);
insert into jiveVCard (USERNAME,VALUE)
    values('thinkparity', '<vCard xmlns="vcard-temp"><FN>thinkParity Solutions Inc.</FN><N><FAMILY>thinkParity Solutions Inc.</FAMILY><GIVEN></GIVEN></N><ORG><ORGNAME>thinkParity Solutions Inc.</ORGNAME></ORG></vCard>');
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('thinkparity', 'thinkParity@thinkparity.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED)
    values('thinkparity','What is my username?','thinkparity',false);

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('rkroeker','parity',0,0);
insert into jiveVCard (USERNAME,VALUE)
    values('rkroeker', '<vCard xmlns="vcard-temp"><FN>Raymond Kroeker</FN><N><FAMILY>Kroeker</FAMILY><GIVEN>Raymond</GIVEN></N><ORG><ORGNAME>thinkParity Solutions Inc.</ORGNAME></ORG></vCard>');
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('rkroeker', 'raymond@thinkparity.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED)
    values('rkroeker','What is my username?','rkroeker',false);
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
	values ('rkroeker', 1000);
insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('thinkparity.archive.0','parity',0,0);
insert into jiveVCard (USERNAME,VALUE)
    values('thinkparity.archive.0', '<vCard xmlns="vcard-temp"><FN>Archive</FN><N><FAMILY>Archive</FAMILY><GIVEN></GIVEN></N><ORG><ORGNAME>thinkParity Solutions Inc.</ORGNAME></ORG></vCard>');
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED)
    values('thinkparity.archive.0','What is my username?','thinkparity.archive.0',false);
insert into PARITY_USER_ARCHIVE_REL (USERNAME,ARCHIVENAME)
    values('rkroeker', 'thinkparity.archive.0');

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('ahalaby','parity',0,0);
insert into jiveVCard (USERNAME,VALUE)
    values('ahalaby', '<vCard xmlns="vcard-temp"><FN>Avi Halaby</FN><N><FAMILY>Halaby</FAMILY><GIVEN>Avi</GIVEN></N><ORG><ORGNAME>Epiphyte Corporation Inc.</ORGNAME></ORG></vCard>');
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('ahalaby', 'avi@epiphyte.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED)
    values('ahalaby','What is my username?','ahalaby',false);
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
	values ('ahalaby', 1000);
insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('thinkparity.archive.1','parity',0,0);
insert into jiveVCard (USERNAME,VALUE)
    values('thinkparity.archive.1', '<vCard xmlns="vcard-temp"><FN>Archive</FN><N><FAMILY>Archive</FAMILY><GIVEN></GIVEN></N><ORG><ORGNAME>thinkParity Solutions Inc.</ORGNAME></ORG></vCard>');
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED)
    values('thinkparity.archive.1','What is my username?','thinkparity.archive.1',false);
insert into PARITY_USER_ARCHIVE_REL (USERNAME,ARCHIVENAME)
    values('ahalaby', 'thinkparity.archive.1');

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('rwaterhouse','parity',0,0);
insert into jiveVCard (USERNAME,VALUE)
    values('rwaterhouse', '<vCard xmlns="vcard-temp"><FN>Randy Waterhouse</FN><N><FAMILY>Waterhouse</FAMILY><GIVEN>Randy</GIVEN></N><ORG><ORGNAME>Epiphyte Corporation Inc.</ORGNAME></ORG></vCard>');
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('rwaterhouse', 'randy@epiphyte.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED)
    values('rwaterhouse','What is my username?','rwaterhouse',false);
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
	values ('rwaterhouse', 1000);
insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('thinkparity.archive.2','parity',0,0);
insert into jiveVCard (USERNAME,VALUE)
    values('thinkparity.archive.2', '<vCard xmlns="vcard-temp"><FN>Archive</FN><N><FAMILY>Archive</FAMILY><GIVEN></GIVEN></N><ORG><ORGNAME>thinkParity Solutions Inc.</ORGNAME></ORG></vCard>');
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED)
    values('thinkparity.archive.2','What is my username?','thinkparity.archive.2',false);
insert into PARITY_USER_ARCHIVE_REL (USERNAME,ARCHIVENAME)
    values('rwaterhouse', 'thinkparity.archive.2');

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('ashaftoe','parity',0,0);
insert into jiveVCard (USERNAME,VALUE)
    values('ashaftoe', '<vCard xmlns="vcard-temp"><FN>Amy Shaftoe</FN><N><FAMILY>Shaftoe</FAMILY><GIVEN>Amy</GIVEN></N><ORG><ORGNAME>Semper Marine Group</ORGNAME></ORG></vCard>');
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('ashaftoe', 'amy@sempermarine.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED)
    values('ashaftoe','What is my username?','ashaftoe',false);
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
	values ('ashaftoe', 1000);
insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('thinkparity.archive.3','parity',0,0);
insert into jiveVCard (USERNAME,VALUE)
    values('thinkparity.archive.3', '<vCard xmlns="vcard-temp"><FN>Archive</FN><N><FAMILY>Archive</FAMILY><GIVEN></GIVEN></N><ORG><ORGNAME>thinkParity Solutions Inc.</ORGNAME></ORG></vCard>');
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED)
    values('thinkparity.archive.3','What is my username?','thinkparity.archive.3',false);
insert into PARITY_USER_ARCHIVE_REL (USERNAME,ARCHIVENAME)
    values('ashaftoe', 'thinkparity.archive.3');

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('brussell','parity',0,0);
insert into jiveVCard (USERNAME,VALUE)
    values('brussell', '<vCard xmlns="vcard-temp"><FN>Bertrand Russell</FN><N><FAMILY>Russell</FAMILY><GIVEN>Bertrand</GIVEN></N><ORG><ORGNAME>thinkParity Solutions Inc.</ORGNAME></ORG></vCard>');
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('brussell', 'bertrand@thinkparity.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED)
    values('brussell','What is my username?','brussell',false);
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
	values ('brussell', 1000);
insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('thinkparity.archive.4','parity',0,0);
insert into jiveVCard (USERNAME,VALUE)
    values('thinkparity.archive.4', '<vCard xmlns="vcard-temp"><FN>Archive</FN><N><FAMILY>Archive</FAMILY><GIVEN></GIVEN></N><ORG><ORGNAME>thinkParity Solutions Inc.</ORGNAME></ORG></vCard>');
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED)
    values('thinkparity.archive.4','What is my username?','thinkparity.archive.4',false);
insert into PARITY_USER_ARCHIVE_REL (USERNAME,ARCHIVENAME)
    values('brussell', 'thinkparity.archive.4');

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('gdengo','parity',0,0);
insert into jiveVCard (USERNAME,VALUE)
    values('gdengo', '<vCard xmlns="vcard-temp"><FN>Goto Dengo</FN><N><FAMILY>Dengo</FAMILY><GIVEN>Goto</GIVEN></N><ORG><ORGNAME>Goto Engineering Inc.</ORGNAME></ORG></vCard>');
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('gdengo', 'goto@gotoengineering.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED)
    values('gdengo','What is my username?','gdengo',false);
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
	values ('gdengo', 1000);
insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('thinkparity.archive.5','parity',0,0);
insert into jiveVCard (USERNAME,VALUE)
    values('thinkparity.archive.5', '<vCard xmlns="vcard-temp"><FN>Archive</FN><N><FAMILY>Archive</FAMILY><GIVEN></GIVEN></N><ORG><ORGNAME>thinkParity Solutions Inc.</ORGNAME></ORG></vCard>');
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED)
    values('thinkparity.archive.5','What is my username?','thinkparity.archive.5',false);
insert into PARITY_USER_ARCHIVE_REL (USERNAME,ARCHIVENAME)
    values('gdengo', 'thinkparity.archive.5');

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
