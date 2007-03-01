/*
 * Created On: Jul 20, 2006 2:43:22 PM
 */
package com.thinkparity.desdemona.model.io.sql;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.email.EMailBuilder;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.jabber.JabberIdBuilder;

import com.thinkparity.codebase.model.migrator.Feature;
import com.thinkparity.codebase.model.profile.VerificationKey;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.user.Token;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.desdemona.model.io.hsqldb.HypersonicException;
import com.thinkparity.desdemona.model.io.hsqldb.HypersonicSession;

/**
 * @author raymond@thinkparity.com
 * @version
 */
public class UserSql extends AbstractSql {

    /** Sql to create an email address. */
    private static final String SQL_CREATE_EMAIL =
            new StringBuffer("insert into EMAIL ")
            .append("(USER_ID,EMAIL,VERIFIED,VERIFICATION_KEY) ")
            .append("values (?,?,?,?)")
            .toString();

    /** Sql to delete an email address. */
    private static final String SQL_DELETE_EMAIL =
            new StringBuffer("delete from EMAIL ")
            .append("where USER_ID=? and EMAIL=?")
            .toString();

    /** Sql to read all users. */
    private static final String SQL_READ =
        new StringBuffer("select PU.USERNAME,PU.USER_ID ")
        .append("from PARITY_USER PU ")
        .append("order by PU.USERNAME asc")
        .toString();

    /** Sql to read a user's archive ids. */
    private static final String SQL_READ_ARCHIVE_IDS =
            new StringBuffer("select PU.USERNAME \"BACKUP_USERNAME\" ")
            .append("from USER_BACKUP_REL UBR ")
            .append("inner join PARITY_USER PU on UBR.BACKUP_ID=PU.USER_ID ")
            .append("where UBR.USER_ID=? ")
            .append("order by UBR.BACKUP_ID asc")
            .toString();

    /** Sql to read an archive user's credentials. */
    private static final String SQL_READ_BACKUP_CREDENTIALS =
            new StringBuffer("select JU.USERNAME,JU.PASSWORD ")
            .append("from jiveUser JU ")
            .append("inner join PARITY_USER PU on JU.USERNAME=PU.USERNAME ")
            .append("inner join USER_BACKUP_REL UBR on PU.USER_ID=UBR.BACKUP_ID ")
            .append("where PU.USERNAME=?")
            .toString();

    /** Sql to read a user. */
    private static final String SQL_READ_BY_USER_ID =
            new StringBuffer("select PU.USERNAME,PU.USER_ID ")
            .append("from PARITY_USER PU ")
            .append("where PU.USERNAME=?")
            .toString();

    /** Sql to read email addresses. */
    private static final String SQL_READ_EMAIL =
        new StringBuffer("select E.EMAIL ")
        .append("from EMAIL E ")
        .append("inner join PARITY_USER PU on PU.USER_ID=E.USER_ID ")
        .append("where PU.USERNAME=? and E.EMAIL=? ")
        .append("and E.VERIFICATIONKEY=?")
        .toString();
        
    /** Sql to count email addresses. */
    private static final String SQL_READ_EMAIL_COUNT =
        new StringBuffer("select COUNT(UE.EMAIL) \"EMAIL_COUNT\" ")
        .append("from USER_EMAIL UE ")
        .append("where UE.EMAIL=?")
        .toString();

    /** Sql to read e-mail addresses. */
    private static final String SQL_READ_EMAILS =
            new StringBuffer("select UE.EMAIL ")
            .append("from USER_EMAIL UE ")
            .append("inner join PARITY_USER PU on UE.USER_ID=PU.USER_ID ")
            .append("where PU.USERNAME=? and UE.VERIFIED=?")
            .toString();

    /** Read all custom features for the user. */
    private static final String SQL_READ_FEATURES =
        new StringBuffer("select PF.PRODUCT_ID,PF.FEATURE_ID,")
        .append("PF.FEATURE_NAME ")
        .append("from PARITY_USER PU ")
        .append("inner join USER_FEATURE_REL UFR on PU.USER_ID=UFR.USER_ID ")
        .append("inner join PRODUCT_FEATURE PF on UFR.FEATURE_ID=PF.FEATURE_ID ")
        .append("where PF.PRODUCT_ID=? and PU.USERNAME=?")
        .toString();

    /** Sql to read the user id. */
    private static final String SQL_READ_LOCAL_USER_ID =
        new StringBuffer("select PU.USER_ID ")
        .append("from PARITY_USER PU ")
        .append("where PU.USERNAME=?")
        .toString();

    /** Sql to read the user profile's security answer. */
    private static final String SQL_READ_SECURITY_ANSWER =
            new StringBuffer("select PU.SECURITY_ANSWER ")
            .append("from PARITY_USER PU ")
            .append("where PU.USERNAME=?")
            .toString();

    /** Sql to read the user profile's security question. */
    private static final String SQL_READ_SECURITY_QUESTION =
            new StringBuffer("select PU.SECURITY_QUESTION ")
            .append("from PARITY_USER PU ")
            .append("where PU.USERNAME=?")
            .toString();

    /** Sql to read the user's token. */
    private static final String SQL_READ_TOKEN =
        new StringBuffer("select PU.TOKEN ")
        .append("from PARITY_USER PU ")
        .append("where PU.USERNAME=? and PU.TOKEN is not null")
        .toString();

