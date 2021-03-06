select A.ARTIFACT_UNIQUE_ID "id",AV.ARTIFACT_VERSION_ID "version",
    A.ARTIFACT_NAME "name",CBU.NAME "created by",AV.CREATED_ON "created on"
from CONTAINER_VERSION CV
inner join ARTIFACT_VERSION AV on AV.ARTIFACT_ID=CV.CONTAINER_ID
    and AV.ARTIFACT_VERSION_ID=CV.CONTAINER_VERSION_ID
inner join ARTIFACT A on A.ARTIFACT_ID=AV.ARTIFACT_ID
inner join PARITY_USER CBU on CBU.USER_ID=AV.CREATED_BY
order by AV.CREATED_ON desc
