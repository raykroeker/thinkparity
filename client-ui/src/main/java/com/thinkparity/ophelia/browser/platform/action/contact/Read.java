/**
 * Created On: 27-Jun-2006 2:25:50 PM
 */
package com.thinkparity.ophelia.browser.platform.action.contact;

import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.codebase.model.contact.Contact;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractBrowserAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * <b>Title:</b>thinkParity Ophelia UI Contact Read<br>
 * <b>Description:</b>Read the contact and display within an avatar.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Read extends AbstractBrowserAction {

    /** The browser application. */
    private final Browser browser;

    /**
     * Create Read.
     * 
     * @param browser
     *            A <code>Browser</code>.
     */
    public Read(final Browser browser) {
        super(ActionId.CONTACT_READ);
        this.browser = browser;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     * 
     */
    public void invoke(final Data data) {
        final Long contactId = (Long) data.get(DataKey.CONTACT_ID);
        final Contact contact = getContactModel().read(contactId);
        if (null == contact) {
            logger.logInfo("Contact no longer exists.");
        } else {
            SwingUtil.ensureDispatchThread(new Runnable() {
                /**
                 * @see java.lang.Runnable#run()
                 *
                 */
                @Override
                public void run() {
                    browser.displayUserInfoAvatar(contact);
                }
            });
        }
    }

    /**
     * The key used to set\get the data.
     * 
     * @see Data
     */
    public enum DataKey { CONTACT_ID }
}
