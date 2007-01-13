

insert into jiveUser (USERNAME,PASSWORD,CREATIONDATE,MODIFICATIONDATE)
    values('rkroeker','parity',0,0);
insert into parityUserEmail (USERNAME,EMAIL,VERIFIED)
    values('rkroeker', 'raymond@thinkparity.com', true);
insert into PARITY_USER_PROFILE (USERNAME,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('rkroeker', 'What is my username?', 'rkroeker',false,'<com.thinkparity.codebase.model.user.UserVCard><name>Raymond Kroeker</name></com.thinkparity.codebase.model.user.UserVCard>');
insert into PARITY_USER_FEATURE_REL (USERNAME, FEATURE_ID)
	values ('rkroeker', 1000);

<com.thinkparity.codebase.model.profile.profilevcard>
  <city>Abbotsford</city>
  <country>CA</country>
  <mobilephone>604-308-0700</mobilephone>
  <name>Raymond Kroeker</name>
  <organization>thinkParity Solutions Inc</organization>

  <phone>604-308-0700</phone>
  
</com.thinkparity.codebase.model.profile.profilevcard>