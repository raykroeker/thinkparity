insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('rkroeker','parity',0,0);
insert into jiveVCard (USERNAME,VALUE)
    values('rkroeker', '<vCard xmlns="vcard-temp"><FN>Raymond Kroeker</FN><N><FAMILY>Kroeker</FAMILY><GIVEN>Raymond</GIVEN></N><ORG><ORGNAME>thinkParity Solutions Inc.</ORGNAME></ORG></vCard>');
insert into parityUserEmail (USERNAME,EMAIL)
    values('rkroeker', 'raymond@thinkparity.com');

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('ahalaby','parity',0,0);
insert into jiveVCard (USERNAME,VALUE)
    values('ahalaby', '<vCard xmlns="vcard-temp"><FN>Avi Halaby</FN><N><FAMILY>Halaby</FAMILY><GIVEN>Avi</GIVEN></N><ORG><ORGNAME>Epiphyte Corporation Inc.</ORGNAME></ORG></vCard>');
insert into parityUserEmail (USERNAME,EMAIL)
    values('ahalaby', 'avi@epiphyte.com');

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('rwaterhouse','parity',0,0);
insert into jiveVCard (USERNAME,VALUE)
    values('rwaterhouse', '<vCard xmlns="vcard-temp"><FN>Randy Waterhouse</FN><N><FAMILY>Waterhouse</FAMILY><GIVEN>Randy</GIVEN></N><ORG><ORGNAME>Epiphyte Corporation Inc.</ORGNAME></ORG></vCard>');
insert into parityUserEmail (USERNAME,EMAIL)
    values('rwaterhouse', 'randy@epiphyte.com');

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('ashaftoe','parity',0,0);
insert into jiveVCard (USERNAME,VALUE)
    values('ashaftoe', '<vCard xmlns="vcard-temp"><FN>Amy Shaftoe</FN><N><FAMILY>Shaftoe</FAMILY><GIVEN>Amy</GIVEN></N><ORG><ORGNAME>Semper Marine Group</ORGNAME></ORG></vCard>');
insert into parityUserEmail (USERNAME,EMAIL)
    values('ashaftoe', 'amy@sempermarine.com');

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('brussell','parity',0,0);
insert into jiveVCard (USERNAME,VALUE)
    values('brussell', '<vCard xmlns="vcard-temp"><FN>Bertrand Russell</FN><N><FAMILY>Russell</FAMILY><GIVEN>Bertrand</GIVEN></N><ORG><ORGNAME>thinkParity Solutions Inc.</ORGNAME></ORG></vCard>');
insert into parityUserEmail (USERNAME,EMAIL)
    values('brussell', 'bertrand@thinkparity.com');

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('gdengo','parity',0,0);
insert into jiveVCard (USERNAME,VALUE)
    values('gdengo', '<vCard xmlns="vcard-temp"><FN>Goto Dengo</FN><N><FAMILY>Dengo</FAMILY><GIVEN>Goto</GIVEN></N><ORG><ORGNAME>Goto Engineering Inc.</ORGNAME></ORG></vCard>');
insert into parityUserEmail (USERNAME,EMAIL)
    values('gdengo', 'goto@gotoengineering.com');

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('junit','parity',0,0);
insert into jiveVCard (USERNAME,VALUE)
    values('junit', '<vCard xmlns="vcard-temp"><FN>JUnit thinkParity</FN><N><FAMILY>thinkParity</FAMILY><GIVEN>JUnit</GIVEN></N><ORG><ORGNAME>thinkParity Solutions Inc.</ORGNAME></ORG></vCard>');
insert into parityUserEmail (USERNAME,EMAIL)
    values('junit', 'junit@thinkparity.com');

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('junit.x','parity',0,0);
insert into jiveVCard (USERNAME,VALUE)
    values('junit.x', '<vCard xmlns="vcard-temp"><FN>JUnit.X thinkParity</FN><N><FAMILY>thinkParity</FAMILY><GIVEN>JUnit.X</GIVEN></N><ORG><ORGNAME>thinkParity Solutions Inc.</ORGNAME></ORG></vCard>');
insert into parityUserEmail (USERNAME,EMAIL)
    values('junit.x', 'junit.x@thinkparity.com');

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('junit.y','parity',0,0);
insert into jiveVCard (USERNAME,VALUE)
    values('junit.y', '<vCard xmlns="vcard-temp"><FN>JUnit.Y thinkParity</FN><N><FAMILY>thinkParity</FAMILY><GIVEN>JUnit.Y</GIVEN></N><ORG><ORGNAME>thinkParity Solutions Inc.</ORGNAME></ORG></vCard>');
