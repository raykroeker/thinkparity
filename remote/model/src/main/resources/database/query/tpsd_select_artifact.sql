select A.ARTIFACT_UNIQUE_ID "id",CBU.USERNAME "created by",A.CREATED_ON "created on",ADOU.USERNAME "draft owner"
from TPSD_ARTIFACT A
inner join TPSD_USER CBU on CBU.USER_ID=A.CREATED_BY
inner join TPSD_USER ADOU on ADOU.USER_ID=A.ARTIFACT_DRAFT_OWNER
where ARTIFACT_UNIQUE_ID = 'f7374cec-9f58-441b-ae64-72e7f2dc8cb1'
order by A.CREATED_ON desc
