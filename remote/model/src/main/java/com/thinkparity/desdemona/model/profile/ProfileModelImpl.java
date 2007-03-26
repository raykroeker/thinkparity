/*
 * Generated On: Jul 19 06 03:25:41 PM
 */
package com.thinkparity.desdemona.model.profile;

import java.io.StringWriter;
import java.io.Writer;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.thinkparity.codebase.LocaleUtil;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.migrator.Feature;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.ProfileVCard;
import com.thinkparity.codebase.model.profile.VerificationKey;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.user.Token;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.codec.MD5Util;
import com.thinkparity.codebase.model.util.xmpp.event.ContactUpdatedEvent;

import com.thinkparity.desdemona.model.AbstractModelImpl;
import com.thinkparity.desdemona.model.Constants.Product.Ophelia;
import com.thinkparity.desdemona.model.io.sql.ContactSql;
import com.thinkparity.desdemona.model.io.sql.UserSql;
import com.thinkparity.desdemona.model.session.Session;
import com.thinkparity.desdemona.util.smtp.MessageFactory;
import com.thinkparity.desdemona.util.smtp.TransportManager;

import org.jivesoftware.wildfire.auth.UnauthorizedException;

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

    /** User db io. */
    private final UserSql userSql;

    /**
     * Create ProfileModelImpl.
     *
     */
    ProfileModelImpl() {
        this(null);
    }

    /**
     * Create ProfileModelImpl.
     *
     * @param session
     *		The user's session.
     */
    ProfileModelImpl(final Session session) {
        super(session);
        this.contactSql = new ContactSql();
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
     * Create a user's token.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A user's <code>Token</code>.
     */
    Token createToken(final JabberId userId) {
        logApiId();
        logVariable("userId", userId);
        try {
            assertIsAuthenticatedUser(userId);

            final Calendar now = currentDateTime();
            final Token existingToken = userSql.readProfileToken(userId);
            if (null != existingToken) {
                getQueueModel().deleteEvents(userId);
                getArtifactModel().deleteDrafts(userId, now);
            }

            final Token newToken = new Token();
            newToken.setValue(MD5Util.md5Hex(String.valueOf(now.getTimeInMillis())));
            userSql.updateProfileToken(userId, newToken);
            return userSql.readProfileToken(userId);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    Boolean isEmailAvailable(final JabberId userId, final EMail email) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("email", email);
        try {
            assertIsAuthenticatedUser(userId);

            return userSql.isEmailAvailable(email);
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
            profile.setVCard(getUserModel().readVCard(userId, new ProfileVCard()));
            profile.setFeatures(userSql.readFeatures(user.getLocalId(),
                    Ophelia.PRODUCT_ID));
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
        try {
            assertIsAuthenticatedUser(userId);
            final User user = getUserModel().read(userId);

            return userSql.readEmails(user.getLocalId(), Boolean.TRUE);
        } catch (final Throwable t) {
            throw panic(t);
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
            final User user = read(userId);
            return userSql.readFeatures(user.getLocalId(), Ophelia.PRODUCT_ID);
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
     * Read a user's token.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A user's <code>Token</code>.
     */
    Token readToken(final JabberId userId) {
        logApiId();
        logVariable("userId", userId);
        try {
            assertIsAuthenticatedUser(userId);
            return userSql.readProfileToken(userId);
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
            notifyContactUpdated(getUserModel().read(userId));
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
            final User user = userSql.read(userId);
            final Credentials credentials = userSql.readCredentials(user.getLocalId());
            final String newPassword = PasswordGenerator.generate();
            userSql.updatePassword(userId, credentials.getPassword(), newPassword);
            return newPassword;
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    void update(final JabberId userId, final ProfileVCard vcard) {
        try {
            assertIsAuthenticatedUser(userId);
            assertIsValid(vcard);

            final Writer vcardXMLWriter = new StringWriter();
            try {
                getUserModel().updateVCard(userId, vcard);
            } finally {
                vcardXMLWriter.close();
            }
            notifyContactUpdated(getUserModel().read(userId));
        } catch (final Throwable t) {
            throw panic(t);
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
    void updatePassword(final JabberId userId, final String password,
            final String newPassword) {
        try {
            userSql.updatePassword(userId, password, newPassword);
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
            notifyContactUpdated(getUserModel().read(userId));
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Assert that a named value is set.
     * 
     * @param name
     *            A value name <code>String</code>..
     * @param value
     *            A value <code>String</code>.
     */
    private void assertIsSet(final String name, final String value) {
       Assert.assertNotNull(value, "Profile field {0} is not set.", name);
    }

    private void assertIsValid(final ProfileVCard vcard) {
        assertIsSet("country", vcard.getCountry());
        assertIsValidCountry("country", vcard.getCountry());
        assertIsSet("language", vcard.getLanguage());
        assertIsValidLanguage("language", vcard.getLanguage());
        assertIsSet("name", vcard.getName());
        assertIsSet("organization", vcard.getOrganization());
        assertIsSet("organization country", vcard.getOrganizationCountry());
        assertIsValidCountry("organization country", vcard.getOrganizationCountry());
        assertIsSet("timeZone", vcard.getTimeZone());
        assertIsValidTimeZone("timeZone", vcard.getTimeZone());
        assertIsSet("title", vcard.getTitle());
    }

    private void assertIsValidCountry(final String name, final String value) {
        final Locale[] locales = LocaleUtil.getInstance().getAvailableLocales();
        boolean found = false;
        for (final Locale locale : locales) {
            if (locale.getISO3Country().equals(value)) {
                found = true;
                break;
            }
        }
        Assert.assertTrue(found, "Profile field {0} is invalid.", name, value);
    }

    private void assertIsValidLanguage(final String name, final String value) {
        final Locale[] locales = LocaleUtil.getInstance().getAvailableLocales();
        boolean found = false;
        for (final Locale locale : locales) {
            if (locale.getISO3Language().equals(value)) {
                found = true;
                break;
            }
        }
        Assert.assertTrue(found, "Profile field {0} contains invalid value {1}.", name, value);
    }

    private void assertIsValidTimeZone(final String name, final String value) {
        final TimeZone timeZone = TimeZone.getTimeZone(value);
        Assert.assertTrue(timeZone.getID().equals(value), "Profile field {0} contains invalid value {1}.", name, value);
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
    private void notifyContactUpdated(final User user) {
        final List<JabberId> contactIds = contactSql.readIds(user.getLocalId());
        final ContactUpdatedEvent contactUpdated = new ContactUpdatedEvent();
        contactUpdated.setContactId(user.getId());
        contactUpdated.setUpdatedOn(currentDateTime());
        enqueueEvent(user.getId(), contactIds, contactUpdated);
    }
}
