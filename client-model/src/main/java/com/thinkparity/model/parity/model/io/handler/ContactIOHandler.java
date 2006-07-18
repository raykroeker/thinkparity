/*
 * Created On: Jul 7, 2006 2:18:59 PM
 */
package com.thinkparity.model.parity.model.io.handler;

import java.util.List;

import com.thinkparity.model.parity.model.contact.ContactInvitation;
import com.thinkparity.model.xmpp.contact.Contact;
import com.thinkparity.model.xmpp.user.User;

/**
 * @author raymond@thinkparity.com
 * @version
 */
public interface ContactIOHandler {
    public void create();
    public void createInvitation(final ContactInvitation invitation,
            final User createdBy);
    public void delete();
    public List<Contact> read();
    public Contact read(final String email);
    public ContactInvitation readInvitation(final String email);
    public List<ContactInvitation> readInvitations();
}
