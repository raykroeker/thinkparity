
-- v1_0-20070626-1300

-- update release
update META_DATA set META_DATA_VALUE = 'v1_0-20070626-1300' where META_DATA_KEY = 'thinkparity.release-name' and META_DATA_VALUE = 'v1_0-20070612-2246';
update MIGRATOR set RELEASE_NAME = 'v1_0-20070626-1300';
