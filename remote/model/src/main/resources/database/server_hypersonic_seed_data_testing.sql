insert into jiveProperty (NAME,PROPVALUE)
    values('xmpp.auth.anonymous', 'true');
insert into jiveProperty (NAME,PROPVALUE)
    values('xmpp.domain', 'thinkparity.dyndns.org');
insert into jiveProperty (NAME,PROPVALUE)
    values('xmpp.socket.plain.port', '5224');
insert into jiveProperty (NAME,PROPVALUE)
    values('xmpp.socket.ssl.port', '5225');
insert into jiveProperty (NAME,PROPVALUE)
    values('com.thinkparity.calpurnia.db.driver', 'org.hsqldb.jdbcDriver');
insert into jiveProperty (NAME,PROPVALUE)
    values('com.thinkparity.calpurnia.db.password', '');
insert into jiveProperty (NAME,PROPVALUE)
    values('com.thinkparity.calpurnia.db.url;jdbc:hsqldb', 'file:/home/jive/thinkParity/testing/hsqldb/calpurnia/db');
insert into jiveProperty (NAME,PROPVALUE)
    values('com.thinkparity.calpurnia.db.username', 'sa');
insert into PARITY_FEATURE (FEATURE_NAME)
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
    values('rkroeker','parity',0,0);
