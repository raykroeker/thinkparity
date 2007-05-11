insert into jiveProperty(NAME,PROPVALUE) values('xmpp.auth.anonymous','true');
insert into jiveProperty(NAME,PROPVALUE) values('xmpp.domain','thinkparity.net');
insert into jiveProperty(NAME,PROPVALUE) values('xmpp.server.socket.port','5272');
insert into jiveProperty(NAME,PROPVALUE) values('xmpp.socket.plain.port','5228');
insert into jiveProperty(NAME,PROPVALUE) values('xmpp.socket.ssl.active','true');
insert into jiveProperty(NAME,PROPVALUE) values('xmpp.socket.ssl.keypass','password');
insert into jiveProperty(NAME,PROPVALUE) values('xmpp.socket.ssl.port','5229');
insert into jiveProperty(NAME,PROPVALUE) values('thinkparity.environment','DEVELOPMENT_ROBERT');
insert into jiveProperty(NAME,PROPVALUE) values('thinkparity.mode','DEVELOPMENT');
insert into jiveProperty(NAME,PROPVALUE) values('thinkparity.product-name','DesdemonaProduct');
insert into jiveProperty(NAME,PROPVALUE) values('thinkparity.release-name','20070325');
insert into jiveProperty(NAME,PROPVALUE) values('thinkparity.migrator.logerror.notify','false');

insert into TPSD_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('thinkparity','parity','What is my username?','thinkparity','0','<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>thinkParity Services Inc.</name><organization>Company Name</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into TPSD_EMAIL(EMAIL)
    values('thinkparity@thinkparity.com');
insert into TPSD_USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    values(7000,5000,'1');

insert into TPSD_PRODUCT(PRODUCT_NAME) values('OpheliaProduct');
insert into TPSD_PRODUCT(PRODUCT_NAME) values('DesdemonaProduct');
insert into TPSD_PRODUCT_RELEASE(PRODUCT_ID,RELEASE_NAME,RELEASE_OS,RELEASE_DATE)
    values(1000,'DEVELOPMENT','LINUX',CURRENT_TIMESTAMP);
insert into TPSD_PRODUCT_RELEASE(PRODUCT_ID,RELEASE_NAME,RELEASE_OS,RELEASE_DATE)
    values(1000,'DEVELOPMENT','WINDOWS_XP',CURRENT_TIMESTAMP);
insert into TPSD_PRODUCT_FEATURE(PRODUCT_ID,FEATURE_NAME) values(1000,'CORE');
insert into TPSD_PRODUCT_FEATURE(PRODUCT_ID,FEATURE_NAME) values(1000,'BACKUP');

insert into TPSD_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('thinkparity-backup','parity','What is my username?','thinkparity-backup','0','<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>thinkParity Software</name><organization>thinkParity Solutions Inc.</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into TPSD_EMAIL(EMAIL)
    values('thinkparity+backup@thinkparity.com');
insert into TPSD_USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    values(7001,5001,'1');

insert into TPSD_USER(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD)
    values('thinkparity-support','parity','What is my username?','thinkparity-support','0','<com.thinkparity.codebase.model.user.UserVCard><country>CAN</country><language>eng</language><name>thinkParity Software</name><organization>thinkParity Solutions Inc.</organization><organizationCountry>CAN</organizationCountry><timeZone>America/Vancouver</timeZone><title>Title</title></com.thinkparity.codebase.model.user.UserVCard>');
insert into TPSD_EMAIL(EMAIL)
    values('thinkparity+support@thinkparity.com');
insert into TPSD_USER_EMAIL(USER_ID,EMAIL_ID,VERIFIED)
    values(7002,5002,'1');
insert into TPSD_USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7002,6000);
insert into TPSD_USER_FEATURE_REL(USER_ID,FEATURE_ID)
    values(7002,6001);
