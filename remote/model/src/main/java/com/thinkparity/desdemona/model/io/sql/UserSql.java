/*
 * Created On: Jul 20, 2006 2:43:22 PM
 */
package com.thinkparity.desdemona.model.io.sql;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.email.EMailBuilder;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.jabber.JabberIdBuilder;

import com.thinkparity.codebase.model.migrator.Feature;
import com.thinkparity.codebase.model.profile.ProfileEMail;
import com.thinkparity.codebase.model.profile.Reservation;
import com.thinkparity.codebase.model.profile.VerificationKey;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.Token;

import com.thinkparity.desdemona.model.io.hsqldb.HypersonicException;
import com.thinkparity.desdemona.model.io.hsqldb.HypersonicSession;
import com.thinkparity.desdemona.model.user.VCardOpener;

/**
 * <b>Title:</b>thinkParity DesdemonaModel User SQL<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.17
 */
public class UserSql extends AbstractSql {

    /** Sql to create a user. */
    private static final String SQL_CREATE =
        new StringBuilder("insert into TPSD_USER ")
        .append("(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,DISABLED,VCARD) ")
        .append("values (?,?,?,?,?,?)")
        .toString();

    /** Sql to create an email address. */
    private static final String SQL_CREATE_EMAIL_REL =
        new StringBuilder("insert into TPSD_USER_EMAIL ")
        .append("(USER_ID,EMAIL_ID,VERIFIED,VERIFICATION_KEY) ")
        .append("values (?,?,?,?)")
        .toString();

    /** Sql to create a user feature relationship. */
    private static final String SQL_CREATE_FEATURE_REL =
        new StringBuilder("insert into TPSD_USER_FEATURE_REL ")
        .append("(USER_ID,FEATURE_ID) ")
        .append("values (?,?)")
        .toString();

    /** Sql to create a reservation. */
    private static final String SQL_CREATE_RESERVATION =
        new StringBuilder("insert into TPSD_USER_RESERVATION ")
        .append("(USERNAME,TOKEN,EXPIRES_ON,CREATED_ON) ")
        .append("values (?,?,?,?)")
        .toString();

    /** Sql to delete an email address. */
    private static final String SQL_DELETE_EMAIL_REL =
        new StringBuilder("delete from TPSD_USER_EMAIL ")
        .append("where USER_ID=? and EMAIL_ID=?")
        .toString();

    /** Sql to delete expired reservations. */
    private static final String SQL_DELETE_EXPIRED_RESERVATIONS =
        new StringBuilder("delete from TPSD_USER_RESERVATION ")
        .append("where EXPIRES_ON < ?")
        .toString();

    /** Sql to delete a reservation by its unique key. */
    private static final String SQL_DELETE_RESERVATION_UK =
        new StringBuilder("delete from TPSD_USER_RESERVATION ")
        .append("where TOKEN=?")
        .toString();

    /** Sql to determine reservation existence by unique key. */
    private static final String SQL_DOES_EXIST_RESERVATION_UK =
        new StringBuilder("select count(UR.TOKEN) \"RESERVATION_COUNT\" ")
        .append("from TPSD_USER_RESERVATION UR ")
        .append("where UR.TOKEN=?")
        .toString();

    /** Sql to read all users. */
    private static final String SQL_READ =
        new StringBuilder("select U.USERNAME,U.USER_ID ")
        .append("from TPSD_USER U ")
        .append("order by U.USERNAME asc")
        .toString();

    /** Sql to read a username from an e-mail address. */
    private static final String SQL_READ_BY_EMAIL =
        new StringBuilder("select U.USERNAME,U.USER_ID ")
        .append("from TPSD_USER U ")
        .append("inner join TPSD_USER_EMAIL UE on UE.USER_ID=U.USER_ID ")
        .append("inner join TPSD_EMAIL E on E.EMAIL_ID=UE.EMAIL_ID ")
        .append("where E.EMAIL=? and UE.VERIFIED=?")
        .toString();

    /** Sql to read a user. */
    private static final String SQL_READ_BY_ID =
        new StringBuilder("select U.USERNAME,U.USER_ID ")
        .append("from TPSD_USER U ")
        .append("where U.USER_ID=?")
        .toString();

    /** Sql to read a user. */
    private static final String SQL_READ_BY_USERNAME =
        new StringBuilder("select U.USERNAME,U.USER_ID ")
        .append("from TPSD_USER U ")
        .append("where U.USERNAME=?")
        .toString();

