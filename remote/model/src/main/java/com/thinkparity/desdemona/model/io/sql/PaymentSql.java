/*
 * Created On:  8-Aug-07 4:56:00 PM
 */
package com.thinkparity.desdemona.model.io.sql;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import com.thinkparity.codebase.model.profile.payment.PaymentInfo;
import com.thinkparity.codebase.model.profile.payment.PaymentPlanCredentials;
import com.thinkparity.codebase.model.profile.payment.PaymentInfo.CardName;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.desdemona.model.io.hsqldb.HypersonicException;
import com.thinkparity.desdemona.model.io.hsqldb.HypersonicSession;
import com.thinkparity.desdemona.model.node.Node;
import com.thinkparity.desdemona.model.profile.payment.Currency;
import com.thinkparity.desdemona.model.profile.payment.Invoice;
import com.thinkparity.desdemona.model.profile.payment.InvoiceItem;
import com.thinkparity.desdemona.model.profile.payment.InvoiceTransaction;
import com.thinkparity.desdemona.model.profile.payment.PaymentPlan;
import com.thinkparity.desdemona.model.profile.payment.PaymentPlan.InvoicePeriod;
import com.thinkparity.desdemona.model.profile.payment.provider.PaymentProvider;

/**
 * <b>Title:</b>thinkParity Desdemona Model Payment SQL<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class PaymentSql extends AbstractSql {

    /** Sql to create an info. */
    private static final String SQL_CREATE_INFO =
        new StringBuilder("insert into TPSD_PAYMENT_INFO ")
        .append("(PROVIDER_ID,INFO_CARD_NAME,INFO_CARD_NUMBER,")
        .append("INFO_CARD_EXPIRY_MONTH,INFO_CARD_EXPIRY_YEAR) ")
        .append("values (?,?,?,?,?)")
        .toString();

    /** Sql to create an invoice. */
    private static final String SQL_CREATE_INVOICE =
        new StringBuilder("insert into TPSD_PAYMENT_PLAN_INVOICE ")
        .append("(PLAN_ID,INVOICE_NUMBER,INVOICE_DATE) ")
        .append("values (?,?,?)")
        .toString();

    /** Sql to create an invoice item. */
    private static final String SQL_CREATE_INVOICE_ITEM =
        new StringBuilder("insert into TPSD_PAYMENT_PLAN_INVOICE_ITEM ")
        .append("(INVOICE_ID,ITEM_NUMBER,ITEM_DESCRIPTION,ITEM_AMOUNT) ")
        .append("values (?,?,?,?)")
        .toString();

    /** Sql to create an invoice lock. */
    private static final String SQL_CREATE_INVOICE_LOCK =
        new StringBuilder("insert into TPSD_PAYMENT_PLAN_INVOICE_LOCK ")
        .append("(INVOICE_ID) ")
        .append("values (?)")
        .toString();

    /** Sql to create a plan. */
    private static final String SQL_CREATE_PLAN =
        new StringBuilder("insert into TPSD_PAYMENT_PLAN ")
        .append("(PLAN_UNIQUE_ID,PLAN_NAME,PLAN_CURRENCY,PLAN_PASSWORD,")
        .append("PLAN_BILLABLE,PLAN_OWNER,INVOICE_PERIOD,")
        .append("INVOICE_PERIOD_OFFSET,PLAN_ARREARS) ")
        .append("values (?,?,?,?,?,?,?,?,?)")
        .toString();

    /** Sql to create the plan info. */
    private static final String SQL_CREATE_PLAN_INFO =
        new StringBuilder("insert into TPSD_PAYMENT_PLAN_PAYMENT_INFO ")
        .append("(PLAN_ID,INFO_ID) ")
        .append("values(?,?)")
        .toString();

    /** Sql to create a plan lock. */
    private static final String SQL_CREATE_PLAN_LOCK =
        new StringBuilder("insert into TPSD_PAYMENT_PLAN_LOCK ")
        .append("(PLAN_ID) ")
        .append("values (?)")
        .toString();

    /** Sql create create a plan queue entry. */
    private static final String SQL_CREATE_PLAN_QUEUE_ENTRY =
        new StringBuilder("insert into TPSD_PAYMENT_PLAN_QUEUE ")
        .append("(PLAN_ID) ")
        .append("values (?)")
        .toString();

    /** Sql to create a transaction. */
    private static final String SQL_CREATE_TRANSACTION =
        new StringBuilder("insert into TPSD_PAYMENT_PLAN_INVOICE_TX ")
        .append("(INVOICE_ID,TX_UNIQUE_ID,TX_DATE) ")
        .append("values (?,?,?)")
        .toString();

    /** Sql to delete an invoice lock. */
    private static final String SQL_DELETE_INVOICE_LOCK =
        new StringBuilder("delete from TPSD_PAYMENT_PLAN_INVOICE_LOCK ")
        .append("where INVOICE_ID=? and NODE_ID=?")
        .toString();

    /** Sql create delete a plan queue entry. */
    private static final String SQL_DELETE_PLAN_QUEUE_ENTRY_BY_PLAN =
        new StringBuilder("delete from TPSD_PAYMENT_PLAN_QUEUE ")
        .append("where PLAN_ID=? and NODE_ID is null")
        .toString();

    /** Sql to determine invoice existence. */
    private static final String SQL_DOES_EXIST_INVOICE_BY_DATE =
        new StringBuilder("select count(PPI.INVOICE_ID) \"INVOICE_COUNT\" ")
        .append("from TPSD_PAYMENT_PLAN_INVOICE PPI ")
        .append("where PPI.PLAN_ID=? and PPI.INVOICE_DATE=?")
        .toString();

    /** Sql to determine invoice existence. */
    private static final String SQL_DOES_EXIST_INVOICE_BY_NUMBER =
        new StringBuilder("select count(PPI.INVOICE_ID) \"INVOICE_COUNT\" ")
        .append("from TPSD_PAYMENT_PLAN_INVOICE PPI ")
        .append("where PPI.PLAN_ID=? and PPI.INVOICE_NUMBER=?")
        .toString();

    /** Sql to determine plan existence by primary key. */
    private static final String SQL_DOES_EXIST_USER_PLAN_PK =
        new StringBuilder("select count(PLAN_ID) \"PLAN_COUNT\" ")
        .append("from TPSD_USER_PAYMENT_PLAN UPP ")
        .append("inner join TPSD_PAYMENT_PLAN PP on PP.PLAN_ID=UPP.PLAN_ID ")
        .append("where UPP.USER_ID=?")
        .toString();

    /** Sql to lock an invoice . */
    private static final String SQL_LOCK_INVOICE =
        new StringBuilder("update TPSD_PAYMENT_PLAN_INVOICE_LOCK ")
        .append("set NODE_ID=? ")
        .append("where INVOICE_ID=? and NODE_ID is null")
        .toString();

    /** Sql to lock a plan. */
    private static final String SQL_LOCK_PLAN =
        new StringBuilder("update TPSD_PAYMENT_PLAN_LOCK ")
        .append("set NODE_ID=? where PLAN_ID=? and NODE_ID is null")
        .toString();

    /** Sql to read a currency. */
    private static final String SQL_READ_CURRENCY =
        new StringBuilder("select PC.CURRENCY_ID,PC.CURRENCY_NAME ")
        .append("from TPSD_PAYMENT_CURRENCY PC ")
        .append("where PC.CURRENCY_NAME=?")
        .toString();

    /** Sql to read payment info via a plan. */
    private static final String SQL_READ_INFO =
        new StringBuilder("select PI.INFO_CARD_EXPIRY_MONTH,")
        .append("PI.INFO_CARD_EXPIRY_YEAR,PI.INFO_CARD_NAME,")
        .append("PI.INFO_CARD_NUMBER,PI.INFO_ID ")
        .append("from TPSD_PAYMENT_INFO PI ")
        .append("inner join TPSD_PAYMENT_PLAN_PAYMENT_INFO PPPI ")
        .append("on PPPI.INFO_ID=PI.INFO_ID ")
        .append("where PPPI.PLAN_ID=?")
        .toString();

    /** Sql to read an invoice count. */
    private static final String SQL_READ_INVOICE_COUNT =
        new StringBuilder("select count(PPI.INVOICE_ID) \"INVOICE_COUNT\" ")
        .append("from TPSD_PAYMENT_PLAN_INVOICE PPI ")
        .toString();

    /** Sql to read invoice items. */
    private static final String SQL_READ_INVOICE_ITEMS =
        new StringBuilder("select PPI.ITEM_NUMBER,PPI.ITEM_DESCRIPTION,")
        .append("PPI.ITEM_AMOUNT ")
        .append("from TPSD_PAYMENT_PLAN_INVOICE_ITEM PPI ")
        .append("where PPI.INVOICE_ID=? ")
        .append("order by PPI.ITEM_NUMBER asc")
        .toString();

    /** Sql to read a plan for an invoice. */
    private static final String SQL_READ_INVOICE_PLAN =
        new StringBuilder("select PP.PLAN_ARREARS,PP.PLAN_BILLABLE,")
        .append("PP.PLAN_ID,PC.CURRENCY_ID,PC.CURRENCY_NAME,PP.PLAN_UNIQUE_ID,")
        .append("PP.PLAN_NAME,PP.PLAN_PASSWORD,PP.INVOICE_PERIOD,")
        .append("PP.INVOICE_PERIOD_OFFSET,O.USER_ID,O.USERNAME ")
        .append("from TPSD_PAYMENT_PLAN PP ")
        .append("inner join TPSD_PAYMENT_CURRENCY PC on PC.CURRENCY_ID=PP.PLAN_CURRENCY ")
        .append("inner join TPSD_PAYMENT_PLAN_INVOICE PPI ")
        .append("on PPI.PLAN_ID=PP.PLAN_ID ")
        .append("inner join TPSD_USER O on O.USER_ID=PP.PLAN_OWNER ")
        .append("where PPI.INVOICE_ID=?")
        .toString();

    /** Sql to read an invoice by the invoice date descending. */
    private static final String SQL_READ_INVOICES =
        new StringBuilder("select PPI.INVOICE_ID,PPI.INVOICE_NUMBER,")
        .append("PPI.INVOICE_DATE,PPI.PAYMENT_DATE ")
        .append("from TPSD_PAYMENT_PLAN_INVOICE PPI ")
        .append("where PPI.PLAN_ID=? ")
        .append("order by PPI.INVOICE_DATE desc")
        .toString();

    /** Sql to read locked playment plan queue entries. */
    private static final String SQL_READ_INVOICES_LOCKED =
        new StringBuilder("select PPI.INVOICE_ID,PPI.INVOICE_NUMBER,")
        .append("PPI.INVOICE_DATE,PPI.PAYMENT_DATE ")
        .append("from TPSD_PAYMENT_PLAN_INVOICE PPI ")
        .append("inner join TPSD_PAYMENT_PLAN_INVOICE_LOCK PPIL ")
        .append("on PPIL.INVOICE_ID=PPI.INVOICE_ID ")
        .append("where PPIL.NODE_ID=? ")
        .append("order by PPI.INVOICE_DATE asc")
        .toString();

    /** Sql to read invoices to lock. */ 
    private static final String SQL_READ_INVOICES_TO_LOCK =
        new StringBuilder("select PPI.INVOICE_ID,PPI.INVOICE_NUMBER,")
        .append("PPI.INVOICE_DATE,PPI.PAYMENT_DATE ")
        .append("from TPSD_PAYMENT_PLAN_INVOICE PPI ")
        .append("inner join TPSD_PAYMENT_PLAN_INVOICE_LOCK PPIL ")
        .append("on PPIL.INVOICE_ID=PPI.INVOICE_ID ")
        .append("where PPI.INVOICE_DATE <= ? ")
        .append("and PPI.PAYMENT_DATE is null ")
        .append("and PPIL.NODE_ID is null ")
        .append("order by PPI.INVOICE_DATE asc")
        .toString();

    /** Sql to read a count of invoices to lock. */ 
    private static final String SQL_READ_LOCKABLE_INVOICE_COUNT =
        new StringBuilder("select count(PPI.INVOICE_ID) \"INVOICE_COUNT\" ")
        .append("from TPSD_PAYMENT_PLAN_INVOICE PPI ")
        .append("inner join TPSD_PAYMENT_PLAN_INVOICE_LOCK PPIL ")
        .append("on PPIL.INVOICE_ID=PPI.INVOICE_ID ")
        .append("where PPI.INVOICE_DATE <= ? ")
        .append("and PPI.PAYMENT_DATE is null ")
        .append("and PPIL.NODE_ID is null")
        .toString();

    /** Sql to read a locked plan count. */
    private static final String SQL_READ_LOCKED_PLAN_COUNT =
        new StringBuilder("select count(PPL.PLAN_ID) \"PLAN_COUNT\" ")
        .append("from TPSD_PAYMENT_PLAN_LOCK PPL ")
        .append("where PPL.NODE_ID=?")
        .toString();

    /** Sql to read the plan count. */
    private static final String SQL_READ_PLAN_COUNT =
        new StringBuilder("select count(PP.PLAN_ID) \"PLAN_COUNT\" ")
        .append("from TPSD_PAYMENT_PLAN PP ")
        .toString();

    /** Sql to read the plan info id. */
    private static final String SQL_READ_PLAN_INFO_ID =
        new StringBuilder("select PPPI.INFO_ID ")
        .append("from TPSD_PAYMENT_PLAN_PAYMENT_INFO PPPI ")
        .append("where PPPI.PLAN_ID=?")
        .toString();

    /** Sql to read a payment plan by its uk. */
    private static final String SQL_READ_PLAN_UK =
        new StringBuilder("select PP.PLAN_ARREARS,PP.PLAN_BILLABLE,PP.PLAN_ID,")
        .append("PC.CURRENCY_ID,PC.CURRENCY_NAME,PP.PLAN_UNIQUE_ID,")
        .append("PP.PLAN_NAME,PP.PLAN_PASSWORD,PP.INVOICE_PERIOD,")
        .append("PP.INVOICE_PERIOD_OFFSET,O.USER_ID,O.USERNAME ")
        .append("from TPSD_PAYMENT_PLAN PP ")
        .append("inner join TPSD_PAYMENT_CURRENCY PC on PC.CURRENCY_ID=PP.PLAN_CURRENCY ")
        .append("inner join TPSD_USER O on O.USER_ID=PP.PLAN_OWNER ")
        .append("where PP.PLAN_NAME=? and PP.PLAN_PASSWORD=? ")
        .append("and PP.PLAN_PASSWORD is not null")
        .toString();

    /** Sql to read plan users. */
    private static final String SQL_READ_PLAN_USERS =
        new StringBuilder("select U.USER_ID,U.USERNAME ")
        .append("from TPSD_USER_PAYMENT_PLAN UPP ")
        .append("inner join TPSD_USER U on U.USER_ID=UPP.USER_ID ")
        .append("where UPP.PLAN_ID=?")
        .toString();

    /** Sql to read locked plans. */
    private static final String SQL_READ_PLANS_LOCKED =
        new StringBuilder("select PP.PLAN_ARREARS,PP.PLAN_BILLABLE,PP.PLAN_ID,")
        .append("PC.CURRENCY_ID,PC.CURRENCY_NAME,PP.PLAN_UNIQUE_ID,")
        .append("PP.PLAN_NAME,PP.PLAN_PASSWORD,PP.INVOICE_PERIOD,")
        .append("PP.INVOICE_PERIOD_OFFSET,O.USER_ID,O.USERNAME ")
        .append("from TPSD_PAYMENT_PLAN PP ")
        .append("inner join TPSD_PAYMENT_CURRENCY PC on PC.CURRENCY_ID=PP.PLAN_CURRENCY ")
        .append("inner join TPSD_USER O on O.USER_ID=PP.PLAN_OWNER ")
        .append("inner join TPSD_PAYMENT_PLAN_LOCK PPL on PPL.PLAN_ID=PP.PLAN_ID ")
        .append("where PPL.NODE_ID=? ")
        .append("order by PP.PLAN_ID asc")
        .toString();

    /** Sql to read a list of plans to lock. */
    private static final String SQL_READ_PLANS_TO_LOCK =
        new StringBuilder("select PP.PLAN_ARREARS,PP.PLAN_BILLABLE,PP.PLAN_ID,")
        .append("PC.CURRENCY_ID,PC.CURRENCY_NAME,PP.PLAN_UNIQUE_ID,")
        .append("PP.PLAN_NAME,PP.PLAN_PASSWORD,PP.INVOICE_PERIOD,")
        .append("PP.INVOICE_PERIOD_OFFSET,O.USER_ID,O.USERNAME ")
        .append("from TPSD_PAYMENT_PLAN PP ")
        .append("inner join TPSD_PAYMENT_CURRENCY PC on PC.CURRENCY_ID=PP.PLAN_CURRENCY ")
        .append("inner join TPSD_USER O on O.USER_ID=PP.PLAN_OWNER ")
        .append("inner join TPSD_PAYMENT_PLAN_LOCK PPL on PPL.PLAN_ID=PP.PLAN_ID ")
        .append("where PPL.NODE_ID is null ")
        .append("order by PP.PLAN_ID asc")
        .toString();

    /** Sql to read the provider class by unique key. */
    private static final String SQL_READ_PROVIDER_CLASS_UK =
        new StringBuilder("select PP.PROVIDER_CLASS ")
        .append("from TPSD_PAYMENT_PROVIDER PP ")
        .append("where PP.PROVIDER_NAME=?")
        .toString();

    /** Sql to read the provider class by unique key. */
    private static final String SQL_READ_PROVIDER_CONFIGURATION_UK =
        new StringBuilder("select PPC.CONFIGURATION_KEY,")
        .append("PPC.CONFIGURATION_VALUE ")
        .append("from TPSD_PAYMENT_PROVIDER_CONFIGURATION PPC ")
        .append("inner join TPSD_PAYMENT_PROVIDER PP ")
        .append("on PP.PROVIDER_ID=PPC.PROVIDER_ID ")
        .append("where PP.PROVIDER_NAME=? order by PPC.CONFIGURATION_KEY asc")
        .toString();

    /** Sql to read the provider id by its unique key. */
    private static final String SQL_READ_PROVIDER_ID_UK =
        new StringBuilder("select PP.PROVIDER_ID ")
        .append("from TPSD_PAYMENT_PROVIDER PP ")
        .append("where PP.PROVIDER_NAME=?")
        .toString();

    /** Sql to read the user plan's arrears flag. */
    private static final String SQL_READ_USER_PLAN_ARREARS =
        new StringBuilder("select PP.PLAN_ARREARS ")
        .append("from TPSD_PAYMENT_PLAN PP ")
        .append("inner join TPSD_USER_PAYMENT_PLAN UPP on UPP.PLAN_ID=PP.PLAN_ID ")
        .append("where UPP.USER_ID=?")
        .toString();

    /** Sql to read a user's payment plan by its uk. */
    private static final String SQL_READ_USER_PLAN_UK =
        new StringBuilder("select PP.PLAN_ARREARS,PP.PLAN_BILLABLE,PP.PLAN_ID,")
        .append("PC.CURRENCY_ID,PC.CURRENCY_NAME,PP.PLAN_UNIQUE_ID,")
        .append("PP.PLAN_NAME,PP.PLAN_PASSWORD,PP.INVOICE_PERIOD,")
        .append("PP.INVOICE_PERIOD_OFFSET,O.USER_ID,O.USERNAME ")
        .append("from TPSD_PAYMENT_PLAN PP ")
        .append("inner join TPSD_PAYMENT_CURRENCY PC on PC.CURRENCY_ID=PP.PLAN_CURRENCY ")
        .append("inner join TPSD_USER_PAYMENT_PLAN UPP ")
        .append("on UPP.PLAN_ID=PP.PLAN_ID ")
        .append("inner join TPSD_USER O on O.USER_ID=PP.PLAN_OWNER ")
        .append("where UPP.USER_ID=?")
        .toString();

    /** Sql to unlock an invoice. */
    private static final String SQL_UNLOCK_INVOICE =
        new StringBuilder("update TPSD_PAYMENT_PLAN_INVOICE_LOCK ")
        .append("set NODE_ID=null ")
        .append("where INVOICE_ID=? and NODE_ID=?")
        .toString();

    /** Sql to unlock a plan. */
    private static final String SQL_UNLOCK_PLANS =
        new StringBuilder("update TPSD_PAYMENT_PLAN_LOCK ")
        .append("set NODE_ID=null where NODE_ID=?")
        .toString();

    /** Sql to update the info. */
    private static final String SQL_UPDATE_INFO =
        new StringBuilder("update TPSD_PAYMENT_INFO ")
        .append("set INFO_CARD_NAME=?,INFO_CARD_NUMBER=?,")
        .append("INFO_CARD_EXPIRY_MONTH=?,INFO_CARD_EXPIRY_YEAR=? ")
        .append("where INFO_ID=?")
        .toString();

    /** Sql to set the invoice payment date. */
    private static final String SQL_UPDATE_INVOICE_PAYMENT_DATE =
        new StringBuilder("update TPSD_PAYMENT_PLAN_INVOICE ")
        .append("set PAYMENT_DATE=? where INVOICE_ID=?")
        .toString();

    /** Sql to update the plan's arrears flag. */
    private static final String SQL_UPDATE_PLAN_ARREARS =
        new StringBuilder("update TPSD_PAYMENT_PLAN ")
        .append("set PLAN_ARREARS=? ")
        .append("where PLAN_ID=?")
        .toString();

    /** Sql to update a transaction's success. */
    private static final String SQL_UPDATE_TRANSACTION_SUCCESS =
        new StringBuilder("update TPSD_PAYMENT_PLAN_INVOICE_TX ")
        .append("set TX_SUCCESS=? where TX_ID=?")
        .toString();

    /** A user sql interface. */
    private final UserSql userSql;

    /**
     * Create PaymentSql.
     * 
     * @param dataSource
     *            A <code>DataSource</code>.
     */
    public PaymentSql(final DataSource dataSource) {
        super(dataSource);
        this.userSql = new UserSql(dataSource);
    }

    /**
     * Create payment info.
     * 
     * @param plan
     *            A <code>PaymentPlan</code>.
     * @param provider
     *            A <code>PaymentProvider</code>.
     * @param info
     *            A <code>PaymentInfo</code>.
     */
    public void createInfo(final PaymentPlan plan,
            final PaymentProvider provider, final PaymentInfo info) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_CREATE_INFO);
            session.setLong(1, lookupId(provider));
            session.setString(2, info.getCardName().name());
            session.setString(3, info.getCardNumber());
            session.setString(4, String.valueOf(info.getCardExpiryMonth()));
            session.setString(5, String.valueOf(info.getCardExpiryYear()));
            if (1 != session.executeUpdate()) {
                throw panic("Could not create payment info.");
            }
            info.setLocalId(session.getIdentity("TPSD_PAYMENT_INFO"));

            session.prepareStatement(SQL_CREATE_PLAN_INFO);
            session.setLong(1, plan.getId());
            session.setLong(2, info.getLocalId());
            if (1 != session.executeUpdate()) {
                throw panic("Could not create plan info.");
            }
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }
    /**
     * Create an invoice.
     * 
     * @param plan
     *            A <code>PaymentPlan</code>.
     * @param invoice
     *            An <code>Invoice</code>.
     * @param itemList
     *            A <code>List<InvoiceItem></code>.
     */
    public void createInvoice(final PaymentPlan plan, final Invoice invoice,
            final List<InvoiceItem> itemList) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_CREATE_INVOICE);
            session.setLong(1, plan.getId());
            session.setInt(2, invoice.getNumber());
            session.setDate(3, invoice.getDate());
            if (1 != session.executeUpdate()) {
                throw panic("Could not create invoice.");
            }
            invoice.setId(session.getIdentity("TPSD_PAYMENT_PLAN_INVOICE"));

            session.prepareStatement(SQL_CREATE_INVOICE_LOCK);
            session.setLong(1, invoice.getId());
            if (1 != session.executeUpdate()) {
                throw panic("Could not create invoice lock.");
            }

            session.prepareStatement(SQL_CREATE_INVOICE_ITEM);
            session.setLong(1, invoice.getId());
            for (int i = 0; i < itemList.size(); i++) {
                session.setInt(2, i);
                session.setString(3, itemList.get(i).getDescription());
                session.setLong(4, itemList.get(i).getAmount());
                if (1 != session.executeUpdate()) {
                    throw panic("Could not create invoice item.");
                }
            }
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Create a plan.
     * 
     * @param plan
     *            A <code>PaymentPlan</code>.
     * @param filters
     *            A <code>List<PaymentPlanFilter></code>.
     */
    public void createPlan(final PaymentPlan plan) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_CREATE_PLAN);
            session.setUniqueId(1, plan.getUniqueId());
            session.setString(2, plan.getName());
            session.setInt(3, plan.getCurrency().getId());
            session.setString(4, plan.getPassword());
            session.setBoolean(5, plan.isBillable());
            session.setLong(6, plan.getOwner().getLocalId());
            session.setString(7, plan.getInvoicePeriod().name());
            session.setInt(8, plan.getInvoicePeriodOffset());
            session.setBoolean(9, plan.isArrears());
            if (1 != session.executeUpdate()) {
                throw panic("Could not create payment plan.");
            }
            plan.setId(session.getIdentity("TPSD_PAYMENT_PLAN"));

            session.prepareStatement(SQL_CREATE_PLAN_LOCK);
            session.setLong(1, plan.getId());
            if (1 != session.executeUpdate()) {
                throw panic("Could not create payment plan lock.");
            }
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Create a plan queue entry.
     * 
     * @param plan
     *            A <code>PaymentPlan</code>.
     */
    public void createPlanQueueEntry(final PaymentPlan plan) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_CREATE_PLAN_QUEUE_ENTRY);
            session.setLong(1, plan.getId());
            if (1 != session.executeUpdate()) {
                throw panic("Could not create plan queue event.");
            }

        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Create an invoice transaction.
     * 
     * @param transaction
     *            An <code>InvoiceTransaction</code>.
     */
    public void createTransaction(final Invoice invoice,
            final InvoiceTransaction transaction) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_CREATE_TRANSACTION);
            session.setLong(1, invoice.getId());
            session.setUniqueId(2, transaction.getUniqueId());
            session.setCalendar(3, transaction.getDate());
            if (1 != session.executeUpdate()) {
                throw panic("Could not create transaction.");
            }
            transaction.setId(session.getIdentity("TPSD_PAYMENT_PLAN_INVOICE_TX"));

        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Delete a plan queue entry.
     * 
     * @param plan
     *            A <code>PaymentPlan</code>.
     */
    public void deleteInvoiceLock(final Node node, final Invoice invoice) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_DELETE_INVOICE_LOCK);
            session.setLong(1, invoice.getId());
            session.setLong(2, node.getId());
            if (1 != session.executeUpdate()) { 
                throw panic("Could not delete invoice lock.");
            }
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Delete a plan queue entry.
     * 
     * @param plan
     *            A <code>PaymentPlan</code>.
     */
    public void deletePlanQueueEntry(final PaymentPlan plan) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_DELETE_PLAN_QUEUE_ENTRY_BY_PLAN);
            session.setLong(1, plan.getId());
            final int rows = session.executeUpdate();
            if (0 > rows || 1 < rows) { 
                throw panic("Could not delete plan queue event.");
            }

        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Determine if an invoice of a given date exists.
     * 
     * @param plan
     *            A <code>PaymentPlan</code>.
     * @param number
     *            A <code>Integer</code>.
     * @return True if an invoice exists.
     */
    public Boolean doesExistInvoice(final PaymentPlan plan, final Calendar date) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_DOES_EXIST_INVOICE_BY_DATE);
            session.setLong(1, plan.getId());
            session.setDate(2, date);
            session.executeQuery();
            if (session.nextResult()) {
                final int invoiceCount = session.getInteger("INVOICE_COUNT");
                if (0 == invoiceCount) {
                    return Boolean.FALSE;
                } else if (1 == invoiceCount) {
                    return Boolean.TRUE;
                } else {
                    throw new HypersonicException("Could not determine invoice existence.");
                }
            } else {
                throw new HypersonicException("Could not determine invoice existence.");
            }
        } finally {
            session.close();
        }
    }

    /**
     * Determine if an invoice of a given number exists.
     * 
     * @param plan
     *            A <code>PaymentPlan</code>.
     * @param number
     *            A <code>Integer</code>.
     * @return True if an invoice exists.
     */
    public Boolean doesExistInvoice(final PaymentPlan plan, final Integer number) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_DOES_EXIST_INVOICE_BY_NUMBER);
            session.setLong(1, plan.getId());
            session.setInt(2, number);
            session.executeQuery();
            if (session.nextResult()) {
                final int invoiceCount = session.getInteger("INVOICE_COUNT");
                if (0 == invoiceCount) {
                    return Boolean.FALSE;
                } else if (1 == invoiceCount) {
                    return Boolean.TRUE;
                } else {
                    throw new HypersonicException("Could not determine invoice existence.");
                }
            } else {
                throw new HypersonicException("Could not determine invoice existence.");
            }
        } finally {
            session.close();
        }
    }

    /**
     * Determine if a plan exists for a user.
     * 
     * @param user
     *            A <code>User</code>.
     * @return True if a plan exists.
     */
    public Boolean doesExistPlan(final User user) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_DOES_EXIST_USER_PLAN_PK);
            session.setLong(1, user.getLocalId());
            session.executeQuery();
            if (session.nextResult()) {
                final int planCount = session.getInteger("PLAN_COUNT");
                if (0 == planCount) {
                    return Boolean.FALSE;
                } else if (1 == planCount) {
                    return Boolean.TRUE;
                } else {
                    throw new HypersonicException("Could not determine plan count.");
                }
            } else {
                throw new HypersonicException("Could not determine plan count.");
            }
        } finally {
            session.close();
        }
    }

    /**
     * Determine if the user's payment plan is in arrears.
     * 
     * @param user
     *            A <code>User</code>.
     * @return True if the plan is in arrears.
     */
    public Boolean isUserPlanInArrears(final User user) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_USER_PLAN_ARREARS);
            session.setLong(1, user.getLocalId());
            session.executeQuery();
            if (session.nextResult()) {
                return session.getBoolean("PLAN_ARREARS");
            } else {
                return null;
            }
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Lock a list of invoices for a node. The outcome will be a number of locks
     * n; in the invoice lock table where 0 <= n < limit. The actual number of
     * locks obtained will be returned.
     * 
     * @param node
     *            A <code>Node</code>.
     * @param limit
     *            An <code>Integer</code>.
     * @param currentDate
     *            A <code>Calendar</code>.
     * @return An <code>Integer</code>.
     */
    public Integer lockInvoices(final Node node, final Integer offset,
            final Integer limit, final Calendar currentDate) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_INVOICES_TO_LOCK);
            session.setDate(1, currentDate);
            session.executeQuery();
            final List<Invoice> invoiceList = new ArrayList<Invoice>(limit);
            while (session.nextResult()) {
                invoiceList.add(extractInvoice(session));
            }

            if (0 > offset || invoiceList.size() - 1 < offset) {
                return Integer.valueOf(0);
            }

            int locked = 0;
            session.prepareStatement(SQL_LOCK_INVOICE);
            session.setLong(1, node.getId());
            int rows;
            for (int i = offset; i < invoiceList.size() && i - offset < limit; i++) {
                session.setLong(2, invoiceList.get(i).getId());
                rows = session.executeUpdate();
                if (0 > rows || 1 < rows) {
                    throw new HypersonicException("Could not lock plan queue entries.");
                }
                locked += rows;
            }
            return Integer.valueOf(locked);
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Lock a list of plans for a node. The outcome will be a number of locks n;
     * in the plan lock table where 0 <= n < limit. The actual number of locks
     * obtained will be returned.
     * 
     * @param node
     *            A <code>Node</code>.
     * @param index
     *            An <code>Integer</code>.
     * @param limit
     *            An <code>Integer</code>.
     * @return An <code>Integer</code>.
     */
    public Integer lockPlans(final Node node, final Integer offset,
            final Integer limit) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_PLANS_TO_LOCK);
            session.executeQuery();
            final List<PaymentPlan> planList = new ArrayList<PaymentPlan>(limit);
            while (session.nextResult()) {
                planList.add(extractPlan(session));
            }

            if (0 > offset || planList.size() - 1 < offset) {
                return Integer.valueOf(0);
            }

            int locked = 0;
            session.prepareStatement(SQL_LOCK_PLAN);
            session.setLong(1, node.getId());
            int rows;
            for (int i = offset; i < planList.size() && i - offset < limit; i++) {
                session.setLong(2, planList.get(i).getId());
                rows = session.executeUpdate();
                if (0 > rows || 1 < rows) {
                    throw new HypersonicException("Could not lock plan.");
                }
                locked += rows;
            }
            return Integer.valueOf(locked);
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Read a currency.
     * 
     * @param name
     *            A name <code>String</code>.
     * @return A <code>Currency</code>.
     */
    public Currency readCurrency(final String name) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_CURRENCY);
            session.setString(1, name);
            session.executeQuery();
            if (session.nextResult()) {
                return extractCurrency(session);
            } else {
                return null;
            }
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Read payment info.
     * 
     * @param plan
     *            A <code>PaymentPlan</code>.
     * @return A <code>PaymentInfo</code>.
     */
    public PaymentInfo readInfo(final PaymentPlan plan) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_INFO);
            session.setLong(1, plan.getId());
            session.executeQuery();
            if (session.nextResult()) {
                return extractInfo(session);
            } else {
                return null;
            }
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Read the total number of invoices.
     * 
     * @return An <code>Integer</code>.
     */
    public Integer readInvoiceCount() {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_INVOICE_COUNT);
            session.executeQuery();
            session.nextResult();
            return session.getInteger("INVOICE_COUNT");
        } finally {
            session.close();
        }
    }

    /**
     * Read the items for an invoice.
     * 
     * @param invoice
     *            An <code>Invoice</code>.
     * @return A <code>List<InvoiceItem></code>.
     */
    public List<InvoiceItem> readInvoiceItems(final Invoice invoice) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_INVOICE_ITEMS);
            session.setLong(1, invoice.getId());
            session.executeQuery();
            final List<InvoiceItem> itemList = new ArrayList<InvoiceItem>();
            while (session.nextResult()) {
                itemList.add(extractInvoiceItem(session));
            }
            return itemList;
        } finally {
            session.close();
        }
    }

    /**
     * Read the latest invoice for the plan.
     * 
     * @param plan
     *            A <code>PaymentPlan</code>.
     * @return An <code>Invoice</code>.
     */
    public Invoice readLatestInvoice(final PaymentPlan plan) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_INVOICES);
            session.setLong(1, plan.getId());
            session.executeQuery();
            if (session.nextResult()) {
                return extractInvoice(session);
            } else {
                return null;
            }
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Read the total number of lockable invoices.
     * 
     * @return An <code>Integer</code>.
     */
    public Integer readLockableInvoiceCount(final Calendar currentDate) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_LOCKABLE_INVOICE_COUNT);
            session.setDate(1, currentDate);
            session.executeQuery();
            session.nextResult();
            return session.getInteger("INVOICE_COUNT");
        } finally {
            session.close();
        }
    }

    /**
     * Read the locked invoices for the node.
     * 
     * @param node
     *            A <code>Node</code>.
     * @return A <code>List<Invoice></code>.
     */
    public List<Invoice> readLockedInvoices(final Node node) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_INVOICES_LOCKED);
            session.setLong(1, node.getId());
            session.executeQuery();
            final List<Invoice> invoiceList = new ArrayList<Invoice>();
            while (session.nextResult()) {
                invoiceList.add(extractInvoice(session));
            }
            return invoiceList;
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Read the locked invoices for the node.
     * 
     * @param node
     *            A <code>Node</code>.
     * @return A <code>List<PaymentPlan></code>.
     */
    public List<PaymentPlan> readLockedPlans(final Node node) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_PLANS_LOCKED);
            session.setLong(1, node.getId());
            session.executeQuery();
            final List<PaymentPlan> planList = new ArrayList<PaymentPlan>();
            while (session.nextResult()) {
                planList.add(extractPlan(session));
            }
            return planList;
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Read an invoice's plan.
     * 
     * @param invoice
     *            An <code>Invoice</code>.
     * @return A <code>PaymentPlan</code>.
     */
    public PaymentPlan readPlan(final Invoice invoice) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_INVOICE_PLAN);
            session.setLong(1, invoice.getId());
            session.executeQuery();
            if (session.nextResult()) {
                return extractPlan(session);
            } else {
                return null;
            }
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Read a payment plan.
     * 
     * @param planCredentials
     *            A <code>PaymentPlanCredentials</code>.
     * @return A <code>PaymentPlan</code>.
     */
    public PaymentPlan readPlan(final PaymentPlanCredentials planCredentials) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_PLAN_UK);
            session.setString(1, planCredentials.getName());
            session.setString(2, planCredentials.getPassword());
            session.executeQuery();
            if (session.nextResult()) {
                return extractPlan(session);
            } else {
                return null;
            }
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Read the payment plan for the user.
     * 
     * @param user
     *            A <code>User</code>.
     * @return A <code>PaymentPlan</code>.
     */
    public PaymentPlan readPlan(final User user) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_USER_PLAN_UK);
            session.setLong(1, user.getLocalId());
            session.executeQuery();
            if (session.nextResult()) {
                return extractPlan(session);
            } else {
                return null;
            }
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Read the total number of plans.
     * 
     * @return An <code>Integer</code>.
     */
    public Integer readPlanCount() {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_PLAN_COUNT);
            session.executeQuery();
            session.nextResult();
            return session.getInteger("PLAN_COUNT");
        } finally {
            session.close();
        }
    }

    /**
     * Read the users attached to the plan.
     * 
     * @param plan
     *            A <code>PaymentPlan</code>.
     * @return A <code>List<User></code>.
     */
    public List<User> readPlanUsers(final PaymentPlan plan) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_PLAN_USERS);
            session.setLong(1, plan.getId());
            session.executeQuery();
            final List<User> userList = new ArrayList<User>();
            while (session.nextResult()) {
                userList.add(extractUser(session));
            }
            return userList;
        } finally {
            session.close();
        }
    }

    /**
     * Read the provider class.
     * 
     * @param name
     *            A provider name <code>String</code>.
     * @return A fully qualified class name <code>String</code>.
     */
    public String readProviderClass(final String name) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_PROVIDER_CLASS_UK);
            session.setString(1, name);
            session.executeQuery();
            if (session.nextResult()) {
                return session.getString("PROVIDER_CLASS");
            } else {
                return null;
            }
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Read the provider configuration.
     * 
     * @param name
     *            A provider name <code>String</code>.
     * @return A <code>Properties</code>.
     */
    public Properties readProviderConfiguration(final String name) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_PROVIDER_CONFIGURATION_UK);
            session.setString(1, name);
            session.executeQuery();
            final Properties properties = new Properties();
            while (session.nextResult()) {
                properties.setProperty(session.getString("CONFIGURATION_KEY"),
                        session.getString("CONFIGURATION_VALUE"));
            }
            return properties;
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Unlock an invoice.
     * 
     * @param node
     *            A <code>Node</code>.
     * @param invoice
     *            An <code>Invoice</code>.
     */
    public void unlockInvoice(final Node node, final Invoice invoice) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_UNLOCK_INVOICE);
            session.setLong(1, invoice.getId());
            session.setLong(2, node.getId());
            if (1 != session.executeUpdate()) {
                throw panic("Could not unlock queue entry.");
            }
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Unlock an plan.
     * 
     * @param node
     *            A <code>Node</code>.
     * @param plan
     *            A <code>PaymentPlan</code>.
     */
    public void unlockPlans(final Node node) {
        final HypersonicSession session = openSession();
        try {
            final int lockedPlanCount = readLockedPlanCount(session, node).intValue();
            session.prepareStatement(SQL_UNLOCK_PLANS);
            session.setLong(1, node.getId());
            if (lockedPlanCount != session.executeUpdate()) {
                throw panic("Could not unlock plans.");
            }
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Update the payment plan info.
     * 
     * @param plan
     *            A <code>PaymentPlan</code>.
     * @param info
     *            A <code>PaymentInfo</code>.
     */
    public void updateInfo(final PaymentPlan plan, final PaymentInfo info) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_UPDATE_INFO);
            session.setString(1, info.getCardName().name());
            session.setString(2, info.getCardNumber());
            session.setString(3, String.valueOf(info.getCardExpiryMonth()));
            session.setString(4, String.valueOf(info.getCardExpiryYear()));
            session.setLong(5, lookupInfoId(plan));

            if (1 != session.executeUpdate()) {
                throw panic("Could not update payment info.");
            }
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Update the invoice payment date.
     * 
     * @param invoice
     *            An <code>Invoice</code>.
     */
    public void updateInvoicePaymentDate(final Invoice invoice) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_UPDATE_INVOICE_PAYMENT_DATE);
            session.setDate(1, invoice.getPaymentDate());
            session.setLong(2, invoice.getId());
            if (1 != session.executeUpdate()) {
                throw panic("Could not update invoice payment date.");
            }
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Update the plan's arrears flag.
     * 
     * @param plan
     *            A <code>PaymentPlan</code>.
     */
    public void updatePlanArrears(final PaymentPlan plan) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_UPDATE_PLAN_ARREARS);
            session.setBoolean(1, plan.isArrears());
            session.setLong(2, plan.getId());
            if (1 != session.executeUpdate()) {
                throw panic("Could not update plan arrears.");
            }
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Update the transaction success.
     * 
     * @param transaction
     *            An <code>InvoiceTransaction</code>.
     */
    public void updateTransactionSuccess(final InvoiceTransaction transaction) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_UPDATE_TRANSACTION_SUCCESS);
            session.setBoolean(1, transaction.isSuccess());
            session.setLong(2, transaction.getId());
            if (1 != session.executeUpdate()) {
                throw panic("Could not update transaction success.");
            }
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Extract a currency from the database session.
     * 
     * @param session
     *            A <code>HypersonicSession</code>.
     * @return A <code>Currency</code>.
     */
    private Currency extractCurrency(final HypersonicSession session) {
        final Currency currency = new Currency();
        currency.setId(session.getInteger("CURRENCY_ID"));
        currency.setName(session.getString("CURRENCY_NAME"));
        return currency;
    }

    /**
     * Extract payment info from the session.
     * 
     * @param session
     *            A <code>HypersonicSession</code>.
     * @return A <code>PaymentInfo</code>.
     */
    private PaymentInfo extractInfo(final HypersonicSession session) {
        final PaymentInfo info = new PaymentInfo();
        info.setCardExpiryMonth(Short.valueOf(session.getString("INFO_CARD_EXPIRY_MONTH")));
        info.setCardExpiryYear(Short.valueOf(session.getString("INFO_CARD_EXPIRY_YEAR")));
        info.setCardName(CardName.valueOf(session.getString("INFO_CARD_NAME")));
        info.setCardNumber(session.getString("INFO_CARD_NUMBER"));
        info.setLocalId(session.getLong("INFO_ID"));
        return info;
    }

    /**
     * Extract an invoice from the session.
     * 
     * @param session
     *            A <code>HypersonicSession</code>.
     * @return An <code>Invoice</code>.
     */
    private Invoice extractInvoice(final HypersonicSession session) {
        final Invoice invoice = new Invoice();
        invoice.setDate(session.getCalendar("INVOICE_DATE"));
        invoice.setId(session.getLong("INVOICE_ID"));
        invoice.setNumber(session.getInteger("INVOICE_NUMBER"));
        invoice.setPaymentDate(session.getCalendar("PAYMENT_DATE"));
        return invoice;
    }

    /**
     * Extract an invoice item from the session.
     * 
     * @param session
     *            A <code>HypersonicSession</code>.
     * @return An <code>InvoiceItem</code>.
     */
    private InvoiceItem extractInvoiceItem(final HypersonicSession session) {
        final InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setAmount(session.getLong("ITEM_AMOUNT"));
        invoiceItem.setDescription(session.getString("ITEM_DESCRIPTION"));
        invoiceItem.setNumber(session.getInteger("ITEM_NUMBER"));
        return invoiceItem;
    }

    /**
     * Extract a payment plan from the session. Note that a user is extracted
     * from the session via the user sql interface.
     * 
     * @param session
     *            A <code>HypersonicSession</code>.
     * @return A <code>PaymentPlan</code>.
     */
    private PaymentPlan extractPlan(final HypersonicSession session) {
        final PaymentPlan plan = new PaymentPlan();
        plan.setArrears(session.getBoolean("PLAN_ARREARS"));
        plan.setBillable(session.getBoolean("PLAN_BILLABLE"));
        plan.setCurrency(extractCurrency(session));
        plan.setId(session.getLong("PLAN_ID"));
        plan.setInvoicePeriod(InvoicePeriod.valueOf(session.getString("INVOICE_PERIOD")));
        plan.setInvoicePeriodOffset(session.getInteger("INVOICE_PERIOD_OFFSET"));
        plan.setName(session.getString("PLAN_NAME"));
        plan.setOwner(extractUser(session));
        plan.setPassword(session.getString("PLAN_PASSWORD"));
        plan.setUniqueId(session.getUniqueId("PLAN_UNIQUE_ID"));
        return plan;
    }

    /**
     * Extract a user from the session.
     * 
     * @param session
     *            A <code>HypersonicSession</code>.
     * @return A <code>User</code>.
     */
    private User extractUser(final HypersonicSession session) {
        return userSql.extract(session);
    }

    /**
     * Lookup the provider's id based upon its unique name.
     * 
     * @param provider
     *            A <code>PaymentProvider</code>.
     * @return A provider id <code>Long</code>.
     */
    private Long lookupId(final PaymentProvider provider) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_PROVIDER_ID_UK);
            session.setString(1, provider.getName());
            session.executeQuery();
            if (session.nextResult()) {
                return session.getLong("PROVIDER_ID");
            } else {
                return null;
            }
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Lookup the info id for the payment plan.
     * 
     * @param plan
     *            A <code>PaymentPlan</code>.
     * @return A <code>Long</code>.
     */
    private Long lookupInfoId(final PaymentPlan plan) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_PLAN_INFO_ID);
            session.setLong(1, plan.getId());
            session.executeQuery();
            if (session.nextResult()) {
                return session.getLong("INFO_ID");
            } else {
                return null;
            }
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Read the locked plan count.
     * 
     * @param session
     *            A <code>HypersonicSession</code>.
     * 
     * @param node
     *            A <code>Node</code>.
     * @return An <code>Integer</code>.
     */
    private Integer readLockedPlanCount(final HypersonicSession session,
            final Node node) {
        session.prepareStatement(SQL_READ_LOCKED_PLAN_COUNT);
        session.setLong(1, node.getId());
        session.executeQuery();
        session.nextResult();
        return session.getInteger("PLAN_COUNT");
    }
}
