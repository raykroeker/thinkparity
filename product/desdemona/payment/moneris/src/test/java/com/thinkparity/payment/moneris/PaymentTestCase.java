/*
 * Created On:  5-Sep-07 9:34:13 AM
 */
package com.thinkparity.payment.moneris;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.UUID;

import com.thinkparity.codebase.junitx.TestCase;

import com.thinkparity.desdemona.model.profile.payment.provider.Info;
import com.thinkparity.desdemona.model.profile.payment.provider.Transaction;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class PaymentTestCase extends TestCase {

    /**
     * Create PaymentTestCase.
     * 
     * @param name
     *            A test name <code>String</code>.
     */
    public PaymentTestCase(final String name) {
        super(name);
    }

    /**
     * Obtain the current date/time.
     * 
     * @return A <code>Calendar</code>.
     */
    protected final Calendar currentDateTime() {
        return Calendar.getInstance();
    }

    /**
     * Instantiate configuration.
     * 
     * @return A <code>Properties</code>.
     */
    protected final Properties newConfiguration() {
        final Properties configuration = new Properties();
        configuration.setProperty("com.thinkparity.payment.moneris.apitoken", "yesguy");
        configuration.setProperty("com.thinkparity.payment.moneris.encryption", "7");
        configuration.setProperty("com.thinkparity.payment.moneris.host", "esqa.moneris.com");
        configuration.setProperty("com.thinkparity.payment.moneris.storeid", "store3");
        return configuration;
    }

    /**
     * Obtain an amount to generate an expired card error.
     * 
     * @return A <code>Long</code>.
     */
    protected final Long newExpiredCardAmount() {
        return 154L;
    }

    /**
     * Obtain an amount to generate an insufficient funds error.
     * 
     * @return A <code>Long</code>.
     */
    protected final Long newInsufficientFundsAmount() {
        return 151L;
    }

    /**
     * Create an invalid card number info.
     * 
     * @return An <code>Info</code>.
     */
    protected final Info newInvalidCardNumberInfo() {
        final Calendar now = currentDateTime();
        final Info info = new Info();
        info.setCardExpiryMonth((short) 12);
        info.setCardExpiryYear((short) now.get(Calendar.YEAR));
        info.setCardNumber("My card number is garbage.");
        info.setCardName("MasterCard");
        return info;
    }

    /**
     * Create an order.
     * 
     * @return An <code>Order</code>.
     */
    protected final Transaction newTransaction() {
        final Calendar now = currentDateTime();
        final Transaction transaction = new Transaction();
        transaction.setId(MessageFormat.format(
                "thinkParity_{0}_{1,date,yyyy-MM-dd HH:mm:ss.SSS Z}",
                UUID.randomUUID().toString(),
                now.getTime()));
        return transaction;
    }

    /**
     * Obtain an amount.
     * 
     * @return A <code>Long</code>.
     */
    protected final Long newValidAmount() {
        return 100L;
    }

    /**
     * Create a valid info.
     * 
     * @return An <code>Info</code>.
     */
    protected final Info newValidInfo() {
        final Calendar now = currentDateTime();
        final Info info = new Info();
        info.setCardExpiryMonth((short) 12);
        info.setCardExpiryYear((short) now.get(Calendar.YEAR));
        info.setCardNumber("5454545454545454");
        info.setCardName("MasterCard");
        return info;
    }
    /**
     * @see com.thinkparity.codebase.junitx.TestCase#setUp()
     *
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * @see com.thinkparity.codebase.junitx.TestCase#tearDown()
     *
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /** <b>Title:</b>Payment Test Case Fixture<br> */
    protected static class Fixture {}
}