    /** Sql to read the user credentials. */
    private static final String SQL_READ_CREDENTIALS =
        new StringBuilder("select U.USERNAME,U.PASSWORD ")
        .append("from TPSD_USER U ")
        .append("where U.USER_ID=? ")
        .toString();

    /** Sql to read email addresses. */
    private static final String SQL_READ_EMAIL_UK =
        new StringBuilder("select E.EMAIL ")
        .append("from TPSD_EMAIL E ")
        .append("inner join TPSD_USER_EMAIL UE on UE.EMAIL_ID=E.EMAIL_ID ")
        .append("where UE.USER_ID=? and E.EMAIL=? ")
        .append("and UE.VERIFICATION_KEY=?")
        .toString();

    /** Sql to read e-mail addresses. */
    private static final String SQL_READ_EMAILS =
        new StringBuilder("select E.EMAIL,UE.EMAIL_ID,UE.USER_ID,UE.VERIFIED ")
        .append("from TPSD_USER U ")
        .append("inner join TPSD_USER_EMAIL UE on U.USER_ID=UE.USER_ID ")
        .append("inner join TPSD_EMAIL E on E.EMAIL_ID=UE.EMAIL_ID ")
        .append("where U.USER_ID=?")
        .toString();

    /** Read all custom features for the user. */
    private static final String SQL_READ_FEATURES =
        new StringBuilder("select PF.PRODUCT_ID,PF.FEATURE_ID,")
        .append("PF.FEATURE_NAME ")
        .append("from TPSD_USER U ")
        .append("inner join TPSD_USER_FEATURE_REL UFR on U.USER_ID=UFR.USER_ID ")
        .append("inner join TPSD_PRODUCT_FEATURE PF on UFR.FEATURE_ID=PF.FEATURE_ID ")
        .append("where U.USER_ID=? and PF.PRODUCT_ID=?")
        .toString();

    /** Sql to read the user id. */
    private static final String SQL_READ_LOCAL_USER_ID =
        new StringBuilder("select U.USER_ID ")
        .append("from TPSD_USER U ")
        .append("where U.USERNAME=?")
        .toString();

    /** Sql to read the user profile's security answer. */
    private static final String SQL_READ_SECURITY_ANSWER =
        new StringBuilder("select U.SECURITY_ANSWER ")
        .append("from TPSD_USER U ")
        .append("where U.USERNAME=?")
        .toString();
        
    /** Sql to read the user profile's security question. */
    private static final String SQL_READ_SECURITY_QUESTION =
        new StringBuilder("select U.SECURITY_QUESTION ")
        .append("from TPSD_USER U ")
        .append("where U.USERNAME=?")
        .toString();

    /** Sql to read the user's token. */
    private static final String SQL_READ_TOKEN =
        new StringBuilder("select U.TOKEN ")
        .append("from TPSD_USER U ")
        .append("where U.USERNAME=? and U.TOKEN is not null")
        .toString();

    /** Sql to read the user's vcard. */
    private static final String SQL_READ_VCARD =
        new StringBuilder("select U.VCARD from TPSD_USER U ")
        .append("where U.USER_ID=?")
        .toString();

    /** Sql to update the password. */
    private static final String SQL_UPDATE_PASSWORD =
        new StringBuilder("update TPSD_USER ")
        .append("set PASSWORD=? ")
        .append("where USERNAME=? and PASSWORD=?")
        .toString();

    /** Sql to create the user's token. */
    private static final String SQL_UPDATE_TOKEN =
        new StringBuilder("update TPSD_USER ")
        .append("set TOKEN=? where USERNAME=?")
        .toString();

    /** Sql to update the user's vcard. */
    private static final String SQL_UPDATE_VCARD =
        new StringBuilder("update TPSD_USER set VCARD=? where USER_ID=?")
        .toString();

    private static final String SQL_VERIFY_EMAIL =
        new StringBuilder("update TPSD_USER_EMAIL ")
        .append("set VERIFIED=?, VERIFICATION_KEY=?")
        .append("where USER_ID=? and EMAIL_ID=? and VERIFICATION_KEY=?")
        .toString();

    /** An e-mail sql interface. */
    private final EMailSql emailSql;

    /**
     * Create UserSql.
     * 
     */
    public UserSql() {
        super();
        this.emailSql = new EMailSql();
    }

