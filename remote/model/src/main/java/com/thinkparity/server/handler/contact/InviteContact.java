/*
 * Feb 28, 2006
 */
package com.thinkparity.server.handler.contact;

import com.thinkparity.server.ParityServerConstants.Xml;
import com.thinkparity.server.handler.AbstractController;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class InviteContact extends AbstractController {

	protected static StringBuffer getApiId(final String api) {
        return getControllerId("[INVITE]").append(" ").append(api);
    }

    /**
	 * Create a InviteContact.
	 * 
	 */
	public InviteContact() { super("contact:invite"); }

    /**
     * @see com.thinkparity.codebase.controller.AbstractController#service()
     */
    @Override
    public void service() {
        logger.info(getApiId("[SERVICE]"));
        invite(readString(Xml.Contact.EMAIL));
    }

    /**
     * Invite a contact.
     * 
     * @param email
     *            An e-mail address.
     */
    private void invite(final String email) { getContactModel().invite(email); }
}
