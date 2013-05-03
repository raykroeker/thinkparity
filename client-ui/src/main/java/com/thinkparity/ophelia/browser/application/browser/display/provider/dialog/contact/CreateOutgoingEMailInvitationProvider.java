/**
 * Created On: 14-Nov-07 2:15:05 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.contact;

import java.util.ArrayList;
import java.util.List;

import com.thinkparity.codebase.email.EMail;

import com.thinkparity.ophelia.model.contact.ContactModel;
import com.thinkparity.ophelia.model.profile.ProfileModel;

import com.thinkparity.ophelia.browser.application.browser.display.provider.ContentProvider;

/**
 * @author robert@thinkparity.com
 * @version $Revision$
 */
public class CreateOutgoingEMailInvitationProvider extends ContentProvider {

    /** A thinkParity contact interface. */
    private final ContactModel contactModel;

    /** Create CreateOutgoingEMailInvitationProvider. */
    public CreateOutgoingEMailInvitationProvider(final ProfileModel profileModel,
            final ContactModel contactModel) {
        super(profileModel);
        this.contactModel = contactModel;
    }

    /**
     * Determine if the specified email can be invited by this user.
     * 
     * @param email
     *            An <code>EMail</code>.
     * @return True if the specified email can be invited by this user.
     */
    public Boolean readIsInviteRestricted(final EMail email) {
        final List<EMail> emailList = new ArrayList<EMail>();
        emailList.add(email);
        return contactModel.isInviteRestricted(emailList);
    }
}
