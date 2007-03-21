/*
 * Created On: Jul 21, 2006 11:57:35 AM
 */
package com.thinkparity.desdemona.model.migrator;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import com.thinkparity.codebase.Application;

import com.thinkparity.codebase.model.migrator.Error;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.util.http.Link;
import com.thinkparity.codebase.model.util.http.LinkFactory;

/**
 * <b>Title:</b>thinkParity DesdemonaModel Error Text<br>
 * <b>Description:</b>An error message text generator.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class ErrorText {

    /** The <code>ResourceBundle</code>. */
    private final ResourceBundle resourceBundle;

    /** The <code>Error</code>. */
    private final Error error;

    /** The error link factory. */
    private final LinkFactory linkFactory;

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
    ErrorText(final Environment environment, final Locale locale,
            final Error error) {
        super();
        this.error = error;
        this.resourceBundle = ResourceBundle.getBundle(
                "localization.Error_Messages", locale);
        this.linkFactory = LinkFactory.getInstance(Application.DESDEMONA, environment);
    }

    /**
     * Obtain the invitation body.
     * 
     * @param inviter
     *            The inviting user.
     * @return The invitation body.
     */
    public String getBody() {
        final Link viewMirgratorError = linkFactory.create("migrator/error/view");
        viewMirgratorError.addParameter("errorId", error.getId());
        return MessageFormat.format(
                resourceBundle.getString("body"), viewMirgratorError.toString());
    }

    /**
     * Obtain the error body mime type.
     * 
     * @return A string.
     */
    public String getBodyType() {
        return resourceBundle.getString("body.mimetype");
    }

    /**
     * Obtain the invitation subject.
     * 
     * @param inviter
     *            The inviting user.
     * @return The invitation subject.
     */
    public String getSubject() {
        return MessageFormat.format(
                resourceBundle.getString("subject"),
                error.getOccuredOn().getTime());
    }
}
