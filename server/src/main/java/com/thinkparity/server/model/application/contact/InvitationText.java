/*
 * Created On: Jul 21, 2006 11:57:35 AM
 */
package com.thinkparity.desdemona.model.contact;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.user.UserNameTokenizer;

/**
 * <b>Title:</b>thinkParity Desdemona Model Contact Outgoing E-Mail Invitation
 * Text<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class InvitationText {

    /** The contact us link. */
    private static final String WWW_CONTACT_US_LINK;

    /** The download link. */
    private static final String WWW_DOWNLOAD_LINK;

    /** The link. */
    private static final String WWW_LINK;

    static {
        WWW_LINK = "http://www.thinkparity.com";
        WWW_DOWNLOAD_LINK = "http://www.thinkparity.com/download";
        WWW_CONTACT_US_LINK = "http://www.thinkparity.com/contactus";
    }

    /** The invitation attachment. */
    private final ContainerVersion attachment;

    /** The created by user's name tokenizer. */
    private final UserNameTokenizer invitedByNameTokenizer;

    /** The resource bundle. */
    private final ResourceBundle resourceBundle;

    /**
     * Create InvitationText.
     * 
     * @param invitedBy
     *            A <code>User</code>.
     * @param attachment
     *            A <code>ContainerVersion</code>.
     */
    InvitationText(final Locale locale, final User invitedBy,
            final ContainerVersion attachment) {
        super();
        this.attachment = attachment;
        this.invitedByNameTokenizer = new UserNameTokenizer(invitedBy);

        this.resourceBundle = ResourceBundle.getBundle(
                "localization.Invitation_Messages", locale);
    }

    /**
     * Obtain the invitation body.
     * 
     * @param inviter
     *            The inviting user.
     * @return The invitation body.
     */
    String getBody() {
        if (null == attachment.getName()) {
            return getString("body.no-version");
        } else {
            return getString("body");
        }
    }

    /**
     * Obtain the invitation body mime type.
     * 
     * @return A string.
     */
    String getBodyType() {
        return "text/plain";
    }

    /**
     * Obtain the personal portion (name) of the from address.
     * 
     * @return A from personal <code>String</code>.
     */
    String getFromPersonal() {
        return getString("from");
    }

    /**
     * Obtain the invitation subject.
     * 
     * @return A <code>String</code>.
     */
    String getSubject() {
        if (null == attachment.getName()) {
            return getString("subject.no-version");
        } else {
            return getString("subject");
        }
    }

    /**
     * Obtain a localized formatted string.
     * 
     * @param key
     *            A localization key.
     * @return A <code>String</code>.
     */
    private String getString(final String key) {
        return MessageFormat.format(resourceBundle.getString(key),
                WWW_LINK, WWW_CONTACT_US_LINK, WWW_DOWNLOAD_LINK,
                invitedByNameTokenizer.getName(),
                invitedByNameTokenizer.getGiven(),
                invitedByNameTokenizer.isSetFamily()
                        ? invitedByNameTokenizer.getGivenAndFamily("{0} {1}")
                                : invitedByNameTokenizer.getGiven(),
                attachment.getArtifactName(),
                attachment.getName());
    }
}
