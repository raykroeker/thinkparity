insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('omid','parity',NOW(),NOW());
insert into jiveVCard (USERNAME,VALUE)
    values('omid', '<vCard xmlns="vcard-temp"><FN>Omid Ejtemai</FN><N><FAMILY>Ejtemai</FAMILY><GIVEN>Omid</GIVEN></N><ORG><ORGNAME>thinkParity Solutions Inc.</ORGNAME></ORG><EMAIL><HOME/><INTERNET/><PREF/><USERID>omid@thinkparity.com</USERID></EMAIL></vCard>');

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('raymond','parity',NOW(),NOW());
insert into jiveVCard (USERNAME,VALUE)
    values('raymond', '<vCard xmlns="vcard-temp"><FN>Raymond Kroeker</FN><N><FAMILY>Kroeker</FAMILY><GIVEN>Raymond</GIVEN></N><ORG><ORGNAME>thinkParity Solutions Inc.</ORGNAME></ORG><EMAIL><HOME/><INTERNET/><PREF/><USERID>raymond@thinkparity.com</USERID></EMAIL></vCard>');

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('robert','parity',NOW(),NOW());
insert into jiveVCard (USERNAME,VALUE)
    values('robert', '<vCard xmlns="vcard-temp"><FN>Robert MacMartin</FN><N><FAMILY>MacMartin</FAMILY><GIVEN>Robert</GIVEN></N><ORG><ORGNAME>thinkParity Solutions Inc.</ORGNAME></ORG><EMAIL><HOME/><INTERNET/><PREF/><USERID>robert@thinkparity.com</USERID></EMAIL></vCard>');

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('djohnson','parity',NOW(),NOW());
insert into jiveVCard (USERNAME,VALUE)
    values('djohnson', '<vCard xmlns="vcard-temp"><FN>Don Johnson</FN><N><FAMILY>Johnson</FAMILY><GIVEN>Don</GIVEN></N><ORG><ORGNAME>thinkParity Solutions Inc.</ORGNAME></ORG><EMAIL><HOME/><INTERNET/><PREF/><USERID>djohnson@thinkparity.com</USERID></EMAIL></vCard>');

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('emorrison','parity',NOW(),NOW());
insert into jiveVCard (USERNAME,VALUE)
    values('emorrison', '<vCard xmlns="vcard-temp"><FN>Emily Morrison</FN><N><FAMILY>Morrison</FAMILY><GIVEN>Emily</GIVEN></N><ORG><ORGNAME>thinkParity Solutions Inc.</ORGNAME></ORG><EMAIL><HOME/><INTERNET/><PREF/><USERID>emorrison@thinkparity.com</USERID></EMAIL></vCard>');

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('kmfarland','parity',NOW(),NOW());
insert into jiveVCard (USERNAME,VALUE)
    values('kmfarland', '<vCard xmlns="vcard-temp"><FN>Kevin MacFarland</FN><N><FAMILY>MacFarland</FAMILY><GIVEN>Kevin</GIVEN></N><ORG><ORGNAME>thinkParity Solutions Inc.</ORGNAME></ORG><EMAIL><HOME/><INTERNET/><PREF/><USERID>kmfarland@thinkparity.com</USERID></EMAIL></vCard>');

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('strimnell','parity',NOW(),NOW());
insert into jiveVCard (USERNAME,VALUE)
    values('strimnell', '<vCard xmlns="vcard-temp"><FN>Sarah Trimnell</FN><N><FAMILY>Trimnell</FAMILY><GIVEN>Sarah</GIVEN></N><ORG><ORGNAME>thinkParity Solutions Inc.</ORGNAME></ORG><EMAIL><HOME/><INTERNET/><PREF/><USERID>strimnell@thinkparity.com</USERID></EMAIL></vCard>');

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('kenjiro','parity',NOW(),NOW());
insert into jiveVCard (USERNAME,VALUE)
    values('kenjiro', '<vCard xmlns="vcard-temp"><FN>Kenjiro MacMartin</FN><N><FAMILY>MacMartin</FAMILY><GIVEN>Kenjiro</GIVEN></N><ORG><ORGNAME>thinkParity Solutions Inc.</ORGNAME></ORG><EMAIL><HOME/><INTERNET/><PREF/><USERID>kmacmartin@thinkparity.com</USERID></EMAIL></vCard>');

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('masako','parity',NOW(),NOW());
insert into jiveVCard (USERNAME,VALUE)
    values('masako', '<vCard xmlns="vcard-temp"><FN>Masako</FN><N><FAMILY></FAMILY><GIVEN>Masako</GIVEN></N><ORG><ORGNAME>thinkParity Solutions Inc.</ORGNAME></ORG><EMAIL><HOME/><INTERNET/><PREF/><USERID>masako@thinkparity.com</USERID></EMAIL></vCard>');


insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('omid', 'raymond@thinkparity.dyndns.org', 'omid', NOW(), 'omid', NOW());
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
    values ('raymond', 'omid@thinkparity.dyndns.org', 'raymond', NOW(), 'raymond', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('raymond', 'robert@thinkparity.dyndns.org', 'raymond', NOW(), 'raymond', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('raymond', 'djohnson@thinkparity.dyndns.org', 'raymond', NOW(), 'raymond', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('raymond', 'emorrison@thinkparity.dyndns.org', 'raymond', NOW(), 'raymond', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('raymond', 'kmfarland@thinkparity.dyndns.org', 'raymond', NOW(), 'raymond', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('raymond', 'strimnell@thinkparity.dyndns.org', 'raymond', NOW(), 'raymond', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('raymond', 'kenjiro@thinkparity.dyndns.org', 'raymond', NOW(), 'raymond', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('raymond', 'masako@thinkparity.dyndns.org', 'raymond', NOW(), 'raymond', NOW());


insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('robert', 'omid@thinkparity.dyndns.org', 'robert', NOW(), 'robert', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('robert', 'raymond@thinkparity.dyndns.org', 'robert', NOW(), 'robert', NOW());
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
    values ('djohnson', 'raymond@thinkparity.dyndns.org', 'djohnson', NOW(), 'djohnson', NOW());
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
    values ('emorrison', 'raymond@thinkparity.dyndns.org', 'emorrison', NOW(), 'emorrison', NOW());
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
    values ('kmfarland', 'raymond@thinkparity.dyndns.org', 'kmfarland', NOW(), 'kmfarland', NOW());
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
    values ('strimnell', 'omid@thinkparity.dyndns.org', 'strinmell', NOW(), 'strinmell', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('strimnell', 'raymond@thinkparity.dyndns.org', 'strinmell', NOW(), 'strinmell', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('strimnell', 'robert@thinkparity.dyndns.org', 'strinmell', NOW(), 'strinmell', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('strimnell', 'djohnson@thinkparity.dyndns.org', 'strinmell', NOW(), 'strinmell', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('strimnell', 'emorrison@thinkparity.dyndns.org', 'strinmell', NOW(), 'strinmell', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('strimnell', 'kmfarland@thinkparity.dyndns.org', 'strinmell', NOW(), 'strinmell', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('strimnell', 'kenjiro@thinkparity.dyndns.org', 'strinmell', NOW(), 'strinmell', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('strimnell', 'masako@thinkparity.dyndns.org', 'strinmell', NOW(), 'strinmell', NOW());

insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('kenjiro', 'omid@thinkparity.dyndns.org', 'kenjiro', NOW(), 'kenjiro', NOW());
insert into parityContact (USERNAME,CONTACTUSERNAME,CREATEDBY,CREATEDON,UPDATEDBY,UPDATEDON)
    values ('kenjiro', 'raymond@thinkparity.dyndns.org', 'kenjiro', NOW(), 'kenjiro', NOW());
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
    values ('masako', 'raymond@thinkparity.dyndns.org', 'masako', NOW(), 'masako', NOW());
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
