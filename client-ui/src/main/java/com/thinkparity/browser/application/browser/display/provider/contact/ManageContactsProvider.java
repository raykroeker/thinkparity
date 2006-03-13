/*
 * Mar 10, 2006
 */
package com.thinkparity.browser.application.browser.display.provider.contact;

import com.thinkparity.browser.application.browser.display.provider.FlatContentProvider;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.session.SessionModel;
import com.thinkparity.model.xmpp.contact.Contact;

/**
 * Provide a flat list of contacts from the session model.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ManageContactsProvider extends FlatContentProvider {

	/**
	 * The parity session api.
	 * 
	 */
	private final SessionModel sessionModel;

	/**
	 * Create a ManageContactsProvider.
	 */
	public ManageContactsProvider(final SessionModel sessionModel) {
		super();
		this.sessionModel = sessionModel;
	}

	/**
	 * @see com.thinkparity.browser.application.browser.display.provider.FlatContentProvider#getElements(java.lang.Object)
	 * 
	 */
	public Object[] getElements(final Object input) {
		try { return sessionModel.readContacts().toArray(new Contact[] {}); }
		catch(final ParityException px) { throw new RuntimeException(px); }
	}
}
