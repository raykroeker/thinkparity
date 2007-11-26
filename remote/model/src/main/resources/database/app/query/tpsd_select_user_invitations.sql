select UI.USERNAME "invited by",U.USERNAME "invited",UINV.INVITED_ON "invited on",UINV.ACCEPTED_ON "accepted on"
from TPSD_USER_INVITATION UINV
inner join TPSD_USER U on U.USER_ID=UINV.USER_ID
inner join TPSD_USER UI on UI.USER_ID=UINV.INVITED_BY
order by UINV.INVITED_ON asc;