insert into jiveVCard (USERNAME,VALUE)
    values('rkroeker', '<vCard xmlns="vcard-temp"><FN>Raymond Kroeker</FN><N><FAMILY>Kroeker</FAMILY><GIVEN>Raymond</GIVEN></N><ORG><ORGNAME>thinkParity Solutions Inc.</ORGNAME></ORG></vCard>');
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('rkroeker', 'raymond@thinkparity.com', true);
insert into parityUserProfile (USERNAME,SECURITYQUESTION,SECURITYANSWER)
    values('rkroeker', 'What is my username?', 'rkroeker');
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
	values ('rkroeker', 1000);

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('ahalaby','parity',0,0);
insert into jiveVCard (USERNAME,VALUE)
    values('ahalaby', '<vCard xmlns="vcard-temp"><FN>Avi Halaby</FN><N><FAMILY>Halaby</FAMILY><GIVEN>Avi</GIVEN></N><ORG><ORGNAME>Epiphyte Corporation Inc.</ORGNAME></ORG></vCard>');
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('ahalaby', 'avi@epiphyte.com', true);
insert into parityUserProfile (USERNAME,SECURITYQUESTION,SECURITYANSWER)
    values('ahalaby', 'What is my username?', 'ahalaby');
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
	values ('ahalaby', 1000);

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('rwaterhouse','parity',0,0);
insert into jiveVCard (USERNAME,VALUE)
    values('rwaterhouse', '<vCard xmlns="vcard-temp"><FN>Randy Waterhouse</FN><N><FAMILY>Waterhouse</FAMILY><GIVEN>Randy</GIVEN></N><ORG><ORGNAME>Epiphyte Corporation Inc.</ORGNAME></ORG></vCard>');
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('rwaterhouse', 'randy@epiphyte.com', true);
insert into parityUserProfile (USERNAME,SECURITYQUESTION,SECURITYANSWER)
    values('rwaterhouse', 'What is my username?', 'rwaterhouse');
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
	values ('rwaterhouse', 1000);

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('ashaftoe','parity',0,0);
insert into jiveVCard (USERNAME,VALUE)
    values('ashaftoe', '<vCard xmlns="vcard-temp"><FN>Amy Shaftoe</FN><N><FAMILY>Shaftoe</FAMILY><GIVEN>Amy</GIVEN></N><ORG><ORGNAME>Semper Marine Group</ORGNAME></ORG></vCard>');
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('ashaftoe', 'amy@sempermarine.com', true);
insert into parityUserProfile (USERNAME,SECURITYQUESTION,SECURITYANSWER)
    values('ashaftoe', 'What is my username?', 'ashaftoe');
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
	values ('ashaftoe', 1000);

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('brussell','parity',0,0);
insert into jiveVCard (USERNAME,VALUE)
    values('brussell', '<vCard xmlns="vcard-temp"><FN>Bertrand Russell</FN><N><FAMILY>Russell</FAMILY><GIVEN>Bertrand</GIVEN></N><ORG><ORGNAME>thinkParity Solutions Inc.</ORGNAME></ORG></vCard>');
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('brussell', 'bertrand@thinkparity.com', true);
insert into parityUserProfile (USERNAME,SECURITYQUESTION,SECURITYANSWER)
    values('brussell', 'What is my username?', 'brussell');
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
	values ('brussell', 1000);

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('gdengo','parity',0,0);
insert into jiveVCard (USERNAME,VALUE)
    values('gdengo', '<vCard xmlns="vcard-temp"><FN>Goto Dengo</FN><N><FAMILY>Dengo</FAMILY><GIVEN>Goto</GIVEN></N><ORG><ORGNAME>Goto Engineering Inc.</ORGNAME></ORG></vCard>');
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('gdengo', 'goto@gotoengineering.com', true);
insert into parityUserProfile (USERNAME,SECURITYQUESTION,SECURITYANSWER)
    values('gdengo', 'What is my username?', 'gdengo');
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
	values ('gdengo', 1000);

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('junit','parity',0,0);
insert into jiveVCard (USERNAME,VALUE)
    values('junit', '<vCard xmlns="vcard-temp"><FN>JUnit thinkParity</FN><N><FAMILY>thinkParity</FAMILY><GIVEN>JUnit</GIVEN></N><ORG><ORGNAME>thinkParity Solutions Inc.</ORGNAME></ORG><TITLE>Test User</title></vCard>');
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('junit', 'junit@thinkparity.com', true);
insert into parityUserProfile (USERNAME,SECURITYQUESTION,SECURITYANSWER)
    values('junit', 'What is my username?', 'junit');
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
	values ('junit', 1000);

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('junit.x','parity',0,0);
insert into jiveVCard (USERNAME,VALUE)
    values('junit.x', '<vCard xmlns="vcard-temp"><FN>JUnit.X thinkParity</FN><N><FAMILY>thinkParity</FAMILY><GIVEN>JUnit.X</GIVEN></N><ORG><ORGNAME>thinkParity Solutions Inc.</ORGNAME></ORG><TITLE>Test User</title></vCard>');
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('junit.x', 'junit.x@thinkparity.com', true);
insert into parityUserProfile (USERNAME,SECURITYQUESTION,SECURITYANSWER)
    values('junit.x', 'What is my username?', 'junit.x');
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
	values ('junit.x', 1000);

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('junit.y','parity',0,0);
insert into jiveVCard (USERNAME,VALUE)
    values('junit.y', '<vCard xmlns="vcard-temp"><FN>JUnit.Y thinkParity</FN><N><FAMILY>thinkParity</FAMILY><GIVEN>JUnit.Y</GIVEN></N><ORG><ORGNAME>thinkParity Solutions Inc.</ORGNAME></ORG><TITLE>Test User</title></vCard>');
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('junit.y', 'junit.y@thinkparity.com', true);
insert into parityUserProfile (USERNAME,SECURITYQUESTION,SECURITYANSWER)
    values('junit.y', 'What is my username?', 'junit.y');
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
	values ('junit.y', 1000);

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('junit.z','parity',0,0);
insert into jiveVCard (USERNAME,VALUE)
    values('junit.z', '<vCard xmlns="vcard-temp"><FN>JUnit.Z thinkParity</FN><N><FAMILY>thinkParity</FAMILY><GIVEN>JUnit.Z</GIVEN></N><ORG><ORGNAME>thinkParity Solutions Inc.</ORGNAME></ORG><TITLE>Test User</title></vCard>');
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('junit.z', 'junit.z@thinkparity.com', true);
insert into parityUserProfile (USERNAME,SECURITYQUESTION,SECURITYANSWER)
    values('junit.z', 'What is my username?', 'junit.z');
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
	values ('junit.z', 1000);

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
