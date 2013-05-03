-- delete desdemona artifact
delete from TPSD_ARTIFACT_TEAM_REL;
delete from TPSD_BACKUP_USER_ARCHIVE;
delete from TPSD_ARTIFACT;
-- delete ophelia container
delete from CONTAINER_DRAFT_ARTIFACT_REL;
delete from CONTAINER_DRAFT_DOCUMENT;
delete from CONTAINER_DRAFT;
delete from CONTAINER_VERSION_ARTIFACT_VERSION_REL;
delete from CONTAINER_VERSION_DELTA;
delete from CONTAINER_VERSION_PUBLISHED_TO;
delete from CONTAINER_VERSION;
delete from CONTAINER;
-- delete ophelia document
delete from DOCUMENT_VERSION;
delete from DOCUMENT;
-- delete ophelia artifact
delete from ARTIFACT_VERSION_META_DATA;
delete from ARTIFACT_VERSION;
delete from ARTIFACT_TEAM_REL;
delete from ARTIFACT;
commit;
