select UEQ.EVENT_ID "id",PU.USERNAME "user",UEQ.EVENT_DATE "date",
    UEQ.EVENT_PRIORITY "priority"
from USER_EVENT_QUEUE UEQ
    inner join PARITY_USER PU on PU.USER_ID=UEQ.USER_ID
order by UEQ.EVENT_DATE desc