    /** Sql to read a username from an e-mail address. */
    private static final String SQL_READ_USERNAME =
            new StringBuffer("select PU.USERNAME ")
            .append("from USER_EMAIL UE ")
            .append("inner join PARITY_USER PU on UE.USER_ID=PU.USER_ID ")
            .append("where UE.EMAIL=?")
            .toString();

    /** Sql to read the user's vcard. */
    private static final String SQL_READ_VCARD =
        new StringBuffer("select PU.VCARD ")
        .append("from PARITY_USER PU ")
        .append("where PU.USERNAME=?")
        .toString();

    /** Sql to create the user's token. */
    private static final String SQL_UPDATE_TOKEN =
        new StringBuffer("update PARITY_USER ")
        .append("set TOKEN=? where USERNAME=?")
        .toString();

    /** Sql to update the user's vcard. */
    private static final String SQL_UPDATE_VCARD =
        new StringBuffer("update PARITY_USER PU ")
        .append("set VCARD=? where USERNAME=?")
        .toString();

    private static final String SQL_VERIFY_EMAIL =
            new StringBuffer("update USER_EMAIL ")
            .append("set VERIFIED=?, VERIFICATION_KEY=?")
            .append("where USER_ID=? and EMAIL=? and VERIFICATION_KEY=?")
            .toString();

    /** Create UserSql. */
    public UserSql() { super(); }

    public void createEmail(final JabberId userId, final EMail email,
            final VerificationKey key) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_CREATE_EMAIL);
            session.setLong(1, readLocalUserId(userId));
            session.setString(2, email.toString());
            session.setBoolean(3, Boolean.FALSE);
            session.setString(4, key.getKey());
            if (1 != session.executeUpdate())
                throw new HypersonicException("Could not create e-mail for {0}:{1}.", userId, email);

            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
           session.close();
        }
    }

    public void deleteEmail(final JabberId userId, final EMail email) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_DELETE_EMAIL);
            session.setLong(1, readLocalUserId(userId));
            session.setString(2, email.toString());
            if (1 != session.executeUpdate())
                throw new HypersonicException(
                        "Could not delete e-mail for {0}:{1}.", userId, email);
            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
           session.close();
        }
    }

    public Boolean isEmailAvailable(final EMail email) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_EMAIL_COUNT);
            session.setString(1, email.toString());
            session.executeQuery();
            session.nextResult();
            if (0 == session.getInteger("EMAIL_COUNT")) {
                return Boolean.TRUE;
            } else if (1 == session.getInteger("EMAIL_COUNT")) {
                return Boolean.FALSE;
            } else {
                throw new HypersonicException("Could not determine availability.");
            }
        } catch (final Throwable t) {
            throw translateError(session, t);
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

    public User read(final JabberId userId) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_BY_USER_ID);
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

    public Credentials readArchiveCredentials(final JabberId archiveId) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_BACKUP_CREDENTIALS);
            session.setString(1, archiveId.getUsername());
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

    public List<JabberId> readArchiveIds(final JabberId userId) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_ARCHIVE_IDS);
            session.setLong(1, readLocalUserId(userId));
            session.executeQuery();
            final List<JabberId> archiveIds = new ArrayList<JabberId>();
            while (session.nextResult()) {
                archiveIds.add(JabberIdBuilder.parseUsername(
                        session.getString("BACKUP_USERNAME")));
            }
            return archiveIds;
        } finally {
            session.close();
        }
    }

    public EMail readEmail(final JabberId userId, final EMail email,
            final VerificationKey key) throws SQLException {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_EMAIL);
            session.setString(1, userId.getUsername());
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

    public List<EMail> readEmails(final JabberId jabberId,
            final Boolean verified) throws SQLException {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_EMAILS);
            session.setString(1, jabberId.getUsername());
            session.setBoolean(2, verified);
            session.executeQuery();
            final List<EMail> emails = new ArrayList<EMail>();
            while (session.nextResult()) {
                emails.add(EMailBuilder.parse(session.getString("EMAIL")));
            }
            return emails;
        } finally {
            session.close();
        }
    }

    public List<Feature> readFeatures(final JabberId userId,
            final Long productId) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_FEATURES);
            session.setLong(1, productId);
            session.setString(2, userId.getUsername());
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

    public String readProfileVCard(final JabberId userId) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_VCARD);
            session.setString(1, userId.getUsername());
            session.executeQuery();
            if (session.nextResult()) {
                return session.getString("VCARD");
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

    /**
     * Read a jabber id for an e-mail addreses.
     * 
     * @param email
     *            An e-mail address.
     * @return A jabber id.
     * @throws SQLException
     */
    public String readUsername(final EMail email) throws SQLException {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_USERNAME);
            session.setString(1, email.toString());
            session.executeQuery();
            if (session.nextResult()) {
                return session.getString("USERNAME");
            } else {
                return null;
            }
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
                throw new HypersonicException("Could not update profile token.");

            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    public void updateProfileVCard(final JabberId userId, final String vcardXML) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_UPDATE_VCARD);
            session.setString(1, vcardXML);
            session.setString(2, userId.getUsername());
            if (1 != session.executeUpdate())
                throw new HypersonicException("Could not update profile vcard.");

            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    public void verifyEmail(final JabberId userId, final EMail email,
            final VerificationKey key) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_VERIFY_EMAIL);
            session.setBoolean(1, Boolean.TRUE);
            session.setString(2, null);
            session.setLong(3, readLocalUserId(userId));
            session.setString(4, email.toString());
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
