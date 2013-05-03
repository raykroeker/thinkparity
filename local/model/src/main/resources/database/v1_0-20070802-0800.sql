
-- v1_0-20070802-0800

-- update release
update META_DATA set META_DATA_VALUE = 'v1_0-20070802-0800' where META_DATA_KEY = 'thinkparity.release-name';
update MIGRATOR set RELEASE_NAME = 'v1_0-20070802-0800';
