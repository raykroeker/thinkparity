/**
 * Created On: Apr 3, 2007 6:34:03 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.firstrun;

import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * <b>Title:</b>thinkParity Ophelia UI Platform First Run Data<br>
 * <b>Description:</b>A holder for data keys.<br>
 * 
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
final class SignupData extends Data {

    /**
     * Create SignupData.
     *
     */
    SignupData() {
        super();
    }

    /**
     * <b>Title:</b>Signup Data Key Enumeration<br>
     * <b>Description:</b><br>
     */
    public enum DataKey {
        CREDENTIALS, EMAIL, EMAIL_RESERVATION, EXISTING_ACCOUNT,
        FEATURE_SET, FEATURES, PAYMENT_CREDENTIALS, PAYMENT_INFO, PROFILE,
        SECURITY_ANSWER, SECURITY_QUESTION, USERNAME_RESERVATION
    }
}
