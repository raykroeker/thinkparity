/*
 * Created On: Aug 27, 2006 1:10:42 PM
 */
package com.thinkparity.model.profile;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import com.thinkparity.codebase.email.EMail;

/** 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class VerificationText {

    /** The resource bundle. */
    private final ResourceBundle resourceBundle;

    /** The email to be verified. */
    private final EMail verificationEmail;

    /** The verification key. */
    private final VerificationKey verificationKey;

    /**
     * Create VerificationText.
     * 
     * @param locale
     *            The invitee locale
     * @param invitee
     *            The invitee e-mail address.
     * @param inviter
     *            The inviter user.
     */
    VerificationText(final Locale locale, final EMail verificationEmail,
            final VerificationKey verificationKey) {
        super();
        this.verificationEmail = verificationEmail;
        this.verificationKey = verificationKey;
        this.resourceBundle = ResourceBundle.getBundle(
                "localization.Verification_Messages", locale);
    }

    /**
     * Obtain the invitation body.
     * 
     * @param inviter
     *            The inviting user.
     * @return The invitation body.
     */
    public String getBody() {
        return MessageFormat.format(
                resourceBundle.getString("body"),
                getSubject(), verificationEmail, verificationKey.getKey());
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
        return resourceBundle.getString("subject");
    }
}
