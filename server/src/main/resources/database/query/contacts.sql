select PU.USER_ID "user id",PU.USERNAME "user",PUC.USER_ID "contact id",
    PUC.USERNAME "contact"
from CONTACT C
    inner join PARITY_USER PU on PU.USER_ID=C.USER_ID
    inner join PARITY_USER PUC on PUC.USER_ID=C.CONTACT_ID
order by PU.USER_ID asc,PUC.USER_ID asc
