/*
 * Created On:  26-Jul-07 4:42:55 PM
 */
package com.thinkparity.desdemona.model.container;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.user.UserNameTokenizer;

/**
 * <b>Title:</b>thinkParity Desdemona Profile Model Welcome Text<br>
 * <b>Description:</b>The welcome container text generator.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class WelcomeText {

    /** The resource bundle. */
    private final ResourceBundle resourceBundle;

    /** The welcome user. */
    private final User user;

    /** The welcome user's names. */
    private final UserNameTokenizer userNameTokenizer;

    /**
     * Create WelcomeText.
     *
     */
    WelcomeText(final Locale locale, final User user) {
        super();
        this.user = user;
        this.userNameTokenizer = new UserNameTokenizer(user.getName());

        this.resourceBundle = ResourceBundle.getBundle(
                "localization.Welcome_Messages", locale);
    }

    /**
     * Obtain the container version artifact name.
     * 
     * @return A <code>String</code>.
     */
    String getContainerVersionArtifactName() {
        return getString("container-name");
    }

    /**
     * Obtain the container version comment.
     * 
     * @return A <code>String</code>.
     */
    String getContainerVersionComment() {
        return getString("container.version-comment");
    }

    /**
     * Obtain the container version name.
     * 
     * @return A <code>String</code>.
     */
    String getContainerVersionName() {
        return null;
    }

    /**
     * Obtain the document version artifact name.
     * 
     * @return A <code>String</code>.
     */
    String getDocumentVersionArtifactName() {
        return getString("document-name");
    }

    /**
     * Obtain the document version comment.
     * 
     * @return A <code>String</code>.
     */
    String getDocumentVersionComment() {
        return null;
    }

    /**
     * Obtain the document version name.
     * 
     * @return A <code>String</code>.
     */
    String getDocumentVersionName() {
        return null;
    }

    /**
     * Obtain a localized string including all welcome text variables.
     * 
     * @param key
     *            A localization key <code>String</code>.
     * @return A <code>String</code>.
     */
    private String getString(final String key) {
        return MessageFormat.format(resourceBundle.getString(key),
                user.getName(), userNameTokenizer.getGiven(),
                userNameTokenizer.getMiddle(), userNameTokenizer.getFamily());
    }
}
