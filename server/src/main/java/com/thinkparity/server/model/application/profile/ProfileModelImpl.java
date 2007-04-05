/*
 * Generated On: Jul 19 06 03:25:41 PM
 */
package com.thinkparity.desdemona.model.profile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
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

import com.thinkparity.codebase.model.contact.IncomingEMailInvitation;
import com.thinkparity.codebase.model.contact.OutgoingEMailInvitation;
import com.thinkparity.codebase.model.migrator.Feature;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.ProfileEMail;
import com.thinkparity.codebase.model.profile.ProfileVCard;
import com.thinkparity.codebase.model.profile.Reservation;
import com.thinkparity.codebase.model.profile.VerificationKey;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.Token;
import com.thinkparity.codebase.model.util.codec.MD5Util;
import com.thinkparity.codebase.model.util.xmpp.event.ContactUpdatedEvent;

import com.thinkparity.desdemona.model.AbstractModelImpl;
import com.thinkparity.desdemona.model.Constants.Product.Ophelia;
import com.thinkparity.desdemona.model.contact.InternalContactModel;
import com.thinkparity.desdemona.model.io.sql.ContactSql;
import com.thinkparity.desdemona.model.io.sql.EMailSql;
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
public final class ProfileModelImpl extends AbstractModelImpl implements
        ProfileModel, InternalProfileModel {

    /** Contact db io. */
    private ContactSql contactSql;

    /** An e-mail address sql interface. */
    private EMailSql emailSql;

    /** User db io. */
    private UserSql userSql;

    /**
     * Create ProfileModelImpl.
     *
     */
    public ProfileModelImpl() {
        super();
    }

    /**
     * @see com.thinkparity.desdemona.model.profile.ProfileModel#addEmail(com.thinkparity.codebase.jabber.JabberId,
     *      com.thinkparity.codebase.email.EMail)
     * 
     */
    public void addEmail(final JabberId userId, final EMail email) {
        try {
            final User user = read(userId);
            // create remote data
            final VerificationKey key = VerificationKey.generate(email);
            userSql.createEmail(user.getLocalId(), email, key);
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
     * @see com.thinkparity.desdemona.model.profile.ProfileModel#create(com.thinkparity.codebase.jabber.JabberId,
     *      com.thinkparity.codebase.model.profile.Reservation,
     *      com.thinkparity.codebase.model.session.Credentials,
     *      com.thinkparity.codebase.model.profile.Profile,
     *      com.thinkparity.codebase.email.EMail, java.lang.String,
     *      java.lang.String)
     * 
     */
    public void create(final JabberId userId, final Reservation reservation,
            final Credentials credentials, final Profile profile,
            final EMail email, final String securityQuestion,
            final String securityAnswer) {
        try {
            assertIsValid(profile.getVCard());

            // delete expired reservations
            userSql.deleteExpiredReservations(currentDateTime());
            Assert.assertTrue(userSql.doesExistReservation(
                    reservation.getToken()), "Reservation {0} expired on {1}.",
                    reservation.getToken(), reservation.getExpiresOn());

            final File tempVCardFile = session.createTempFile();
            try {
                final FileWriter tempFileWriter = new FileWriter(tempVCardFile);
                try {
                    XSTREAM_UTIL.toXML(profile.getVCard(), tempFileWriter);
                    final InputStream vcardStream = new FileInputStream(tempVCardFile);
                    try {
                        profile.setLocalId(userSql.create(credentials,
                                securityQuestion, securityAnswer, vcardStream,
                                tempVCardFile.length(), getDefaultBufferSize()));
                    } finally {
                        vcardStream.close();
                    }
                } finally {
                    tempFileWriter.close();
                }
            } finally {
                tempVCardFile.delete();
            }

            // add e-mail address
            final VerificationKey key = VerificationKey.generate(email);
            userSql.createEmail(profile.getLocalId(), email, key);

            // add features
            for (final Feature feature : profile.getFeatures())
                userSql.createFeature(profile, feature);

            // remove username reservation
            userSql.deleteReservation(reservation.getToken());

            // send verification email
            final MimeMessage mimeMessage = MessageFactory.createMimeMessage();
            createVerification(mimeMessage, email, key);
            addRecipient(mimeMessage, email);
            TransportManager.deliver(mimeMessage);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.profile.ProfileModel#createReservation(com.thinkparity.codebase.jabber.JabberId,
     *      java.lang.String, java.util.Calendar)
     * 
     */
    public Reservation createReservation(final JabberId userId,
            final String username, final Calendar reservedOn) {
        try {
            userSql.deleteExpiredReservations(currentDateTime());

            // expire in an hour
            final Calendar expiresOn = (Calendar) reservedOn.clone();
            expiresOn.set(Calendar.HOUR, expiresOn.get(Calendar.HOUR) + 1);

            final Reservation reservation = new Reservation();
            reservation.setCreatedOn(reservedOn);
            reservation.setExpiresOn(expiresOn);
            reservation.setToken(newToken());
            reservation.setUsername(username);
            try {
                userSql.createReservation(reservation);
                return reservation;
            } catch (final Throwable t) {
                /* NOTE probably not the best way to indicate that a reservation
                 * cannot be made */
                return null;
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.profile.ProfileModel#createToken(com.thinkparity.codebase.jabber.JabberId)
     * 
     */
    public Token createToken(final JabberId userId) {
        try {
            final Calendar now = currentDateTime();
            final Token existingToken = userSql.readProfileToken(userId);
            if (null != existingToken) {
                getQueueModel().deleteEvents(userId);
                getArtifactModel().deleteDrafts(userId, now);
            }

            final Token newToken = newToken();
            userSql.updateProfileToken(userId, newToken);
            return userSql.readProfileToken(userId);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.profile.ProfileModel#isEmailAvailable(com.thinkparity.codebase.jabber.JabberId,
     *      com.thinkparity.codebase.email.EMail)
     * 
     */
    public Boolean isEmailAvailable(final JabberId userId, final EMail email) {
        try {
            return !emailSql.doesExist(email).booleanValue();
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.profile.ProfileModel#read(com.thinkparity.codebase.jabber.JabberId)
     * 
     */
    public Profile read(final JabberId userId) {
        try {
            final User user = getUserModel().read(userId);

            final Profile profile = new Profile();
            profile.setVCard(getUserModel().readVCard(user.getLocalId(), new ProfileVCard()));
            profile.setFeatures(userSql.readFeatures(user.getLocalId(),
                    Ophelia.PRODUCT_ID));
            return inject(profile, user);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.profile.ProfileModel#readEMails(com.thinkparity.codebase.jabber.JabberId)
     * 
     */
    public List<ProfileEMail> readEMails(final JabberId userId) {
        try {
            final User user = getUserModel().read(userId);
            return userSql.readEMails(user);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.profile.InternalProfileModel#readEMails(com.thinkparity.codebase.jabber.JabberId, com.thinkparity.codebase.model.user.User)
     *
     */
    public List<EMail> readEMails(final JabberId userId, final User user) {
        try {
            // read only verified e-mails
            final List<ProfileEMail> profileEMails = userSql.readEMails(user);
            final List<EMail> emails = new ArrayList<EMail>(profileEMails.size());
            for (final ProfileEMail profileEMail : profileEMails) {
                if (profileEMail.isVerified())
                    emails.add(profileEMail.getEmail());
            }
            return emails;
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    
    /**
     * @see com.thinkparity.desdemona.model.profile.ProfileModel#readFeatures(com.thinkparity.codebase.jabber.JabberId)
     * 
     */
    public List<Feature> readFeatures(final JabberId userId) {
        try {
            final User user = read(userId);
            return userSql.readFeatures(user.getLocalId(), Ophelia.PRODUCT_ID);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.profile.ProfileModel#readSecurityQuestion(com.thinkparity.codebase.jabber.JabberId)
     * 
     */
    public String readSecurityQuestion(final JabberId userId) {
        try {
            return userSql.readProfileSecurityQuestion(userId);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.profile.ProfileModel#readToken(com.thinkparity.codebase.jabber.JabberId)
     * 
     */
    public Token readToken(final JabberId userId) {
        try {
            return userSql.readProfileToken(userId);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.profile.ProfileModel#removeEmail(com.thinkparity.codebase.jabber.JabberId,
     *      com.thinkparity.codebase.email.EMail)
     * 
     */
    public void removeEmail(final JabberId userId, final EMail email) {
        try {
            // delete remote data
            final Profile profile = read(userId);
            userSql.deleteEmail(profile.getLocalId(), email);
            notifyContactUpdated(getUserModel().read(userId));
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.profile.ProfileModel#resetPassword(com.thinkparity.codebase.jabber.JabberId,
     *      java.lang.String)
     * 
     */
    public String resetPassword(final JabberId userId,
            final String securityAnswer) {
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

    /**
     * @see com.thinkparity.desdemona.model.profile.ProfileModel#update(com.thinkparity.codebase.jabber.JabberId,
     *      com.thinkparity.codebase.model.profile.ProfileVCard)
     * 
     */
    public void update(final JabberId userId, final ProfileVCard vcard) {
        try {
            assertIsValid(vcard);

            final Profile profile = read(userId);
            getUserModel().updateVCard(profile.getLocalId(), vcard);
            notifyContactUpdated(getUserModel().read(userId));
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.profile.ProfileModel#updatePassword(com.thinkparity.codebase.jabber.JabberId,
     *      java.lang.String, java.lang.String)
     * 
     */
    public void updatePassword(final JabberId userId, final String password,
            final String newPassword) {
        try {
            userSql.updatePassword(userId, password, newPassword);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.profile.ProfileModel#verifyEmail(com.thinkparity.codebase.jabber.JabberId,
     *      com.thinkparity.codebase.email.EMail, java.lang.String)
     * 
     */
    public void verifyEmail(final JabberId userId, final EMail email, final String key) {
        try {
            final Profile profile = read(userId);

            final VerificationKey verifiedKey = VerificationKey.generate(email);
            final EMail verifiedEmail = userSql.readEmail(profile.getLocalId(),
                    email, verifiedKey);
            Assert.assertNotNull("VERIFICATION KEY INCORRECT", verifiedEmail);
            Assert.assertTrue("VERIFICATION KEY INCORRECT", email.equals(verifiedEmail));
            userSql.verifyEmail(profile.getLocalId(), verifiedEmail, verifiedKey);
            // create invitations
            final InternalContactModel contactModel = getContactModel();
            final List<OutgoingEMailInvitation> invitations =
                contactModel.readOutgoingEMailInvitations(userId, email);
            IncomingEMailInvitation incomingInvitation;
            for (final OutgoingEMailInvitation invitation : invitations) {
                incomingInvitation = new IncomingEMailInvitation();
                incomingInvitation.setCreatedBy(invitation.getCreatedBy());
                incomingInvitation.setCreatedOn(invitation.getCreatedOn());
                incomingInvitation.setExtendedBy(incomingInvitation.getCreatedBy());
                incomingInvitation.setInvitationEMail(email);
                contactModel.createInvitation(userId,
                        invitation.getCreatedBy().getId(), incomingInvitation);
            }
            // fire event
            notifyContactUpdated(getUserModel().read(userId));
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.AbstractModelImpl#initializeModel(com.thinkparity.desdemona.model.session.Session)
     *
     */
    @Override
    protected void initializeModel(final Session session) {
        this.contactSql = new ContactSql();
        this.emailSql = new EMailSql();
        this.userSql = new UserSql();
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
     * Create a new token.
     * 
     * @return A <code>Token</code>.
     */
    private Token newToken() {
        final Token token = new Token();
        token.setValue(MD5Util.md5Hex(String.valueOf(
                currentDateTime().getTimeInMillis())));
        return token;
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
