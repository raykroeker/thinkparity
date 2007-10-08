select O.USERNAME,PPI.*
from TPSD_PAYMENT_PLAN PP 
    inner join TPSD_USER O on O.USER_ID=PP.PLAN_OWNER
    inner join TPSD_PAYMENT_PLAN_INVOICE PPI on PPI.PLAN_ID=PP.PLAN_ID
order by PP.PLAN_ID desc,PPI.INVOICE_ID desc;