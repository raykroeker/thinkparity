/*
 * Created On: Aug 24, 2006 1:14:41 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.profile;

import java.util.List;

import com.thinkparity.codebase.model.profile.ProfileEMail;

import com.thinkparity.ophelia.model.profile.ProfileModel;

import com.thinkparity.ophelia.browser.application.browser.display.provider.ContentProvider;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class VerifyEMailProvider extends ContentProvider {

    /**
     * Create VerifyEMailProvider.
     * 
     * @param profileModel
     *            An instance of <code>ProfileModel</code>.
     */
    public VerifyEMailProvider(final ProfileModel profileModel) {
        super(profileModel);
    }

    /**
     * Read emails.
     * 
     * @return A list of email addresses.
     */
    public List<ProfileEMail> readEMails() {
        return profileModel.readEmails();
    }
}
