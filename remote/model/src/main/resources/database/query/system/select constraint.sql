select SS.SCHEMANAME || '.' || ST.TABLENAME,SC.CONSTRAINTNAME,SC.TYPE,SC.REFERENCECOUNT
from SYS.SYSCONSTRAINTS SC
    inner join SYS.SYSTABLES ST on ST.TABLEID=SC.TABLEID
    inner join SYS.SYSSCHEMAS SS on SS.SCHEMAID=SC.SCHEMAID
where SC.CONSTRAINTNAME = 'SQL070808044837833';