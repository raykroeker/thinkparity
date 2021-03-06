select U.USERNAME,PPITX.*
from TPSD_PAYMENT_PLAN PP 
    inner join TPSD_USER_PAYMENT_PLAN UPP on UPP.PLAN_ID=PP.PLAN_ID
    inner join TPSD_USER U  on U.USER_ID=UPP.USER_ID
    inner join TPSD_PAYMENT_PLAN_INVOICE PPI on PPI.PLAN_ID=PP.PLAN_ID
    inner join TPSD_PAYMENT_PLAN_INVOICE_TX PPITX on PPITX.INVOICE_ID=PPI.INVOICE_ID
order by PP.PLAN_ID desc,PPI.INVOICE_ID desc;