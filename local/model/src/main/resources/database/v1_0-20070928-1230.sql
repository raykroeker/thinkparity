
-- v1_0-20070928-1230

-- update release
update META_DATA set META_DATA_VALUE = 'v1_0-20070928-1230' where META_DATA_KEY = 'thinkparity.release-name';
update MIGRATOR set RELEASE_NAME = 'v1_0-20070928-1230';
