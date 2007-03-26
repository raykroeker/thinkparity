select UEQ.EVENT_ID "id",U.USERNAME "user",UEQ.EVENT_DATE "date",
    UEQ.EVENT_PRIORITY "priority"
from TPSD_USER_EVENT_QUEUE UEQ
    inner join TPSD_USER U on U.USER_ID=UEQ.USER_ID
order by UEQ.EVENT_DATE desc
