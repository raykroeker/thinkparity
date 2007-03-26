select A.ARTIFACT_UNIQUE_ID "id",CBU.USERNAME "created by",A.CREATED_ON "created on"
from TPSD_ARTIFACT A
inner join TPSD_USER CBU on CBU.USER_ID=A.CREATED_BY
order by A.CREATED_ON desc
