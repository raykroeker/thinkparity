/*
 * Created On:  4-Sep-07 10:11:27 AM
 */
package com.thinkparity.payment.moneris.delegate;

import JavaAPI.Receipt;

import com.thinkparity.desdemona.model.profile.payment.CardDeclinedException;
import com.thinkparity.desdemona.model.profile.payment.CardExpiredException;

import com.thinkparity.payment.moneris.delegate.Constants.ResponseCode;

/**
 * <b>Title:</b>thinkParity Moneris Pre-Authorize Payment Delegate<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class PreAuth extends MonerisDelegate {

    /** The authorization amount. */
    private String amount;

    /** The card expiry date. */
    private String cardExpiryDate;

    /** The card number. */
    private String cardNumber;

    /** A customer id. */
    private String customerId;

    /**
     * Create PreAuth.
     *
     */
    public PreAuth() {
        super();
    }

    /**
     * @see com.thinkparity.payment.moneris.delegate.MonerisDelegate#invoke()
     *
     */
    @Override
    public void invoke() throws Exception {
        post(newPreAuth());
        final Receipt receipt = getReceipt();
        final String responseCode = receipt.getResponseCode();
        if (null == responseCode) {
            throw panic();
        } else {
            final int intResponseCode = Integer.valueOf(responseCode);
            if (ResponseCode.APPROVED_CEILING > intResponseCode) {
            } else {
                if (ResponseCode.EXPIRED == intResponseCode) {
                    throw new CardExpiredException();
                } else {
                    throw new CardDeclinedException();
                }
            }
        }
    }

    /**
     * Set the amount.
     *
     * @param amount
     *		A <code>String</code>.
     */
    public void setAmount(final String amount) {
        this.amount = amount;
    }

    /**
     * Set the cardExpiryDate.
     *
     * @param cardExpiryDate
     *		A <code>String</code>.
     */
    public void setCardExpiryDate(final String cardExpiryDate) {
        this.cardExpiryDate = cardExpiryDate;
    }

    /**
     * Set the cardNumber.
     *
     * @param cardNumber
     *		A <code>String</code>.
     */
    public void setCardNumber(final String cardNumber) {
        this.cardNumber = cardNumber;
    }

    /**
     * Set the customerId.
     *
     * @param customerId
     *		A <code>String</code>.
     */
    public void setCustomerId(final String customerId) {
        this.customerId = customerId;
    }

    /**
     * Create a moneris java api pre auth request.
     * 
     * @return A <code>JavaAPI.PreAuth</code>.
     */
    private JavaAPI.PreAuth newPreAuth() {
        final JavaAPI.PreAuth preAuth = new JavaAPI.PreAuth();
        preAuth.setAmount(amount);
        preAuth.setCryptType(getEnvironment().getEncryption());
        preAuth.setCustId(customerId);
        preAuth.setExpDate(cardExpiryDate);
        preAuth.setOrderId(getTransaction().getId());
        preAuth.setPan(cardNumber);
        return preAuth;
    }
}
