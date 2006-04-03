/*
 * Apr 2, 2006
 */
package com.thinkparity.browser.platform.action.artifact;

import java.util.LinkedList;
import java.util.List;

import javax.swing.Icon;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;

import com.thinkparity.model.xmpp.contact.Contact;
import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SendVersion extends AbstractAction {

    private static final Icon ICON;

    private static final ActionId ID;

    private static final String NAME;

    static {
        ICON = null;
        ID = ActionId.ARTIFACT_SEND_VERSION;
        NAME = "Send Version";
    }
        
    /**
     * The browser application.
     * 
     */
    private final Browser browser;

    /**
     * Create a SendVersion.
     * 
     */
    public SendVersion(final Browser browser) {
        super("Send Version", ID, NAME, ICON);
        this.browser = browser;
    }

    /**
     * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
     * 
     */
    public void invoke(final Data data) throws Exception {
        final List<Contact> contacts = (List<Contact>) data.get(DataKey.CONTACTS);
        final Long artifactId = (Long) data.get(DataKey.ARTIFACT_ID);
        final Long versionId = (Long) data.get(DataKey.VERSION_ID);

        getSessionModel().send(proxy(contacts), artifactId, versionId);
    }

    /**
     * Proxy a contact list to a user list.
     * 
     * @param i
     *            The contact list.
     * @return The user list.
     * @deprecated
     */
    private List<User> proxy(final Iterable<Contact> i) {
        final List<User> l = new LinkedList<User>();
        for(final Contact c : i ) { l.add(c); }
        return l;
    }

    public enum DataKey { ARTIFACT_ID, CONTACTS, VERSION_ID }
}
