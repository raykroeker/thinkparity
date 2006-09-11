/**
 * Created On: 3-Jul-2006 1:10:20 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.contact;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.profile.Profile;


import com.thinkparity.ophelia.browser.application.browser.display.provider.SingleContentProvider;
import com.thinkparity.ophelia.model.contact.ContactModel;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class ReadContactProvider extends SingleContentProvider {
    
    /** A thinkParity contact interface. */
    private final ContactModel contactModel;
    
    /** Create ReadContactProvider. */
    public ReadContactProvider(final Profile profile,
            final ContactModel contactModel) {
        super(profile);
        this.contactModel = contactModel;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.provider.SingleContentProvider#getElement(java.lang.Object)
     * 
     */
    public Object getElement(final Object input) {
        Assert.assertNotNull("NULL INPUT", input);
        Assert.assertOfType("INPUT TYPE INCORRECT", JabberId.class, input);
        return contactModel.read((JabberId)input);
    }
}
