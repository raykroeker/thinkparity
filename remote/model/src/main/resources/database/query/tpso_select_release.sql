select *
from META_DATA MD
left outer join MIGRATOR M on M.RELEASE_NAME = MD.META_DATA_VALUE
where MD.META_DATA_KEY = 'thinkparity.release-name';