
-- v1_0-20070830-1600

-- update release
update META_DATA set META_DATA_VALUE = 'v1_0-20070830-1600' where META_DATA_KEY = 'thinkparity.release-name';
update MIGRATOR set RELEASE_NAME = 'v1_0-20070830-1600';
