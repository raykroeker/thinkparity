/*
 * Created On:  4-Sep-07 10:28:19 PM
 */
package com.thinkparity.payment.moneris.delegate;

import JavaAPI.Receipt;

import com.thinkparity.desdemona.model.profile.payment.CardDeclinedException;
import com.thinkparity.desdemona.model.profile.payment.CardExpiredException;

import com.thinkparity.payment.moneris.delegate.Constants.ResponseCode;

/**
 * <b>Title:</b>thinkParity Moneris Payment Purchase Delegate<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Purchase extends MonerisDelegate {

    /** The authorization amount. */
    private String amount;

    /** The card expiry date. */
    private String cardExpiryDate;

    /** The card number. */
    private String cardNumber;

    /** A customer id. */
    private String customerId;

    /**
     * Create Purchase.
     *
     */
    public Purchase() {
        super();
    }

    /**
     * @see com.thinkparity.payment.moneris.delegate.MonerisDelegate#invoke()
     *
     */
    @Override
    public void invoke() throws Exception {
        post(newPurchase());
        final Receipt receipt = getReceipt();
        final String responseCode = receipt.getResponseCode();
        if (null == responseCode || "null".equals(responseCode)) {
            if (isInvalidPan(receipt)) {
                throw new CardDeclinedException();
            } else {
                throw panic();
            }
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
     *      A <code>String</code>.
     */
    public void setAmount(final String amount) {
        this.amount = amount;
    }

    /**
     * Set the cardExpiryDate.
     *
     * @param cardExpiryDate
     *      A <code>String</code>.
     */
    public void setCardExpiryDate(final String cardExpiryDate) {
        this.cardExpiryDate = cardExpiryDate;
    }

    /**
     * Set the cardNumber.
     *
     * @param cardNumber
     *      A <code>String</code>.
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
     * Create a moneris purchase.
     * 
     * @return A <code>JavaAPI.Purchase</code>.
     */
    private JavaAPI.Purchase newPurchase() {
        final JavaAPI.Purchase purchase = new JavaAPI.Purchase();
        purchase.setAmount(amount);
        purchase.setCryptType(getEnvironment().getEncryption());
        purchase.setCustId(customerId);
        purchase.setExpDate(cardExpiryDate);
        purchase.setOrderId(getTransaction().getId());
        purchase.setPan(cardNumber);
        return purchase;
    }
}
