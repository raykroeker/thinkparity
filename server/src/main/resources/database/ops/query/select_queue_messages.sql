select MC.CHANNEL_NAME,M.MSG_ID,M.MSG_TIMESTAMP,M.MSG,MD.DATA_ORDINAL,MD.DATA_NAME,MD.DATA_VALUE,MD.DATA_TYPE
from TPSD_MSG M
    inner join TPSD_MSG_CHANNEL MC on MC.CHANNEL_ID=M.MSG_CHANNEL
    inner join TPSD_MSG_DATA MD on MD.MSG_ID=M.MSG_ID
where MC.CHANNEL_NAME = '/com/thinkparity/queue'
order by M.MSG_ID asc,MD.DATA_ORDINAL asc;