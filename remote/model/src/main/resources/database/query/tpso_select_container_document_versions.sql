select c.artifact_name,d.artifact_name,cvavr.artifact_version_id
from container_version_artifact_version_rel cvavr
inner join artifact c on c.artifact_id = cvavr.container_id
inner join artifact d on d.artifact_id = cvavr.artifact_id
where c.artifact_unique_id = 'fe362e5a-c749-4cb7-a15a-0b9057f16778';