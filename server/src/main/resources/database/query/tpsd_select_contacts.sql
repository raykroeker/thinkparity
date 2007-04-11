select U.USER_ID "user id",U.USERNAME "user",UC.USER_ID "contact id",
    UC.USERNAME "contact"
from TPSD_CONTACT C
    inner join TPSD_USER U on U.USER_ID=C.USER_ID
    inner join TPSD_USER UC on UC.USER_ID=C.CONTACT_ID
order by U.USER_ID asc,UC.USER_ID asc
