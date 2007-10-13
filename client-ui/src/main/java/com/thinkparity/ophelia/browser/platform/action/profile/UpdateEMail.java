/*
 * Created On: Aug 28, 2006 8:33:52 AM
 */
package com.thinkparity.ophelia.browser.platform.action.profile;

import com.thinkparity.codebase.email.EMail;

import com.thinkparity.codebase.model.profile.EMailIntegrityException;

import com.thinkparity.ophelia.model.profile.ProfileModel;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractBrowserAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class UpdateEMail extends AbstractBrowserAction {

    /** The browser application. */
    private final Browser browser;

    /**
     * Create UpdateEMail.
     * 
     * @param browser
     *            A <code>Browser</code>.
     */
    public UpdateEMail(final Browser browser) {
        super(ActionId.PROFILE_UPDATE_EMAIL);
        this.browser = browser;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     * 
     */
    @Override
    public void invoke(final Data data) {
        final EMail email = (EMail) data.get(DataKey.EMAIL);

        final ProfileModel profileModel = getProfileModel();
        try {
            profileModel.updateEMail(email);
        } catch (final EMailIntegrityException emix) {
            logger.logWarning("Cannot update e-mail.  {0}", emix.getMessage());
            browser.displayErrorDialog("UpdateEMailIntegrity", new Object[] { email.toString() });
        }
    }

    /** <b>Title:</b>Update E-Mail Data Key<br> */
    public enum DataKey { EMAIL }
}
