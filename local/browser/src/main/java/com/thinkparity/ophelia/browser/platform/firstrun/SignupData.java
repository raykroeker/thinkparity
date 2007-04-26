/**
 * Created On: Apr 3, 2007 6:34:03 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.firstrun;

import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class SignupData extends Data {

    /**
     * Create SignupData.
     */
    public SignupData() {
        super();
    }

    /**
     * <b>Title:</b>Signup Data Key Enumeration<br>
     * <b>Description:</b><br>
     */
    public enum DataKey {
        CREDENTIALS, EMAIL, EMAIL_RESERVATION, EXISTING_ACCOUNT,
        FEATURES, FEATURE_SET, PROFILE, SECURITY_ANSWER, SECURITY_QUESTION,
        USERNAME_RESERVATION
    }
}
