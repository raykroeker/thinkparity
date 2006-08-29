/*
 * Generated On: Jul 19 06 03:25:41 PM
 */
package com.thinkparity.model.profile;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.jivesoftware.messenger.user.UserManager;
import org.jivesoftware.messenger.user.UserProvider;

import org.dom4j.Element;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.model.util.smtp.MessageFactory;
import com.thinkparity.model.util.smtp.TransportManager;

import com.thinkparity.server.ParityServerConstants.VCardFields;
import com.thinkparity.server.model.AbstractModelImpl;
import com.thinkparity.server.model.io.sql.user.UserSql;
import com.thinkparity.server.model.session.Session;
import com.thinkparity.server.model.user.User;

/**
 * <b>Title:</b>thinkParity Profile Model Implementation</br>
 * <b>Description:</b>
 *
 * @author CreateModel.groovy
 * @version 1.1
 */
class ProfileModelImpl extends AbstractModelImpl {

    /** A jive user provider. */
    private final UserProvider userProvider;

    /** User db io. */
    private final UserSql userSql;

    /**
     * Create ProfileModelImpl.
     *
     * @param session
     *		The user's session.
     */
    ProfileModelImpl(final Session session) {
        super(session);
        this.userProvider = UserManager.getUserProvider();
        this.userSql = new UserSql();
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
        logApiId();
        logVariable("userId", userId);
        logVariable("email", email);
        try {
            assertIsAuthenticatedUser(userId);
            // create remote data
            final VerificationKey key = VerificationKey.generate(email);
            userSql.createEmail(userId, email, key);
            // send verification email
            final MimeMessage mimeMessage = MessageFactory.createMimeMessage();
            createVerification(mimeMessage, email, key);
            addRecipient(mimeMessage, email);
            TransportManager.deliver(mimeMessage);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Read a profile.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>Profile</code>.
     */
    Profile read(final JabberId userId) {
        logApiId();
        logVariable("userId", userId);
        try {
            assertIsAuthenticatedUser(userId);
            final User user = getUserModel().readUser(userId);
            final Element vCardElement = user.getVCard();
    
            final Profile profile = new Profile();
            profile.setId(user.getId());
            profile.setName((String) vCardElement.element("FN").getData());
            profile.setOrganization((String) vCardElement.element("ORG").element("ORGNAME").getData());
            final Element titleElement = vCardElement.element(VCardFields.TITLE);
            if (null != titleElement)
                profile.setTitle((String) titleElement.getData());
    
            profile.setVCard(user.getVCard());
            return logVariable("profile", profile);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Read all emails addresses for a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>List&lt;EMail&gt;</code>.
     */
    List<EMail> readEmails(final JabberId userId) {
        logApiId();
        logVariable("userId", userId);
        try {
            assertIsAuthenticatedUser(userId);
            return userSql.readEmails(userId, Boolean.TRUE);
        } catch (final SQLException sqlx) {
            throw translateError(sqlx);
        }
    }

    /**
     * Read a user's security question.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A users's security question <code>String</code>.
     */
    String readSecurityQuestion(final JabberId userId) {
        logApiId();
        logVariable("userId", userId);
        try {
            return userSql.readProfileSecurityQuestion(userId);
        } catch (final Throwable t) {
            throw translateError(t);
        }
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
        logApiId();
        logVariable("userId", userId);
        logVariable("email", email);
        try {
            assertIsAuthenticatedUser(userId);
            // delete remote data
            userSql.deleteEmail(userId, email);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Reset a user's credentials.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param securityAnswer
     *            A security question answer <code>String</code>.
     * @return The user's new password.
     */
    String resetCredentials(final JabberId userId, final String securityAnswer) {
        logApiId();
        logVariable("userId", userId);
        logVariable("securityAnswer", "XXXXX");
        assertIsAuthenticatedUser(userId);
        try {
            final String storedSecurityAnswer =
                userSql.readProfileSecurityAnswer(userId);
            Assert.assertTrue("SECURITY ANSWER DOES NOT MATCH",
                    securityAnswer.equals(storedSecurityAnswer));
            final String password = PasswordGenerator.generate();
            userProvider.setPassword(userId.getUsername(), password);
            return password;
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Verify an email in a user's profile.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param email
     *            An <code>EMail</code>.
     * @param key
     *            A verification key <code>String</code>.
     */
    void verifyEmail(final JabberId userId, final EMail email, final String key) {
        logApiId();
        logVariable("userId", userId);
        logVariable("email", email);
        logVariable("key", key);
        try {
            assertIsAuthenticatedUser(userId);
            final VerificationKey verifiedKey = VerificationKey.generate(email);
            final EMail verifiedEmail = userSql.readEmail(userId, email,
                    verifiedKey);
            Assert.assertNotNull("VERIFICATION KEY INCORRECT", verifiedEmail);
            Assert.assertTrue("VERIFICATION KEY INCORRECT", email.equals(verifiedEmail));
            userSql.verifyEmail(userId, verifiedEmail, verifiedKey);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Create a verification message and attach it to the mime message.
     * 
     * @param mimeMessage
     *            A <code>MimeMessage</code>.
     * @param email
     *            An <code>EMail</code>.
     * @param key
     *            A <code>VerificationKey</code>.
     * @throws MessagingException
     */
    private void createVerification(final MimeMessage mimeMessage,
            final EMail email, final VerificationKey key)
            throws MessagingException {
        final VerificationText text = new VerificationText(Locale.getDefault(), email, key);
        mimeMessage.setSubject(text.getSubject());

        final MimeBodyPart verificationBody = new MimeBodyPart();
        verificationBody.setContent(text.getBody(), text.getBodyType());

        final Multipart verification = new MimeMultipart();
        verification.addBodyPart(verificationBody);
        mimeMessage.setContent(verification);
    }
}
