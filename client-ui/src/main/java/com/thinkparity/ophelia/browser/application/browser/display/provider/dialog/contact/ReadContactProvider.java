/**
 * Created On: 3-Jul-2006 1:10:20 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.contact;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.profile.Profile;

import com.thinkparity.ophelia.browser.application.browser.display.provider.SingleContentProvider;
import com.thinkparity.ophelia.model.user.UserModel;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class ReadContactProvider extends SingleContentProvider {
    
    /** A thinkParity user interface. */
    private final UserModel userModel;
    
    /** Create ReadContactProvider. */
    public ReadContactProvider(final Profile profile,
            final UserModel userModel) {
        super(profile);
        this.userModel = userModel;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.provider.SingleContentProvider#getElement(java.lang.Object)
     * 
     */
    public Object getElement(final Object input) {
        Assert.assertNotNull("NULL INPUT", input);
        Assert.assertOfType("INPUT TYPE INCORRECT", JabberId.class, input);
        return userModel.read((JabberId)input);
    }
}
