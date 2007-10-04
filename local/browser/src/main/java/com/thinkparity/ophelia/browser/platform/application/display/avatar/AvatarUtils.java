/*
 * Created On:  15-Aug-07 9:59:22 AM
 */
package com.thinkparity.ophelia.browser.platform.application.display.avatar;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.codebase.model.profile.payment.PaymentInfo;

/**
 * <b>Title:</b>thinkParity Ophelia UI Avatar Utilities<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class AvatarUtils {

    /**
     * Create AvatarUtils.
     * 
     * @param avatar
     *            The <code>Avatar</code>.
     */
    AvatarUtils(final Avatar avatar) {
        super();
    }

    /**
     * Extract payment info from the controls.
     * 
     * @param expiryMonthJComboBox
     *            An expiry month <code>JComboBox</code>.
     * @param expiryYearJComboBox
     *            An expiry year <code>JComboBox</code>.
     * @param numberJTextField
     *            A number <code>JTextField</code>.
     * @param typeJComboBox
     *            A type <code>JComboBox</code>.
     * @return A <code>PaymentInfo</code>.
     */
    public final PaymentInfo extractPaymentInfo(
            final JComboBox expiryMonthJComboBox,
            final JComboBox expiryYearJComboBox,
            final JTextField numberJTextField, final JComboBox typeJComboBox) {
        final PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setCardExpiryMonth(
                (Short) SwingUtil.extractSelection(expiryMonthJComboBox, null));
        paymentInfo.setCardExpiryYear(
                (Short) SwingUtil.extractSelection(expiryYearJComboBox, null));
        paymentInfo.setCardNumber(SwingUtil.extract(numberJTextField, Boolean.TRUE));
        paymentInfo.setCardName(
                (PaymentInfo.CardName) SwingUtil.extractSelection(typeJComboBox, null));
        return paymentInfo;
    }
}
