select *
from TPSD_SESSION S
inner join TPSD_USER U on U.USER_ID=S.USER_ID;
