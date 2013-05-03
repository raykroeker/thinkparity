select A.ARTIFACT_ID "artifact id",A.ARTIFACT_UNIQUE_ID "unique id",CB.USERNAME "created by",A.CREATED_ON "created on",DO.USERNAME "draft owner",A.UPDATED_ON "draft created"
from TPSD_ARTIFACT A
    inner join TPSD_USER DO on DO.USER_ID=A.ARTIFACT_DRAFT_OWNER
    inner join TPSD_USER CB on CB.USER_ID=A.CREATED_BY
where DO.USER_ID > 7000
order by A.UPDATED_ON desc;