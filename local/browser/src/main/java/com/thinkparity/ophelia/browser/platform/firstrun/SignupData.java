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

    public enum DataKey { CREDENTIALS, EMAIL, FEATURE_SET, PROFILE, RESERVATION }
}
