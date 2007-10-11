/*
 * Created On:  15-Aug-07 9:13:27 AM
 */
package com.thinkparity.ophelia.browser.platform.action.profile;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.codebase.model.profile.payment.PaymentInfo;

import com.thinkparity.ophelia.model.profile.ProfileModel;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractBrowserAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * <b>Title:</b>thinkParity Update Account Info Action<br>
 * <b>Description:</b>Update the profile's account info; including the features
 * and if applicable payment info.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class UpdatePaymentInfo extends AbstractBrowserAction {

    /** A profile model. */
    private ProfileModel profileModel;

    /** The browser. */
    private final Browser browser;

    /**
     * Create UpdatePaymentInfo.
     * 
     * @param browser
     *            A <code>Browser</code>.
     */
    public UpdatePaymentInfo(final Browser browser) {
        super(ActionId.PROFILE_UPDATE_PAYMENT_INFO);
        this.browser = browser;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     *
     */
    @Override
    protected void invoke(final Data data) {
        profileModel = getProfileModel();
        final PaymentInfo paymentInfo = (PaymentInfo) data.get(DataKey.PAYMENT_INFO);
        if (null == paymentInfo) {
            // the check profile flag indicates the dialog is displayed only if
            // the profile is not active and payment is accessible.
            final Boolean checkProfile = (Boolean) data.get(DataKey.CHECK_PROFILE);
            Boolean showDialog = Boolean.TRUE;
            if (checkProfile &&
                    (profileModel.readIsActive() || !profileModel.isAccessiblePaymentInfo())) {
                showDialog = Boolean.FALSE;
            }
            if (showDialog) {
                if (profileModel.isAccessiblePaymentInfo()) {
                    displayUpdateProfilePaymentInfo();
                } else {
                    Assert.assertUnreachable("Payment info is not accessible.");
                }
            }
        } else {
            profileModel.updatePaymentInfo(paymentInfo);
        }
    }

    /**
     * Display the update payment info dialog.
     */
    private void displayUpdateProfilePaymentInfo() {
        SwingUtil.ensureDispatchThread(new Runnable() {
            public void run() {
                browser.displayUpdateProfilePaymentInfo();
            }
        });
    }

    /** <b>Title:</b>Update Payment Info Data Key<br> */
    public enum DataKey { CHECK_PROFILE, PAYMENT_INFO }
}
