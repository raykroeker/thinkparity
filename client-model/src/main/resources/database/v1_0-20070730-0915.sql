
-- v1_0-20070730-0915

-- Ticket #
alter table ARTIFACT_VERSION add FLAGS bigint not null default 0;

-- update release
update META_DATA set META_DATA_VALUE = 'v1_0-20070730-0915' where META_DATA_KEY = 'thinkparity.release-name';
update MIGRATOR set RELEASE_NAME = 'v1_0-20070730-0915';
