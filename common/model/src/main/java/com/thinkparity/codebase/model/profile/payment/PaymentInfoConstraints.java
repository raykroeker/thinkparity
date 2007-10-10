/*
 * Created On:  8-Aug-07 6:16:31 PM
 */
package com.thinkparity.codebase.model.profile.payment;

import java.util.Calendar;

import com.thinkparity.codebase.DateUtil;
import com.thinkparity.codebase.constraint.Constraint;
import com.thinkparity.codebase.constraint.ShortConstraint;
import com.thinkparity.codebase.constraint.StringConstraint;

/**
 * <b>Title:</b>thinkParity Common Model Profile Payment Info Constraints<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class PaymentInfoConstraints {

    /** A singleton instance. */
    private static PaymentInfoConstraints INSTANCE;

    /**
     * Obtain an instance of payment info constraints.
     * 
     * @return A <code>PaymentInfoConstraints</code>.
     */
    public static PaymentInfoConstraints getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new PaymentInfoConstraints();
        }
        return INSTANCE;
    }

    /** The payment info expiry month constraint. */
    private final ShortConstraint cardExpiryMonth;

    /** The payment info expiry year constraint. */
    private final ShortConstraint cardExpiryYear;

    /** The cardholder's name constraint. */
    private final StringConstraint cardHolderName;

    /** The payment info card name constraint. */
    private final Constraint<PaymentInfo.CardName> cardName;

    /** The payment info number constraint. */
    private final StringConstraint cardNumber;

    /**
     * Create PaymentInfoConstraints.
     *
     */
    private PaymentInfoConstraints() {
        super();
        this.cardExpiryMonth = new ShortConstraint();
        this.cardExpiryMonth.setMaxValue((short) 12);
        this.cardExpiryMonth.setMinValue((short) 1);
        this.cardExpiryMonth.setName("Payment info expiry month.");
        this.cardExpiryMonth.setNullable(Boolean.FALSE);

        final Calendar calendar = DateUtil.getInstance();
        this.cardExpiryYear = new ShortConstraint();
        this.cardExpiryYear.setMinValue((short) calendar.get(Calendar.YEAR));
        this.cardExpiryYear.setMaxValue((short) (calendar.get(Calendar.YEAR) + 10));
        this.cardExpiryYear.setName("Payment info expiry year.");
        this.cardExpiryYear.setNullable(Boolean.FALSE);

        this.cardHolderName = new StringConstraint();
        this.cardHolderName.setMaxLength(64);
        this.cardHolderName.setName("Payment info card holder name.");
        this.cardHolderName.setNullable(Boolean.TRUE);

        this.cardName = new Constraint<PaymentInfo.CardName>() {};
        this.cardName.setName("Payment info card name.");
        this.cardName.setNullable(Boolean.FALSE);

        this.cardNumber = new StringConstraint();
        this.cardNumber.setMaxLength(16);
        this.cardNumber.setMinLength(13);
        this.cardNumber.setName("Payment info number.");
        this.cardNumber.setNullable(Boolean.FALSE);
    }

    /**
     * Obtain the payment info expiry month constraint.
     *
     * @return A <code>ShortConstraint</code>.
     */
    public ShortConstraint getCardExpiryMonth() {
        return cardExpiryMonth;
    }

    /**
     * Obtain the payment info expiry year constraint.
     *
     * @return A <code>ShortConstraint</code>.
     */
    public ShortConstraint getCardExpiryYear() {
        return cardExpiryYear;
    }

    /**
     * Obtain the cardholder name constraint.
     *
     * @return A StringConstraint.
     */
    public StringConstraint getCardHolderName() {
        return cardHolderName;
    }

    /**
     * @deprecated use {@link #getCardHolderName()}
     * 
     */
    @Deprecated
    public StringConstraint getCardholderName() {
        return getCardHolderName();
    }

    /**
     * Obtain credit card name.
     *
     * @return A StringConstraint.
     */
    public Constraint<PaymentInfo.CardName> getCardName() {
        return cardName;
    }

    /**
     * Obtain credit card number.
     *
     * @return A StringConstraint.
     */
    public StringConstraint getCardNumber() {
        return cardNumber;
    }
}
