/**
 * Created On: 3-Jul-2006 1:10:20 PM
 * $Id$
 */
package com.thinkparity.browser.application.browser.display.provider.contact;

import com.thinkparity.browser.application.browser.display.provider.SingleContentProvider;

import com.thinkparity.model.parity.model.contact.ContactModel;
import com.thinkparity.model.xmpp.JabberId;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class ContactInfoProvider extends SingleContentProvider {
    
    /**
     * The parity contacts api.
     * 
     */
    private final ContactModel contactModel;
    
    /**
     * Create a ContactInfoProvider.
     */
    public ContactInfoProvider(final ContactModel contactModel) {
        super();
        this.contactModel = contactModel;
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.provider.SingleContentProvider#getElement(java.lang.Object)
     * 
     */
    public Object getElement(final Object input) {
        return contactModel.read((JabberId)input);
    }
}
