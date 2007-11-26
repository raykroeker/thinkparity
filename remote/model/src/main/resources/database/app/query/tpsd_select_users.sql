select U.USER_ID,U.USERNAME,E.EMAIL,U.PASSWORD,U.TOKEN,U.DISABLED,U.ACTIVE
from TPSD_USER U
inner join TPSD_USER_EMAIL UE on UE.USER_ID=U.USER_ID
inner join TPSD_EMAIL E on E.EMAIL_ID=UE.EMAIL_ID
order by U.USERNAME asc;
