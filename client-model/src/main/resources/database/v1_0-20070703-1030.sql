
-- v1_0-20070703-1030

-- update release
update META_DATA set META_DATA_VALUE = 'v1_0-20070703-1030' where META_DATA_KEY = 'thinkparity.release-name' and META_DATA_VALUE = 'v1_0-20070626-1300';
update MIGRATOR set RELEASE_NAME = 'v1_0-20070703-1030';