insert into parityUserEmail (USERNAME,EMAIL)
    values('junit.y', 'junit.y@thinkparity.com');

insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('rkroeker', 'ahalaby@rkutil.raykroeker.com', 'rkroeker', NOW(), 'rkroeker', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('rkroeker', 'rwaterhouse@rkutil.raykroeker.com', 'rkroeker', NOW(), 'rkroeker', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('rkroeker', 'ashaftoe@rkutil.raykroeker.com', 'rkroeker', NOW(), 'rkroeker', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('rkroeker', 'brussel@rkutil.raykroeker.com', 'rkroeker', NOW(), 'rkroeker', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('rkroeker', 'gdengo@rkutil.raykroeker.com', 'rkroeker', NOW(), 'rkroeker', NOW());

insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('rwaterhouse', 'rkroeker@rkutil.raykroeker.com', 'rwaterhouse', NOW(), 'rwaterhouse', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('rwaterhouse', 'ahalaby@rkutil.raykroeker.com', 'rwaterhouse', NOW(), 'rwaterhouse', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('rwaterhouse', 'ashaftoe@rkutil.raykroeker.com', 'rwaterhouse', NOW(), 'rwaterhouse', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('rwaterhouse', 'brussel@rkutil.raykroeker.com', 'rwaterhouse', NOW(), 'rwaterhouse', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('rwaterhouse', 'gdengo@rkutil.raykroeker.com', 'rwaterhouse', NOW(), 'rwaterhouse', NOW());

insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('ahalaby', 'rkroeker@rkutil.raykroeker.com', 'ahalaby', NOW(), 'ahalaby', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('ahalaby', 'rwaterhouse@rkutil.raykroeker.com', 'ahalaby', NOW(), 'ahalaby', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('ahalaby', 'ashaftoe@rkutil.raykroeker.com', 'ahalaby', NOW(), 'ahalaby', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('ahalaby', 'brussel@rkutil.raykroeker.com', 'ahalaby', NOW(), 'ahalaby', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('ahalaby', 'gdengo@rkutil.raykroeker.com', 'ahalaby', NOW(), 'ahalaby', NOW());

insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('ashaftoe', 'rkroeker@rkutil.raykroeker.com', 'ashaftoe', NOW(), 'ashaftoe', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('ashaftoe', 'ahalaby@rkutil.raykroeker.com', 'ashaftoe', NOW(), 'ashaftoe', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('ashaftoe', 'rwaterhouse@rkutil.raykroeker.com', 'ashaftoe', NOW(), 'ashaftoe', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('ashaftoe', 'brussel@rkutil.raykroeker.com', 'ashaftoe', NOW(), 'ashaftoe', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('ashaftoe', 'gdengo@rkutil.raykroeker.com', 'ashaftoe', NOW(), 'ashaftoe', NOW());

insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('brussell', 'rkroeker@rkutil.raykroeker.com', 'brussell', NOW(), 'brussell', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('brussell', 'ahalaby@rkutil.raykroeker.com', 'brussell', NOW(), 'brussell', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('brussell', 'rwaterhouse@rkutil.raykroeker.com', 'brussell', NOW(), 'brussell', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('brussell', 'ashaftoe@rkutil.raykroeker.com', 'brussell', NOW(), 'brussell', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('brussell', 'gdengo@rkutil.raykroeker.com', 'brussell', NOW(), 'brussell', NOW());

insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('gdengo', 'rkroeker@rkutil.raykroeker.com', 'gdengo', NOW(), 'gdengo', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('gdengo', 'ahalaby@rkutil.raykroeker.com', 'gdengo', NOW(), 'gdengo', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('gdengo', 'rwaterhouse@rkutil.raykroeker.com', 'gdengo', NOW(), 'gdengo', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('gdengo', 'ashaftoe@rkutil.raykroeker.com', 'gdengo', NOW(), 'gdengo', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('gdengo', 'brussel@rkutil.raykroeker.com', 'gdengo', NOW(), 'gdengo', NOW());

insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('junit', 'junit.x@rkutil.raykroeker.com', 'junit', NOW(), 'junit', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('junit', 'junit.y@rkutil.raykroeker.com', 'junit', NOW(), 'junit', NOW());

insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('junit.x', 'junit@rkutil.raykroeker.com', 'junit.x', NOW(), 'junit.x', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('junit.x', 'junit.y@rkutil.raykroeker.com', 'junit.x', NOW(), 'junit.x', NOW());

insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('junit.y', 'junit@rkutil.raykroeker.com', 'junit.y', NOW(), 'junit.y', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('junit.y', 'junit.x@rkutil.raykroeker.com', 'junit.y', NOW(), 'junit.y', NOW());
