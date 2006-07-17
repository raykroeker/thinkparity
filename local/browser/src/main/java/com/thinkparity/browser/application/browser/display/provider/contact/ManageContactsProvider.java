/*
 * Mar 10, 2006
 */
package com.thinkparity.browser.application.browser.display.provider.contact;

import com.thinkparity.browser.application.browser.display.provider.FlatSingleContentProvider;

import com.thinkparity.model.parity.model.contact.ContactModel;
import com.thinkparity.model.parity.model.profile.Profile;
import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.contact.Contact;

/**
 * Provide a flat list of contacts from the session model.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ManageContactsProvider extends FlatSingleContentProvider {

	/**
	 * The parity session api.
	 * 
	 */
	private final ContactModel contactModel;

	/**
	 * Create a ManageContactsProvider.
	 */
	public ManageContactsProvider(final Profile profile, final ContactModel contactModel) {
		super(profile);
		this.contactModel = contactModel;
	}

    /**
     * @see com.thinkparity.browser.application.browser.display.provider.FlatSingleContentProvider#getElement(java.lang.Object)
     */
    public Object getElement(final Object input) {
        return contactModel.read((JabberId)input);
    }

	/**
	 * @see com.thinkparity.browser.application.browser.display.provider.FlatSingleContentProvider#getElements(java.lang.Object)
	 * 
	 */
	public Object[] getElements(final Object input) {
		return contactModel.read().toArray(new Contact[] {});
	}
}
