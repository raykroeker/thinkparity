select u.username,pre.*
from tpsd_product_release pr
    inner join tpsd_product_release_error pre on pre.release_id=pr.release_id
    inner join tpsd_user u on u.user_id=pre.user_id
where pr.release_name = 'v1_0-20070919-2045'
order by pre.error_id desc;