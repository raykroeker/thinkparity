/*
 * Created On: Jul 21, 2006 11:57:35 AM
 */
package com.thinkparity.desdemona.model.contact;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.user.User;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class InvitationText {

    /** The inviting user. */
    private final User invitedBy;

    /** The resource bundle. */
    private final ResourceBundle resourceBundle;

    /**
     * Create InvitationText.
     * 
     * @param locale
     *            The invitee locale
     * @param invitedBy
     *            The invited by user.
     */
    InvitationText(final Environment environment, final Locale locale,
            final User invitedBy) {
        super();
        this.invitedBy = invitedBy;
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
    public String getBody() {
        /* NOCOMMIT - InvitationText#getBody() - Use the migrator to reference
         * the release name */
        final String download = "http://download.thinkparity.net/en/thinkParity-v1_0-BETA-20070409.exe";
        return MessageFormat.format(
                resourceBundle.getString("body"),
                getSubject(), download);
    }

    /**
     * Obtain the invitation body mime type.
     * 
     * @return A string.
     */
    public String getBodyType() { return "text/plain"; }

    /**
     * Obtain the invitation subject.
     * 
     * @param inviter
     *            The inviting user.
     * @return The invitation subject.
     */
    public String getSubject() {
        return MessageFormat.format(
                resourceBundle.getString("subject"), invitedBy.getName());
    }
}