    /**
     * Create a user.
     * 
     * @param credentials
     *            A user's <code>Credentials</code>.
     * @param securityQuestion
     *            A security question <code>String</code>.
     * @param securityAnswer
     *            A security answer <code>String</code>.
     * @param vcardStream
     *            A vcard <code>InputStream</code>.
     * @param vcardLength
     *            The vcard length.
     * @param bufferSize
     *            A buffer size <code>Integer</code>.
     */
    public Long create(final Credentials credentials,
            final String securityQuestion, final String securityAnswer,
            final InputStream vcardStream, final Long vcardLength,
            final Integer bufferSize) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_CREATE);
            session.setString(1, credentials.getUsername());
            session.setString(2, credentials.getPassword());
            session.setString(3, securityQuestion);
            session.setString(4, securityAnswer);
            session.setBoolean(5, Boolean.FALSE);
            session.setAsciiStream(6, vcardStream, vcardLength, bufferSize);
            if (1 != session.executeUpdate())
                throw new HypersonicException("Could not create user.");
            final Long userId = session.getIdentity("TPSD_USER");
            session.commit();
            return userId;
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    public void createEmail(final Long userId, final EMail email,
            final VerificationKey key) {
        final HypersonicSession session = openSession();
        try {
            emailSql.readLazyCreate(session, email);
            final Long emailId = emailSql.readEMailId(session, email);

            session.prepareStatement(SQL_CREATE_EMAIL_REL);
            session.setLong(1, userId);
            session.setLong(2, emailId);
            session.setBoolean(3, Boolean.FALSE);
            session.setString(4, key.getKey());
            if (1 != session.executeUpdate())
                throw panic("Could not create e-mail relationship for {0}:{1}.", userId, email);

            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
           session.close();
        }
    }

    /**
     * Create a feature relationship for a user.
     * 
     * @param user
     *            A <code>User</code>.
     * @param feature
     *            A <code>Feature</code>.
     */
    public void createFeature(final User user, final Feature feature) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_CREATE_FEATURE_REL);
            session.setLong(1, user.getLocalId());
            session.setLong(2, feature.getFeatureId());
            if (1 != session.executeUpdate())
                throw new HypersonicException("Could not create feature relationship.");

            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Create a reservation.
     * 
     * @param reservation
     *            A <code>ProfileReservation</code>.
     */
    public void createReservation(final Reservation reservation) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_CREATE_RESERVATION);
            session.setString(1, reservation.getUsername());
            session.setString(2, reservation.getToken().getValue());
            session.setCalendar(3, reservation.getExpiresOn());
            session.setCalendar(4, reservation.getCreatedOn());
            if (1 != session.executeUpdate())
                throw new HypersonicException("Could not create reservation.");
            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    public void deleteEmail(final Long userId, final EMail email) {
        final HypersonicSession session = openSession();
        try {
            final Long emailId = emailSql.readEMailId(session, email);
            session.prepareStatement(SQL_DELETE_EMAIL_REL);
            session.setLong(1, userId);
            session.setLong(2, emailId);
            if (1 != session.executeUpdate())
                throw new HypersonicException(
                        "Could not delete e-mail relationship for {0}:{1}.", userId, email);

            emailSql.delete(session, email);
            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
           session.close();
        }
    }

    /**
     * Delete expired profile reservations.
     * 
     * @param expireOn
     *            The target expiration date.
     */
    public void deleteExpiredReservations(final Calendar expireOn) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_DELETE_EXPIRED_RESERVATIONS);
            session.setCalendar(1, expireOn);
            session.executeUpdate();
            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Delete a reservation.
     * 
     * @param token
     *            A reservation <code>Token</code>.
     */
    public void deleteReservation(final Token token) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_DELETE_RESERVATION_UK);
            session.setString(1, token.getValue());
            if (1 != session.executeUpdate())
                throw new HypersonicException("Could not delete reservation.");
            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Determine whether or not a given reservation exists.
     * 
     * @param token
     *            A reservation <code>Token</code>.
     * @return True if a reservation exists.
     */
    public Boolean doesExistReservation(final Token token) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_DOES_EXIST_RESERVATION_UK);
            session.setString(1, token.getValue());
            session.executeQuery();
            if (session.nextResult()) {
                final int reservationCount = session.getInteger("RESERVATION_COUNT");
                if (0 == reservationCount) {
                    return Boolean.FALSE;
                } else if (1 == reservationCount) {
                    return Boolean.TRUE;
                } else {
                    throw new HypersonicException("Could not determine reservation existence.");
                }
            } else {
                throw new HypersonicException("Could not determine reservation existence.");
            }
        } finally {
            session.close();
        }
    }

    /**
     * Open a user's vcard.
     * 
     * @param userId
     *            A user id <code>Long</code>.
     * @param opener
     *            A <code>VCardOpener</code>.
     * @throws IOException
     */
    public void openVCard(final Long userId, final VCardOpener opener)
            throws IOException {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_VCARD);
            session.setLong(1, userId);
            session.executeQuery();
            if (session.nextResult()) {
                final InputStream stream = session.getClob("VCARD");
                try {
                    opener.open(stream);
                } finally {
                    stream.close();
                }
            } else {
                opener.open(null);
            }
        } finally {
            session.close();
        }
    }

    /**
     * Read a list of users.
     * 
     * @return A list of users.
     */
    public List<User> read() {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ);
            session.executeQuery();
            final List<User> users = new ArrayList<User>();
            while (session.nextResult()) {
                users.add(extract(session));
            }
            return users;
        } finally {
            session.close();
        }
    }

    /**
     * Read a user id for an e-mail address.
     * 
     * @param email
     *            An <code>EMail</code> address.
     * @return A <code>User</code>.
     */
    public User read(final EMail email) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_BY_EMAIL);
            session.setString(1, email.toString());
            session.setBoolean(2, Boolean.TRUE);
            session.executeQuery();
            if (session.nextResult()) {
                return extract(session);
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

    public User read(final JabberId userId) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_BY_USERNAME);
            session.setString(1, userId.getUsername());
            session.executeQuery();
            if (session.nextResult()) {
                return extract(session);
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

    /**
     * Read a user id for an e-mail address.
     * 
     * @param email
     *            An <code>EMail</code> address.
     * @return A <code>User</code>.
     */
    public User read(final Long userId) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_BY_ID);
            session.setLong(1, userId);
            session.executeQuery();
            if (session.nextResult()) {
                return extract(session);
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

    /**
     * Read the user credentials.
     * 
     * @param userId
     *            A user id <code>Long</code>.
     * @return The user <code>Credentials</code>.
     */
    public Credentials readCredentials(final Long userId) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_CREDENTIALS);
            session.setLong(1, userId);
            session.executeQuery();
            if (session.nextResult()) {
                return extractCredentials(session);
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

    public EMail readEmail(final Long userId, final EMail email,
            final VerificationKey key) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_EMAIL_UK);
            session.setLong(1, userId);
            session.setString(2, email.toString());
            session.setString(3, key.getKey());
            session.executeQuery();
            if (session.nextResult()) {
                return EMailBuilder.parse(session.getString("EMAIL"));
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

    public List<ProfileEMail> readEMails(final User user) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_EMAILS);
            session.setLong(1, user.getLocalId());
            session.executeQuery();
            final List<ProfileEMail> emails = new ArrayList<ProfileEMail>();
            while (session.nextResult()) {
                emails.add(extractEMail(session));
            }
            return emails;
        } finally {
            session.close();
        }
    }

    public List<Feature> readFeatures(final Long userId, final Long productId) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_FEATURES);
            session.setLong(1, userId);
            session.setLong(2, productId);
            session.executeQuery();
            final List<Feature> features = new ArrayList<Feature>();
            while (session.nextResult()) {
                features.add(extractFeature(session));
            }
            return features;
        } finally {
            session.close();
        }
    }

    /**
     * Read the user profile's security answer.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return The security question answer <code>String</code>.
     */
    public String readProfileSecurityAnswer(final JabberId userId) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_SECURITY_ANSWER);
            session.setString(1, userId.getUsername());
            session.executeQuery();
            if (session.nextResult()) {
                return session.getString("SECURITY_ANSWER");
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

    /**
     * Read the user profile's security answer.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return The security question answer <code>String</code>.
     */
    public String readProfileSecurityQuestion(final JabberId userId) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_SECURITY_QUESTION);
            session.setString(1, userId.getUsername());
            session.executeQuery();
            if (session.nextResult()) {
                return session.getString("SECURITY_QUESTION");
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

    public Token readProfileToken(final JabberId userId) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_TOKEN);
            session.setString(1, userId.getUsername());
            session.executeQuery();
            if (session.nextResult()) {
                return extractToken(session);
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

    /**
     * Update the user's password.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param password
     *            A password <code>String</code>.
     * @param newPassword
     *            A new password <code>String</code>.
     */
    public void updatePassword(final JabberId userId, final String password,
            final String newPassword) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_UPDATE_PASSWORD);
            session.setString(1, newPassword);
            session.setString(2, userId.getUsername());
            session.setString(3, password);
            if (1 != session.executeUpdate())
                throw new HypersonicException(
                        "Could not update password for user {0}.",
                        userId.getUsername());

            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    public void updateProfileToken(final JabberId userId, final Token token) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_UPDATE_TOKEN);
            session.setString(1, token.getValue());
            session.setString(2, userId.getUsername());
            if (1 != session.executeUpdate())
                throw panic("Could not update profile token.");

            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Update the user's vcard information.
     * 
     * @param userId
     *            The user id <code>Long</code>.
     * @param vcardStream
     *            The vcard <code>InputStream</code>.
     * @param vcardLength
     *            The vcard length <code>Long</code>.
     * @param bufferSize
     *            The buffer size <code>Integer</code>.
     */
    public void updateVCard(final Long userId, final InputStream vcardStream,
            final Long vcardLength, final Integer bufferSize) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_UPDATE_VCARD);
            session.setAsciiStream(1, vcardStream, vcardLength, bufferSize);
            session.setLong(2, userId);
            if (1 != session.executeUpdate())
                throw panic("Could not update profile vcard.");

            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    public void verifyEmail(final Long userId, final EMail email,
            final VerificationKey key) {
        final HypersonicSession session = openSession();
        try {
            final Long emailId = emailSql.readEMailId(session, email);

            session.prepareStatement(SQL_VERIFY_EMAIL);
            session.setBoolean(1, Boolean.TRUE);
            session.setString(2, null);
            session.setLong(3, userId);
            session.setLong(4, emailId);
            session.setString(5, key.getKey());
            if (1 != session.executeUpdate())
                throw new HypersonicException(
                        "Could not verify e-mail for {0}:{1}:{2}.", userId,
                        email, key);
            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Extract the user information from the database session.
     * 
     * @param session
     *            A db session.
     * @return A user.
     */
    User extract(final HypersonicSession session) {
        final User user = new User();
        user.setLocalId(session.getLong("USER_ID"));
        user.setId(JabberIdBuilder.parseUsername(session.getString("USERNAME")));
        return user;
    }

    Credentials extractCredentials(final HypersonicSession session) {
        final Credentials credentials = new Credentials();
        credentials.setPassword(session.getString("PASSWORD"));
        credentials.setUsername(session.getString("USERNAME"));
        return credentials;
    }

    Feature extractFeature(final HypersonicSession session) {
        final Feature feature = new Feature();
        feature.setFeatureId(session.getLong("FEATURE_ID"));
        feature.setName(session.getString("FEATURE_NAME"));
        feature.setProductId(session.getLong("PRODUCT_ID"));
        return feature;
    }

    /**
     * Read the local user id.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A local user id <code>Long</code>.
     */
    Long readLocalUserId(final JabberId userId) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_LOCAL_USER_ID);
            session.setString(1, userId.getUsername());
            session.executeQuery();
            if (session.nextResult()) {
                return session.getLong("USER_ID");
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

    /**
     * Extract a profile e-mail address from the session.
     * 
     * @param session
     *            A <code>HypersonicSession</code>.
     * @return A <code>ProfileEMail</code>.
     */
    private ProfileEMail extractEMail(final HypersonicSession session) {
        final ProfileEMail email = new ProfileEMail();
        email.setEmail(session.getEMail("EMAIL"));
        email.setEmailId(session.getLong("EMAIL_ID"));
        email.setProfileId(session.getLong("USER_ID"));
        email.setVerified(session.getBoolean("VERIFIED"));
        return email;
    }

    /**
     * Extract a token from the session.
     * 
     * @param session
     *            A database <code>HypersonicSession</code>.
     * @return A <code>Token</code>.
     */
    private Token extractToken(final HypersonicSession session) {
        final Token token = new Token();
        token.setValue(session.getString("TOKEN"));
        return token;
    }
}
