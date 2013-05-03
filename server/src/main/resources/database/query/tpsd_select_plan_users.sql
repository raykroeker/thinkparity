select U.USERNAME,PP.*
from TPSD_PAYMENT_PLAN PP 
    inner join TPSD_USER_PAYMENT_PLAN UPP on UPP.PLAN_ID=PP.PLAN_ID
    inner join TPSD_USER U on U.USER_ID=UPP.USER_ID
order by PP.PLAN_ID desc;