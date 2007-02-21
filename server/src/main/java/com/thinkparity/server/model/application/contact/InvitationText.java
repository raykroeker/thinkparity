/*
 * Created On: Jul 21, 2006 11:57:35 AM
 */
package com.thinkparity.desdemona.model.contact;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import com.thinkparity.codebase.Application;
import com.thinkparity.codebase.email.EMail;

import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.http.Link;
import com.thinkparity.codebase.model.util.http.LinkFactory;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class InvitationText {

    /** The invitee e-mail address. */
    private final EMail invitee;

    /** The inviting user. */
    private final User inviter;

    /** The invitation link factory. */
    private final LinkFactory linkFactory;

    /** The resource bundle. */
    private final ResourceBundle resourceBundle;

    /**
     * Create InvitationText.
     * 
     * @param locale
     *            The invitee locale
     * @param invitee
     *            The invitee e-mail address.
     * @param inviter
     *            The inviter user.
     */
    InvitationText(final Environment environment, final Locale locale,
            final EMail invitee, final User inviter) {
        super();
        this.invitee = invitee;
        this.inviter = inviter;
        this.resourceBundle = ResourceBundle.getBundle(
                "localization.Invitation_Messages", locale);
        this.linkFactory = LinkFactory.getInstance(Application.ROSALINE, environment);
    }

    /**
     * Obtain the invitation body.
     * 
     * @param inviter
     *            The inviting user.
     * @return The invitation body.
     */
    public String getBody() {
        final Link acceptInvitation = linkFactory.create("invitation/accept");
        acceptInvitation.addParameter("JabberId", inviter.getId().getQualifiedJabberId());

        final Link createAccount = linkFactory.create("user/create");
        createAccount.addParameter("Email", invitee.toString());
        createAccount.addParameter("JabberId", inviter.getId().getQualifiedJabberId());
        createAccount.addParameter("PostCompletion", "AcceptInvitation");

        return MessageFormat.format(
                resourceBundle.getString("body"),
                getSubject(), acceptInvitation, createAccount);
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
                resourceBundle.getString("subject"), inviter.getName());
    }
}
