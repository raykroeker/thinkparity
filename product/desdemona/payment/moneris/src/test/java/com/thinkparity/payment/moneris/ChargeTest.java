/*
 * Created On:  5-Sep-07 9:35:06 AM
 */
package com.thinkparity.payment.moneris;

import java.util.Calendar;

import com.thinkparity.desdemona.model.profile.payment.CardDeclinedException;
import com.thinkparity.desdemona.model.profile.payment.CardExpiredException;
import com.thinkparity.desdemona.model.profile.payment.PaymentException;
import com.thinkparity.desdemona.model.profile.payment.provider.Client;
import com.thinkparity.desdemona.model.profile.payment.provider.Info;
import com.thinkparity.desdemona.model.profile.payment.provider.Payment;
import com.thinkparity.desdemona.model.profile.payment.provider.Transaction;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ChargeTest extends PaymentTestCase {

    private static final String NAME = "Payment provider charge test";

    /** Test datum. */
    private Fixture datum;

    /**
     * Create ChargeTest.
     *
     */
    public ChargeTest() {
        super(NAME);
    }

    /**
     * Test charging an expired card.
     * 
     */
    public void testChargeExpiredCard() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test charge expired card.");

        final Calendar now = Calendar.getInstance();
        final Calendar startDate = (Calendar) now.clone();
        startDate.set(Calendar.MONTH, now.get(Calendar.MONTH) + 1);

        final Info info = newValidInfo();

        final Transaction transaction = newTransaction();

        final Payment payment = new Payment();
        payment.setAmount(newExpiredCardAmount());

        datum.provider.setClient(datum.client);
        datum.provider.setInfo(info);
        boolean didFailExpired = false;
        try {
            datum.provider.charge(transaction, payment);
        } catch (final CardDeclinedException cdx) {
            fail(cdx, "Could not charge.");
        } catch (final CardExpiredException cex) {
            didFailExpired = true;
        } catch (final PaymentException px) {
            fail(px, "Could not charge.");
        }
        assertTrue(didFailExpired, "Did not fail with expired card.");
    }

    /**
     * Test charging a card with insufficient funds.
     * 
     */
    public void testChargeInsufficientFunds() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test charge insufficient funds.");

        final Calendar now = Calendar.getInstance();
        final Calendar startDate = (Calendar) now.clone();
        startDate.set(Calendar.MONTH, now.get(Calendar.MONTH) + 1);

        final Info info = newValidInfo();

        final Transaction transaction = newTransaction();

        final Payment payment = new Payment();
        payment.setAmount(newInsufficientFundsAmount());

        datum.provider.setClient(datum.client);
        datum.provider.setInfo(info);
        boolean didFailDeclined = false;
        try {
            datum.provider.charge(transaction, payment);
        } catch (final CardDeclinedException cdx) {
            didFailDeclined = true;
        } catch (final CardExpiredException cex) {
            fail(cex, "Could not charge.");
        } catch (final PaymentException px) {
            fail(px, "Could not charge.");
        }
        assertTrue(didFailDeclined, "Did not fail with declined.");
    }

    /**
     * Test charge.
     * 
     */
    public void testCharge() {
        TEST_LOGGER.logTraceId();

        final Calendar now = Calendar.getInstance();
        final Calendar startDate = (Calendar) now.clone();
        startDate.set(Calendar.MONTH, now.get(Calendar.MONTH) + 1);

        final Info info = newValidInfo();

        final Transaction transaction = newTransaction();

        final Payment payment = new Payment();
        payment.setAmount(84L * 100L);

        datum.provider.setClient(datum.client);
        datum.provider.setInfo(info);
        try {
            datum.provider.charge(transaction, payment);
        } catch (final CardDeclinedException cdx) {
            fail(cdx, "Could not charge.");
        } catch (final CardExpiredException cex) {
            fail(cex, "Could not charge.");
        } catch (final PaymentException px) {
            fail(px, "Could not charge.");
        }
    }

    /**
     * Test charge with an invalid number.
     * 
     */
    public void testChargeInvalidCardNumber() {
        TEST_LOGGER.logTraceId();

        final Calendar now = Calendar.getInstance();
        final Calendar startDate = (Calendar) now.clone();
        startDate.set(Calendar.MONTH, now.get(Calendar.MONTH) + 1);

        final Info info = newInvalidCardNumberInfo();

        final Transaction transaction = newTransaction();

        final Payment payment = new Payment();
        payment.setAmount(84L * 100L);

        datum.provider.setClient(datum.client);
        datum.provider.setInfo(info);
        boolean didThrow = false;
        try {
            datum.provider.charge(transaction, payment);
        } catch (final CardDeclinedException cdx) {
            fail(cdx, "Could not charge.");
        } catch (final CardExpiredException cex) {
            fail(cex, "Could not charge.");
        } catch (final PaymentException px) {
            didThrow = true;
        }
        assertTrue(didThrow, "Did not correctly identify invalid card number.");
    }

    /**
     * @see com.thinkparity.payment.moneris.PaymentTestCase#setUp()
     *
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        final Client client = new Client();
        client.setId(getName());

        final PaymentProvider provider = new PaymentProvider();
        provider.configure(newConfiguration());
        datum = new Fixture(provider, client);
    }

    /**
     * @see com.thinkparity.payment.moneris.PaymentTestCase#tearDown()
     *
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /** <b>Title:</b>Schedule Test Fixture<br> */
    private static class Fixture extends PaymentTestCase.Fixture {
        private final Client client;
        private final PaymentProvider provider;
        private Fixture(final PaymentProvider provider, final Client client) {
            super();
            this.client = client;
            this.provider = provider;
        }
    }
}
