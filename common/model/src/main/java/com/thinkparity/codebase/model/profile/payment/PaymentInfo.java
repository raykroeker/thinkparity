/*
 * Created On:  8-Aug-07 3:42:14 PM
 */
package com.thinkparity.codebase.model.profile.payment;

/**
 * <b>Title:</b>thinkParity Common Model Profile Payment Info<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class PaymentInfo {

    /** The expiry month. */
    private Short cardExpiryMonth;

    /** The expiry year. */
    private Short cardExpiryYear;

    /** The card holder's name. */
    private String cardHolderName;

    /** The type. */
    private CardName cardName;

    /** The number. */
    private String cardNumber;

    /** A local payment id. */
    private transient Long localId;

    /**
     * Create ProfilePaymentInfo.
     *
     */
    public PaymentInfo() {
        super();
    }

    /**
     * Obtain the cardExpiryMonth.
     *
     * @return A <code>Short</code>.
     */
    public Short getCardExpiryMonth() {
        return cardExpiryMonth;
    }

    /**
     * Obtain the cardExpiryYear.
     *
     * @return A <code>Short</code>.
     */
    public Short getCardExpiryYear() {
        return cardExpiryYear;
    }

    /**
     * Obtain the cardholderName.
     *
     * @return A <code>String</code>.
     */
    public String getCardHolderName() {
        return cardHolderName;
    }

    /**
     * Obtain the cardName.
     *
     * @return A <code>CardName</code>.
     */
    public CardName getCardName() {
        return cardName;
    }

    /**
     * Obtain the cardNumber.
     *
     * @return A <code>String</code>.
     */
    public String getCardNumber() {
        return cardNumber;
    }

    /**
     * Obtain localId.
     *
     * @return A Long.
     */
    public Long getLocalId() {
        return localId;
    }

    /**
     * Set the cardExpiryMonth.
     *
     * @param cardExpiryMonth
     *		A <code>Short</code>.
     */
    public void setCardExpiryMonth(final Short cardExpiryMonth) {
        this.cardExpiryMonth = cardExpiryMonth;
    }

    /**
     * Set the cardExpiryYear.
     *
     * @param cardExpiryYear
     *		A <code>Short</code>.
     */
    public void setCardExpiryYear(final Short cardExpiryYear) {
        this.cardExpiryYear = cardExpiryYear;
    }

    /**
     * Set the cardholderName.
     *
     * @param cardholderName
     *		A <code>String</code>.
     */
    public void setCardHolderName(final String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    /**
     * Set the cardName.
     *
     * @param cardName
     *		A <code>CardName</code>.
     */
    public void setCardName(final CardName cardName) {
        this.cardName = cardName;
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
     * Set localId.
     *
     * @param localId
     *		A Long.
     */
    public void setLocalId(final Long localId) {
        this.localId = localId;
    }

    /** <b>Title:</b>Payment Info Card Name<br> */
    public enum CardName {
        MASTER_CARD, VISA
    }
}
