/*
 * Created On:  8-Aug-07 5:11:54 PM
 */
package com.thinkparity.desdemona.model.profile.payment.provider;

import java.util.Properties;

import com.thinkparity.desdemona.model.profile.payment.CardDeclinedException;
import com.thinkparity.desdemona.model.profile.payment.CardExpiredException;
import com.thinkparity.desdemona.model.profile.payment.PaymentException;

/**
 * <b>Title:</b>thinkParity Desdemona Model Profile Payment Provider<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface PaymentProvider {

    /**
     * Charge a payment.
     * 
     * @param transaction
     *            A <code>Transaction</code>.
     * @param payment
     *            A <code>Payment</code>.
     * @throws CardDeclinedException
     *             the card was declined
     * @throws CardExpiredException
     *             the card has expired
     * @throws PaymentException
     *             the payment system could not process the charge
     */
    void charge(Transaction transaction, Payment payment)
            throws CardDeclinedException, CardExpiredException,
            PaymentException;;

    /**
     * Configure the provider.
     * 
     */
    void configure(Properties properties);

    /**
     * Obtain the provider name.
     * 
     * @return A provider name <code>String</code>.
     */
    String getName();

    /**
     * Determine if the provider cannot process payment.
     * 
     * @return True if the provider is closedb.
     */
    Boolean isClosed();

    /**
     * Determine if the provider can process payment.
     * 
     * @return True if the provider is open.
     */
    Boolean isOpen();

    /**
     * Set the client
     * 
     * @param client
     *            A <code>Client</code>.
     */
    void setClient(Client client);

    /**
     * Set the info.
     * 
     * @param info
     *            An <code>Info</code>.
     */
    void setInfo(Info info);
}
