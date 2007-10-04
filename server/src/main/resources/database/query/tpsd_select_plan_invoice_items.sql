select U.USERNAME,PPII.*
from TPSD_PAYMENT_PLAN PP 
    inner join TPSD_USER_PAYMENT_PLAN UPP on UPP.PLAN_ID=PP.PLAN_ID
    inner join TPSD_USER U  on U.USER_ID=UPP.USER_ID
    inner join TPSD_PAYMENT_PLAN_INVOICE PPI on PPI.PLAN_ID=PP.PLAN_ID
    inner join TPSD_PAYMENT_PLAN_INVOICE_ITEM PPII on PPII.INVOICE_ID=PPI.INVOICE_ID
where U.USERNAME = '20071002-1843';