select CI.CONTACT_INVITATION_ID "id",UC.USER_ID "created by id",UC.USERNAME "created by",
    CI.CREATED_ON "created on",U.USER_ID "user id",U.USERNAME "user",E.EMAIL "invitation e-mail",CIA.ATTACHMENT_REFERENCE_TYPE_ID,CIA.ATTACHMENT_REFERENCE_ID
from TPSD_CONTACT_INVITATION_OUTGOING_EMAIL CIOE
    inner join TPSD_CONTACT_INVITATION CI on CI.CONTACT_INVITATION_ID=CIOE.CONTACT_INVITATION_ID
    left outer join TPSD_CONTACT_INVITATION_ATTACHMENT CIA on CIA.CONTACT_INVITATION_ID=CI.CONTACT_INVITATION_ID
    inner join TPSD_USER UC on UC.USER_ID=CI.CREATED_BY
    inner join TPSD_USER U on U.USER_ID=CIOE.USER_ID
    inner join TPSD_EMAIL E on E.EMAIL_ID=CIOE.INVITATION_EMAIL_ID
order by CI.CREATED_ON desc