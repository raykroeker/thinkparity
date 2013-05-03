/*
 * Created On:  9-Aug-07 4:00:15 PM
 */
package com.thinkparity.desdemona.model.profile.payment.provider;

/**
 * <b>Title:</b>thinkParity Desdemona Model Profile Payment Provider Info<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Info {

    /** A card expiry month. */
    private Short cardExpiryMonth;

    /** A card expiry year. */
    private Short cardExpiryYear;

    /** A card name. */
    private String cardName;

    /** A card number. */
    private String cardNumber;

    /**
     * Create Info.
     *
     */
    public Info() {
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
     * Obtain the cardName.
     *
     * @return A <code>String</code>.
     */
    public String getCardName() {
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
     * Set the cardName.
     *
     * @param cardName
     *		A <code>String</code>.
     */
    public void setCardName(final String cardName) {
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
}
