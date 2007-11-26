select u.username,pre.*
from tpsd_product_release pr
    inner join tpsd_product_release_error pre on pre.release_id=pr.release_id
    inner join tpsd_user u on u.user_id=pre.user_id
where pre.error_id > 9001
order by pre.error_id desc;