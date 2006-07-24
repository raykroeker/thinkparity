/*
 * Created On: Jul 21, 2006 11:57:35 AM
 */
package com.thinkparity.server.model.contact;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import com.thinkparity.codebase.Application;
import com.thinkparity.codebase.http.Link;
import com.thinkparity.codebase.http.LinkFactory;

import com.thinkparity.server.Version;
import com.thinkparity.server.model.user.User;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class InvitationText {

    /** The invitee e-mail address. */
    private final String invitee;

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
    InvitationText(final Locale locale, final String invitee, final User inviter) {
        super();
        this.invitee = invitee;
        this.inviter = inviter;
        this.resourceBundle = ResourceBundle.getBundle(
                "localization.Invitation_Messages", locale);
        this.linkFactory = LinkFactory.getInstance(Application.ROSALINE, Version.getMode());
    }

    /**
     * Obtain the invitation body.
     * 
     * @param inviter
     *            The inviting user.
     * @return The invitation body.
     */
    public String getBody() {
        final Link acceptInvitation = linkFactory.create("Contact", "AcceptInvitation");
        acceptInvitation.addParameter("JabberId", inviter.getId().getQualifiedJabberId());

        final Link createAccount = linkFactory.create("Account", "Create");
        createAccount.addParameter("Email", invitee);
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
