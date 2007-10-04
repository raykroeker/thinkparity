/*
 * Created On:  9-Aug-07 3:28:29 PM
 */
package com.thinkparity.payment.moneris;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Properties;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.desdemona.model.profile.payment.CardDeclinedException;
import com.thinkparity.desdemona.model.profile.payment.CardExpiredException;
import com.thinkparity.desdemona.model.profile.payment.PaymentException;
import com.thinkparity.desdemona.model.profile.payment.provider.Client;
import com.thinkparity.desdemona.model.profile.payment.provider.Info;
import com.thinkparity.desdemona.model.profile.payment.provider.Payment;
import com.thinkparity.desdemona.model.profile.payment.provider.Transaction;

/**
 * <b>Title:</b>thinkParity Moneris Payment Provider<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class PaymentProvider
        implements
        com.thinkparity.desdemona.model.profile.payment.provider.PaymentProvider {

    /** A dollars/cents amount pattern. */
    private static final String AMOUNT_PATTERN;
   
    /** A card expiry date simple date format. */
    private static final String EXPIRY_DATE_PATTERN;
   
    static {
        AMOUNT_PATTERN = "{0}.{1}";
        EXPIRY_DATE_PATTERN = "{0,date,yy}{0,date,MM}";
    }

    /**
     * Format a payment into an amount for moneris.
     * 
     * @param payment
     *            A <code>Payment</code>.
     * @return A <code>String</code>.
     */
    private static String formatAmount(final Payment payment) {
        final String amountString = String.valueOf(payment.getAmount());
        final String dollars = amountString.length() > 2 ?
                amountString.substring(0, amountString.length() - 2) : "";
        final String cents = amountString.length() > 2 ?
                amountString.substring(amountString.length() - 2) : amountString;
        return MessageFormat.format(AMOUNT_PATTERN, dollars, cents);
    }

    /**
     * Format the info's expiry month/year into a moneris acceptable format.
     * 
     * @param info
     *            An <code>Info</code>.
     * @return A <code>String</code>.
     */
    private static String formatCardExpiryDate(final Info info) {
        final Calendar date = Calendar.getInstance();
        date.set(Calendar.YEAR, info.getCardExpiryYear());
        date.set(Calendar.MONTH, info.getCardExpiryMonth());
        date.set(Calendar.DAY_OF_MONTH, 0);
        date.set(Calendar.HOUR, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        return MessageFormat.format(EXPIRY_DATE_PATTERN, date.getTime());
    }

    /**
     * Format the info's card number into a moneris acceptable format.
     * 
     * @param info
     *            An <code>Info</code>.
     * @return A <code>String</code>.
     */
    private static String formatCardNumber(final Info info) {
        return info.getCardNumber().replace("-", "").replace(" ", "");
    }

    /**
     * Format the client's id into a moneris acceptable format.
     * 
     * @param info
     *            An <code>Info</code>.
     * @return A <code>String</code>.
     */
    private static String formatCustomerId(final Client client) {
        return client.getId();
    }

    /** The client info. */
    private Client client;

    /** A payment environment. */
    private PaymentEnvironment environment;

    /** A payment gateway. */
    private final PaymentGateway gateway;

    /** The payment info. */
    private Info info;

    /**
     * Create PaymentProvider.
     *
     */
    public PaymentProvider() {
        super();
        this.gateway = PaymentGateway.getInstance();
    }

    /**
     * @see com.thinkparity.desdemona.model.profile.payment.provider.PaymentProvider#charge(com.thinkparity.desdemona.model.profile.payment.provider.Transaction,
     *      com.thinkparity.desdemona.model.profile.payment.provider.Payment)
     * 
     */
    @Override
    public void charge(final Transaction transaction, final Payment payment)
            throws CardDeclinedException, CardExpiredException,
            PaymentException {
        ensureOpen();
        ensureInfo();
        ensureClient();
        try {
            gateway.purchase(environment, newTransaction(transaction.getId()),
                    formatAmount(payment), formatCardExpiryDate(info),
                    formatCardNumber(info), formatCustomerId(client));
        } catch (final CardDeclinedException cdx) {
            throw cdx;
        } catch (final CardExpiredException cex) {
            throw cex;
        } catch (final PaymentException px) {
            throw px;
        } catch (final Exception x) {
            throw new PaymentException(x);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.profile.payment.provider.PaymentProvider#configure(java.util.Properties)
     *
     */
    @Override
    public void configure(final Properties properties) {
        this.environment = new PaymentEnvironment();
        this.environment.setAPIToken(properties.getProperty("com.thinkparity.payment.moneris.apitoken"));
        this.environment.setEncryption(properties.getProperty("com.thinkparity.payment.moneris.encryption"));
        this.environment.setHost(properties.getProperty("com.thinkparity.payment.moneris.host"));
        this.environment.setStoreId(properties.getProperty("com.thinkparity.payment.moneris.storeid"));
    }

    /**
     * @see com.thinkparity.desdemona.model.profile.payment.provider.PaymentProvider#getName()
     *
     */
    @Override
    public String getName() {
        return "Moneris";
    }

    /**
     * @see com.thinkparity.desdemona.model.profile.payment.provider.PaymentProvider#isClosed()
     *
     */
    @Override
    public Boolean isClosed() {
        return gateway.isClosed();
    }

    /**
     * @see com.thinkparity.desdemona.model.profile.payment.provider.PaymentProvider#isOpen()
     *
     */
    @Override
    public Boolean isOpen() {
        return gateway.isOpen();
    }

    /**
     * @see com.thinkparity.desdemona.model.profile.payment.provider.PaymentProvider#setClient(com.thinkparity.desdemona.model.profile.payment.provider.Client)
     * 
     */
    @Override
    public void setClient(final Client client) {
        Assert.assertIsNull("Client info cannot be changed.", this.client);
        this.client = client;
    }

    /**
     * @see com.thinkparity.desdemona.model.profile.payment.provider.PaymentProvider#setInfo(com.thinkparity.desdemona.model.profile.payment.provider.Info)
     *
     */
    @Override
    public void setInfo(final Info info) {
        Assert.assertIsNull(this.info, "Payment info cannot be changed.");
        this.info = info;
    }

    /**
     * Ensure client is set.
     * 
     */
    private void ensureClient() {
        if (null == client) {
            throw new IllegalStateException("Client is not set.");
        }
    }

    /**
     * Ensure info is set.
     * 
     */
    private void ensureInfo() {
        if (null == info) {
            throw new IllegalStateException("Info is not set.");
        }
    }

    /**
     * Ensure the payment gateway is open.
     * 
     */
    private void ensureOpen() {
        if (gateway.isClosed()) {
            throw new IllegalStateException("Gateway is not open.");
        }
    }

    /**
     * Create a payment transaction.
     * 
     * @param xid
     *            A <code>String</code>.
     * @return A <code>PaymentTransaction</code>.
     */
    private PaymentTransaction newTransaction(final String xid) {
        final PaymentTransaction transaction = new PaymentTransaction();
        transaction.setId(xid);
        return transaction;
    }
}
