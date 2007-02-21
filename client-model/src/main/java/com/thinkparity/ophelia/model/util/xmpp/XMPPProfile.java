/*
 * Created On: Jul 6, 2006 11:51:59 AM
 */
package com.thinkparity.ophelia.model.util.xmpp;

import java.util.List;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.user.Token;

import com.thinkparity.ophelia.model.io.xmpp.XMPPMethod;
import com.thinkparity.ophelia.model.io.xmpp.XMPPMethodResponse;
import com.thinkparity.ophelia.model.util.smack.SmackException;
import com.thinkparity.ophelia.model.util.xmpp.event.ProfileListener;

/**
 * <b>Title:</b>thinkParity XMPP Profile<br>
 * <b>Description:</b>The profile remote interface implemenation. Handles all
 * remote method innvocations to the thinkParity server for the profile
 * component. Also handles the remote events generated for the profile.
 * 
 * @author raymond@thinkparity.com
 * @version
 * @see XMPPCore
 */
final class XMPPProfile extends AbstractXMPP<ProfileListener> {

    /**
     * Create XMPPProfile.
     * 
     */
    XMPPProfile(final XMPPCore xmppCore) {
        super(xmppCore);
    }

    /**
     * Add an email to a user's profile.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param email
     *            An <code>EMail</code>.
     */
    void addEmail(final JabberId userId, final EMail email) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("email", email);
        assertIsAuthenticatedUser(userId);
        final XMPPMethod addEmail = new XMPPMethod("profile:addemail");
        addEmail.setParameter("userId", userId);
        addEmail.setParameter("email", email);
        execute(addEmail);
    }

    /**
     * Create the profile's token.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>Token</code>.
     */
    Token createToken(final JabberId userId) {
        final XMPPMethod createToken = new XMPPMethod("profile:createtoken");
        createToken.setParameter("userId", userId);
        return execute(createToken, Boolean.TRUE).readResultToken("token");
    }

    /**
     * Determine the availability of an e-mail address.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param email
     *            An <code>EMail</code> address.
     * @return True if the email is not being used.
     */
    Boolean isEmailAvailable(final JabberId userId, final EMail email) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("email", email);
        final XMPPMethod isEmailAvailable = new XMPPMethod("profile:isemailavailable");
        isEmailAvailable.setParameter("userId", userId);
        isEmailAvailable.setParameter("email", email);
        return execute(isEmailAvailable, Boolean.TRUE).readResultBoolean("isAvailable");
    }

    /**
     * Read a profile.
     * 
     * @return A profile.
     */
    Profile read(final JabberId userId) throws SmackException {
        logger.logApiId();
        logger.logVariable("userId", userId);
        assertIsAuthenticatedUser(userId);
        final XMPPMethod read = new XMPPMethod("profile:read");
        read.setParameter("userId", userId);
        final XMPPMethodResponse response = execute(read, Boolean.TRUE);
        final Profile profile = new Profile();
        profile.setId(response.readResultJabberId("id"));
        profile.setVCard(response.readResultProfileVCard("vcard"));
        profile.setName(response.readResultString("name"));
        profile.setOrganization(response.readResultString("organization"));
        profile.setTitle(response.readResultString("title"));
        return profile;
    }

    List<EMail> readEMails(final JabberId userId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        assertIsAuthenticatedUser(userId);
        final XMPPMethod readEMails = new XMPPMethod("profile:reademails");
        readEMails.setParameter("userId", userId);
        final XMPPMethodResponse response = execute(readEMails, Boolean.TRUE);
        return response.readResultEMails("emails");
    }

    /**
     * Read the user profile's security question.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A security question <code>String</code>.
     */
    String readSecurityQuestion(final JabberId userId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        assertIsAuthenticatedUser(userId);
        final XMPPMethod resetCredentials = new XMPPMethod("profile:readsecurityquestion");
        resetCredentials.setParameter("userId", userId);
        final XMPPMethodResponse response = execute(resetCredentials, Boolean.TRUE);
        return response.readResultString("securityQuestion");
    }

    /**
     * Read the profile's token.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>Token</code>.
     */
    Token readToken(final JabberId userId) {
        final XMPPMethod readToken = new XMPPMethod("profile:readtoken");
        readToken.setParameter("userId", userId);
        return execute(readToken, Boolean.TRUE).readResultToken("token");
        
    }

    /**
     * Remove an email from a user's profile.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param email
     *            An <code>EMail</code>.
     */
    void removeEmail(final JabberId userId, final EMail email) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("email", email);
        assertIsAuthenticatedUser(userId);
        final XMPPMethod addEmail = new XMPPMethod("profile:removeemail");
        addEmail.setParameter("userId", userId);
        addEmail.setParameter("email", email);
        execute(addEmail);
    }

    /**
     * Reset the user's password.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     */
    String resetPassword(final JabberId userId, final String securityAnswer) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("securityAnswer", "XXXXX");
        assertIsAuthenticatedUser(userId);
        final XMPPMethod resetCredentials = new XMPPMethod("profile:resetpassword");
        resetCredentials.setParameter("userId", userId);
        resetCredentials.setParameter("securityAnswer", securityAnswer);
        final XMPPMethodResponse response = execute(resetCredentials, Boolean.TRUE);
        return response.readResultString("password");
    }

    /**
     * Update the profile.
     * 
     * @param userId
     *            The user id <code>JabberId</code>.
     * @param profile
     *            The user's <code>Profile</code>.
     */
    void update(final JabberId userId, final Profile profile) {
        logger.logApiId();
        logger.logVariable("profile", profile);
        assertIsAuthenticatedUser(userId);

        final XMPPMethod update = new XMPPMethod("profile:update");
        update.setParameter("userId", userId);
        update.setParameter("name", profile.getName());
        update.setParameter("organization", profile.getOrganization());
        update.setParameter("title", profile.getTitle());
        update.setParameter("vcard", profile.getVCard());
        execute(update);
    }

    /**
     * Verify an email in a user's profile.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param email
     *            An <code>EMail</code>.
     * @param key
     *            An email verification key <code>String</code>.
     */
    void verifyEmail(final JabberId userId, final EMail email, final String key) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("email", email);
        logger.logVariable("key", key);
        assertIsAuthenticatedUser(userId);
        final XMPPMethod verifyEmail = new XMPPMethod("profile:verifyemail");
        verifyEmail.setParameter("userId", userId);
        verifyEmail.setParameter("email", email);
        verifyEmail.setParameter("key", key);
        execute(verifyEmail);
    }
}
