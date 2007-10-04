
-- v1_0-20071004-0930
alter table PROFILE add PROFILE_ACTIVE character not null default '1';

-- update release
update META_DATA set META_DATA_VALUE = 'v1_0-20071004-0930' where META_DATA_KEY = 'thinkparity.release-name';
update MIGRATOR set RELEASE_NAME = 'v1_0-20071004-0930';
