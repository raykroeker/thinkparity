-- delete any tx errors/tx
delete from tpsd_payment_plan_invoice_tx_error
    where tx_id in (select pptx.tx_id
        from tpsd_payment_plan_invoice_tx pptx
            inner join tpsd_payment_plan_invoice ppi on ppi.invoice_id=pptx.invoice_id
            inner join tpsd_user_payment_plan upp on upp.plan_id=ppi.plan_id
            inner join tpsd_user u on u.user_id=upp.user_id
        where u.username='20071002-1843');
delete from tpsd_payment_plan_invoice_tx
    where invoice_id in (select ppi.invoice_id
        from tpsd_payment_plan_invoice ppi
            inner join tpsd_user_payment_plan upp on upp.plan_id=ppi.plan_id
            inner join tpsd_user u on u.user_id=upp.user_id
        where u.username='20071002-1843');
-- delete the invoice "paid on" date
update tpsd_payment_plan_invoice set payment_date=null
    where invoice_id in (select ppi.invoice_id
        from tpsd_payment_plan_invoice ppi
            inner join tpsd_user_payment_plan upp on upp.plan_id=ppi.plan_id
            inner join tpsd_user u on u.user_id=upp.user_id
        where u.username='20071002-1843');
-- set the invoice item to end in "1" (insufficient funds)
update tpsd_payment_plan_invoice_item set item_amount=8401
    where invoice_id in (select ppi.invoice_id
        from tpsd_payment_plan_invoice ppi
            inner join tpsd_user_payment_plan upp on upp.plan_id=ppi.plan_id
            inner join tpsd_user u on u.user_id=upp.user_id
        where u.username='20071002-1843');
-- add missing locks
insert into tpsd_payment_plan_invoice_lock (invoice_id) (select ppi.invoice_id
        from tpsd_payment_plan_invoice ppi
            inner join tpsd_user_payment_plan upp on upp.plan_id=ppi.plan_id
            inner join tpsd_user u on u.user_id=upp.user_id
        where invoice_id not in (select invoice_id from tpsd_payment_plan_invoice_lock)
            and u.username='20071002-1843');
