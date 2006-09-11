/*
 * Generated On: Jul 19 06 03:25:41 PM
 */
package com.thinkparity.desdemona.model.profile;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.jivesoftware.messenger.auth.UnauthorizedException;
import org.jivesoftware.messenger.user.UserManager;
import org.jivesoftware.messenger.user.UserProvider;

import org.xmpp.packet.IQ;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.codebase.model.profile.VerificationKey;

import com.thinkparity.desdemona.model.AbstractModelImpl;
import com.thinkparity.desdemona.model.io.sql.ContactSql;
import com.thinkparity.desdemona.model.io.sql.UserSql;
import com.thinkparity.desdemona.model.session.Session;
import com.thinkparity.desdemona.util.smtp.MessageFactory;
import com.thinkparity.desdemona.util.smtp.TransportManager;
import com.thinkparity.desdemona.util.xmpp.IQWriter;


/**
 * <b>Title:</b>thinkParity Profile Model Implementation</br>
 * <b>Description:</b>
 *
 * @author CreateModel.groovy
 * @version 1.1
 */
class ProfileModelImpl extends AbstractModelImpl {

    /** Contact db io. */
    private final ContactSql contactSql;

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
        this.contactSql = new ContactSql();
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
            final User user = getUserModel().read(userId);
    
            final Profile profile = new Profile();
            return inject(profile, user);
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
     * Read all features for a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>List&lt;Feature&gt</code>.
     */
    List<Feature> readFeatures(final JabberId userId) {
        logApiId();
        logVariable("userId", userId);
        try {
            assertIsAuthenticatedUser(userId);
            return userSql.readFeatures(userId);
        } catch (final Throwable t) {
            throw translateError(t);
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
            notifyContactUpdated(userId);
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

    void update(final JabberId userId, final String name,
            final String organization, final String title) {
        logApiId();
        logVariable("userId", userId);
        logVariable("name", name);
        logVariable("organization", organization);
        logVariable("title", title);
        try {
            getUserModel().update(userId, name, organization, title);
            notifyContactUpdated(userId);
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
            // notify all contacts
            notifyContactUpdated(userId);
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

    /**
     * Fire the contact updated event for a user. All contacts for that user
     * will be updated.
     * 
     * @param userId
     *            A user id.
     * @throws SQLException
     * @throws UnauthorizedException
     */
    private void notifyContactUpdated(final JabberId userId)
            throws SQLException, UnauthorizedException {
        // fire notification that the user has been updated
        final List<JabberId> contactIds = contactSql.readIds(userId);
        final IQWriter notification = createIQWriter("contact:contactupdated");
        notification.writeJabberId("contactId", userId);
        notification.writeCalendar("updatedOn", currentDateTime());
        final IQ notificationIQ = notification.getIQ();
        for (final JabberId contactId : contactIds) {
            setTo(notificationIQ, contactId);
            send(contactId, notificationIQ);
        }
    }
}
