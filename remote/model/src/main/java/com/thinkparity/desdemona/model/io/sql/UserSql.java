/*
 * Created On: Jul 20, 2006 2:43:22 PM
 */
package com.thinkparity.desdemona.model.io.sql;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.sql.DataSource;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.email.EMailBuilder;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.jabber.JabberIdBuilder;

import com.thinkparity.codebase.model.contact.ContactInvitation;
import com.thinkparity.codebase.model.migrator.Feature;
import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.profile.EMailReservation;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.ProfileEMail;
import com.thinkparity.codebase.model.profile.SecurityCredentials;
import com.thinkparity.codebase.model.profile.UsernameReservation;
import com.thinkparity.codebase.model.profile.VerificationKey;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.TemporaryCredentials;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.user.UserVCard;
import com.thinkparity.codebase.model.util.Token;
import com.thinkparity.codebase.model.util.VCardReader;
import com.thinkparity.codebase.model.util.VCardWriter;

import com.thinkparity.desdemona.model.io.hsqldb.HypersonicException;
import com.thinkparity.desdemona.model.io.hsqldb.HypersonicSession;
import com.thinkparity.desdemona.model.profile.payment.PaymentPlan;

/**
 * <b>Title:</b>thinkParity DesdemonaModel User SQL<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.17
 */
public final class UserSql extends AbstractSql {

    /** Sql to create a user. */
    private static final String SQL_CREATE =
        new StringBuilder("insert into TPSD_USER ")
        .append("(USERNAME,PASSWORD,SECURITY_QUESTION,SECURITY_ANSWER,ACTIVE,")
        .append("DISABLED,VCARD,CREATED_ON) ")
        .append("values (?,?,?,?,?,?,?,?)")
        .toString();

    /** Sql to create an email address. */
    private static final String SQL_CREATE_EMAIL_REL =
        new StringBuilder("insert into TPSD_USER_EMAIL ")
        .append("(USER_ID,EMAIL_ID,VERIFIED,VERIFICATION_KEY) ")
        .append("values (?,?,?,?)")
        .toString();

    /** Sql to create a reservation. */
    private static final String SQL_CREATE_EMAIL_RESERVATION =
        new StringBuilder("insert into TPSD_EMAIL_RESERVATION ")
        .append("(EMAIL,TOKEN,EXPIRES_ON,CREATED_ON) ")
        .append("values (?,?,?,?)")
        .toString();

    /** Sql to create a user feature relationship. */
    private static final String SQL_CREATE_FEATURE_REL =
        new StringBuilder("insert into TPSD_USER_FEATURE_REL ")
        .append("(USER_ID,FEATURE_ID) ")
        .append("values (?,?)")
        .toString();

    /** Sql to create a user invitation record. */
    private static final String SQL_CREATE_INVITATION =
        new StringBuilder("insert into TPSD_USER_INVITATION ")
        .append("(USER_ID,INVITED_BY,INVITED_ON) ")
        .append("values (?,?,?)")
        .toString();

    /** Sql to delete a user invitation record. */
    private static final String SQL_DELETE_INVITATION =
        new StringBuilder("delete from TPSD_USER_INVITATION ")
        .append("where USER_ID=? ")
        .append("and INVITED_BY=? ")
        .append("and INVITED_ON=? ")
        .append("and ACCEPTED_ON is null")
        .toString();

    /** Sql to create a payment plan. */
    private static final String SQL_CREATE_PAYMENT_PLAN =
        new StringBuilder("insert into TPSD_USER_PAYMENT_PLAN ")
        .append("(USER_ID,PLAN_ID,CREATED_ON) ")
        .append("values (?,?,?)")
        .toString();

    /** Sql to create a user release relationship. */
    private static final String SQL_CREATE_PRODUCT_RELEASE =
        new StringBuilder("insert into TPSD_USER_PRODUCT_RELEASE_REL ")
        .append("(USER_ID,PRODUCT_ID,RELEASE_ID) ")
        .append("values (?,?,?)")
        .toString();

    /** Sql to create temporary credentials. */
    private static final String SQL_CREATE_TEMPORARY_CREDENTIALS =
        new StringBuilder("insert into TPSD_USER_TEMPORARY_CREDENTIAL ")
        .append("(USER_ID,TOKEN,EXPIRES_ON,CREATED_ON) ")
        .append("values (?,?,?,?)")
        .toString();

    /** Sql to create a reservation. */
    private static final String SQL_CREATE_USERNAME_RESERVATION =
        new StringBuilder("insert into TPSD_USERNAME_RESERVATION ")
        .append("(USERNAME,TOKEN,EXPIRES_ON,CREATED_ON) ")
        .append("values (?,?,?,?)")
        .toString();

    /** Sql to delete an email address. */
    private static final String SQL_DELETE_EMAIL_REL =
        new StringBuilder("delete from TPSD_USER_EMAIL ")
        .append("where USER_ID=? and EMAIL_ID=?")
        .toString();

    /** Sql to delete a reservation by its unique key. */
    private static final String SQL_DELETE_EMAIL_RESERVATION_UK =
        new StringBuilder("delete from TPSD_EMAIL_RESERVATION ")
        .append("where TOKEN=?")
        .toString();

    /** Sql to delete expired e-mail address reservations. */
    private static final String SQL_DELETE_EXPIRED_EMAIL_RESERVATIONS =
        new StringBuilder("delete from TPSD_EMAIL_RESERVATION ")
        .append("where EXPIRES_ON < ?")
        .toString();

    /** Sql to delete expired username reservations. */
    private static final String SQL_DELETE_EXPIRED_USERNAME_RESERVATIONS =
        new StringBuilder("delete from TPSD_USERNAME_RESERVATION ")
        .append("where EXPIRES_ON < ?")
        .toString();

    /** Sql to delete a feature relationship. */
    private static final String SQL_DELETE_FEATURE_REL =
        new StringBuilder("delete from TPSD_USER_FEATURE_REL ")
        .append("where USER_ID=? and FEATURE_ID=?")
        .toString();

    /** Sql to delete temporary credentials. */
    private static final String SQL_DELETE_TEMPORARY_CREDENTIALS =
        new StringBuilder("delete from TPSD_USER_TEMPORARY_CREDENTIAL ")
        .append("where EXPIRES_ON < ?")
        .toString();

    /** Sql to delete temporary credentials. */
    private static final String SQL_DELETE_TEMPORARY_CREDENTIALS_PK =
        new StringBuilder("delete from TPSD_USER_TEMPORARY_CREDENTIAL ")
        .append("where USER_ID=?")
        .toString();

    /** Sql to delete temporary credentials. */
    private static final String SQL_DELETE_TEMPORARY_CREDENTIALS_UK =
        new StringBuilder("delete from TPSD_USER_TEMPORARY_CREDENTIAL ")
        .append("where TOKEN=?")
        .toString();

    /** Sql to delete a reservation by its unique key. */
    private static final String SQL_DELETE_USERNAME_RESERVATION_UK =
        new StringBuilder("delete from TPSD_USERNAME_RESERVATION ")
        .append("where TOKEN=?")
        .toString();

    /** Sql to determine e-mail address reservation existence by unique key. */
    private static final String SQL_DOES_EXIST_EMAIL_RESERVATION_UK =
        new StringBuilder("select count(ER.TOKEN) \"RESERVATION_COUNT\" ")
        .append("from TPSD_EMAIL_RESERVATION ER ")
        .append("where ER.TOKEN=?")
        .toString();

    /** Sql to determine user existence by e-mail unique key. */
    private static final String SQL_DOES_EXIST_EMAIL_UK =
        new StringBuilder("select count(U.USER_ID) \"USER_COUNT\" ")
        .append("from TPSD_USER U ")
        .append("inner join TPSD_USER_EMAIL UE on UE.USER_ID=U.USER_ID ")
        .append("inner join TPSD_EMAIL E on E.EMAIL_ID=UE.EMAIL_ID ")
        .append("where E.EMAIL=?")
        .toString();

    /** Sql to determine temporary credential existence by unique key. */
    private static final String SQL_DOES_EXIST_TEMPORARY_CREDENTIALS_UK =
        new StringBuilder("select count(UTC.TOKEN) \"CREDENTIAL_COUNT\" ")
        .append("from TPSD_USER_TEMPORARY_CREDENTIAL UTC ")
        .append("inner join TPSD_USER U on U.USER_ID=UTC.USER_ID ")
        .append("where U.USER_ID=? and UTC.TOKEN=?")
        .toString();

    /** Sql to determine user existence by unique key. */
    private static final String SQL_DOES_EXIST_UK =
        new StringBuilder("select count(USER_ID) \"USER_COUNT\" ")
        .append("from TPSD_USER U ")
        .append("where U.USERNAME=?")
        .toString();

    /** Sql to determine username reservation existence by unique key. */
    private static final String SQL_DOES_EXIST_USERNAME_RESERVATION_UK =
        new StringBuilder("select count(UR.TOKEN) \"RESERVATION_COUNT\" ")
        .append("from TPSD_USERNAME_RESERVATION UR ")
        .append("where UR.TOKEN=?")
        .toString();

    /** Sql to read all users. */
    private static final String SQL_READ =
        new StringBuilder("select U.USERNAME,U.USER_ID ")
        .append("from TPSD_USER U ")
        .append("order by U.USERNAME asc")
        .toString();

    /** Sql to read the active flag. */
    private static final String SQL_READ_ACTIVE =
        new StringBuilder("select U.ACTIVE ")
        .append("from TPSD_USER U ")
        .append("where U.USER_ID=?")
        .toString();

    /** Sql to read a user. */
    private static final String SQL_READ_BY_CREDENTIALS =
        new StringBuilder("select U.USERNAME,U.USER_ID ")
        .append("from TPSD_USER U ")
        .append("where U.USERNAME=? and U.PASSWORD=? and U.DISABLED=?")
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

    /** Sql to read an e-mail count. */
    private static final String SQL_READ_EMAIL_COUNT =
        new StringBuilder("select count(EMAIL_ID) \"EMAIL_COUNT\" ")
        .append("from TPSD_USER_EMAIL UE ")
        .append("where UE.USER_ID=? and UE.VERIFIED=?")
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
        .append("where U.USER_ID=? ")
        .append("order by PF.PRODUCT_ID")
        .toString();

    /** Sql to read the user id. */
    private static final String SQL_READ_LOCAL_USER_ID =
        new StringBuilder("select U.USER_ID ")
        .append("from TPSD_USER U ")
        .append("where U.USERNAME=?")
        .toString();

    /** Read all custom features for the user. */
    private static final String SQL_READ_PRODUCT_FEATURES =
        new StringBuilder("select PF.PRODUCT_ID,PF.FEATURE_ID,")
        .append("PF.FEATURE_NAME ")
        .append("from TPSD_USER U ")
        .append("inner join TPSD_USER_FEATURE_REL UFR on U.USER_ID=UFR.USER_ID ")
        .append("inner join TPSD_PRODUCT_FEATURE PF on UFR.FEATURE_ID=PF.FEATURE_ID ")
        .append("inner join TPSD_PRODUCT P on P.PRODUCT_ID=PF.PRODUCT_ID ")
        .append("where U.USER_ID=? and P.PRODUCT_NAME=?")
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

    /** Sql to activate/deactivate a user. */
    private static final String SQL_UPDATE_ACTIVE =
        new StringBuilder("update TPSD_USER ")
        .append("set ACTIVE=? where USER_ID=?")
        .toString();

    /** Sql to update a user invitation record. */
    private static final String SQL_UPDATE_INVITATION =
        new StringBuilder("update TPSD_USER_INVITATION ")
        .append("set ACCEPTED_ON=? ")
        .append("where USER_ID=? ")
        .append("and INVITED_BY=? ")
        .append("and INVITED_ON=?")
        .toString();

    /** Sql to update the password. */
    private static final String SQL_UPDATE_PASSWORD =
        new StringBuilder("update TPSD_USER ")
        .append("set PASSWORD=? ")
        .append("where USERNAME=? and PASSWORD=?")
        .toString();

    /** Sql to update a user release relationship. */
    private static final String SQL_UPDATE_PRODUCT_RELEASE =
        new StringBuilder("update TPSD_USER_PRODUCT_RELEASE_REL ")
        .append("set RELEASE_ID=? ")
        .append("where USER_ID=? and PRODUCT_ID=?")
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

    /** Sql to determine a user count for a product release. */
    private static final String SQL_USER_COUNT_BY_PRODUCT_RELEASE =
        new StringBuilder("select count(UPRR.USER_ID) \"USER_COUNT\" ")
        .append("from TPSD_USER_PRODUCT_RELEASE_REL UPRR ")
        .append("where UPRR.PRODUCT_ID=? and UPRR.RELEASE_ID=?")
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
     * Create UserSql.
     * 
     * @param dataSource
     *            A <code>DataSource</code>.
     */
    public UserSql(final DataSource dataSource) {
        super(dataSource);
        this.emailSql = new EMailSql(dataSource);
    }
        
    /**
     * Create a user.
     * 
     * @param T
     *            The <code>UserVCard</code> type.
     * @param active
     *            A <code>Boolean</code>.
     * @param credentials
     *            A user's <code>Credentials</code>.
     * @param securityQuestion
     *            A security question <code>String</code>.
     * @param securityAnswer
     *            A security answer <code>String</code>.
     * @param vcard
     *            The vcard <code>T</code>.
     */
    public <T extends UserVCard> Long create(final Boolean active,
            final Boolean disabled, final Credentials credentials,
            final SecurityCredentials securityCredentials, final T vcard,
            final VCardWriter<T> vcardWriter, final Calendar createdOn) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_CREATE);
            session.setString(1, credentials.getUsername());
            session.setString(2, credentials.getPassword());
            session.setString(3, securityCredentials.getQuestion());
            session.setString(4, securityCredentials.getAnswer());
            session.setBoolean(5, active);
            session.setBoolean(6, disabled);
            session.setVCard(7, vcard, vcardWriter);
            session.setCalendar(8, createdOn);
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
     * Create a reservation.
     * 
     * @param reservation
     *            A <code>ProfileReservation</code>.
     */
    public void createEMailReservation(final EMailReservation reservation) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_CREATE_EMAIL_RESERVATION);
            session.setEMail(1, reservation.getEMail());
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
     * Create a payment plan relationship for a user.
     * 
     * @param user
     *            A <code>User</code>.
     * @param plan
     *            A <code>PaymentPlan</code>.
     * @param createdOn
     *            A <code>Calendar</code>.
     */
    public void createPaymentPlan(final User user, final PaymentPlan plan,
            final Calendar createdOn) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_CREATE_PAYMENT_PLAN);
            session.setLong(1, user.getLocalId());
            session.setLong(2, plan.getId());
            session.setCalendar(3, createdOn);
            if (1 != session.executeUpdate()) {
                throw panic("Could not create payment plan.");
            }

            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Create a user/product release relationship.
     * 
     * @param user
     *            A <code>User</code>.
     * @param product
     *            A <code>Product</code>.
     * @param release
     *            A <code>Release</code>.
     */
    public void createProductRelease(final User user, final Product product,
            final Release release) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_CREATE_PRODUCT_RELEASE);
            session.setLong(1, user.getLocalId());
            session.setLong(2, product.getId());
            session.setLong(3, release.getId());
            if (1 != session.executeUpdate())
                throw panic("Could not create product release.");
            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Create temporary credentials for a user.
     * 
     * @param user
     *            A <code>User</code>.
     * @param credentials
     *            A set of <code>TemporaryCredentials</code>.
     */
    public void createTemporaryCredentials(final User user,
            final TemporaryCredentials credentials) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_CREATE_TEMPORARY_CREDENTIALS);
            session.setLong(1, user.getLocalId());
            session.setString(2, credentials.getToken().getValue());
            session.setCalendar(3, credentials.getExpiresOn());
            session.setCalendar(4, credentials.getCreatedOn());
            if (1 != session.executeUpdate())
                throw panic("Could not create temporary credentials.");
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
    public void createUsernameReservation(final UsernameReservation reservation) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_CREATE_USERNAME_RESERVATION);
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
     * Delete a reservation.
     * 
     * @param token
     *            A reservation <code>Token</code>.
     */
    public void deleteEMailReservation(final Token token) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_DELETE_EMAIL_RESERVATION_UK);
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
     * Delete expired profile reservations.
     * 
     * @param expireOn
     *            The target expiration date.
     */
    public void deleteReservations(final Calendar expireOn) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_DELETE_EXPIRED_USERNAME_RESERVATIONS);
            session.setCalendar(1, expireOn);
            session.executeUpdate();
            session.prepareStatement(SQL_DELETE_EXPIRED_EMAIL_RESERVATIONS);
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
     * Delete all temporary credentials that expire before the date.
     * 
     * @param expireOn
     *            An expire on <code>Calendar</code>.
     */
    public void deleteTemporaryCredentials(final Calendar expireOn) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_DELETE_TEMPORARY_CREDENTIALS);
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
     * Delete all temporary credentials that expire before the date.
     * 
     * @param expireOn
     *            An expire on <code>Calendar</code>.
     */
    public void deleteTemporaryCredentials(final TemporaryCredentials credentials) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_DELETE_TEMPORARY_CREDENTIALS_UK);
            session.setString(1, credentials.getToken().getValue());
            session.executeUpdate();
            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Delete all temporary credentials for a user.
     * 
     * @param user
     *            A <code>User</code>.
     */
    public void deleteTemporaryCredentials(final User user) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_DELETE_TEMPORARY_CREDENTIALS_PK);
            session.setLong(1, user.getLocalId());
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
    public void deleteUsernameReservation(final Token token) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_DELETE_USERNAME_RESERVATION_UK);
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
     * Determine whether or not a user exists by their e-mail address.
     * 
     * @param username
     *            A username <code>String</code>.
     * @return True if the user exists.
     */
    public Boolean doesExist(final EMail email) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_DOES_EXIST_EMAIL_UK);
            session.setEMail(1, email);
            session.executeQuery();
            if (session.nextResult()) {
                final int userCount = session.getInteger("USER_COUNT");
                if (0 == userCount) {
                    return Boolean.FALSE;
                } else if (1 == userCount) {
                    return Boolean.TRUE;
                } else {
                    throw new HypersonicException("Could not determine user existence.");
                }
            } else {
                throw new HypersonicException("Could not determine user existence.");
            }
        } finally {
            session.close();
        }
    }

    /**
     * Determine whether or not a user exists by their username.
     * 
     * @param username
     *            A username <code>String</code>.
     * @return True if the user exists.
     */
    public Boolean doesExist(final String username) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_DOES_EXIST_UK);
            session.setString(1, username);
            session.executeQuery();
            if (session.nextResult()) {
                final int userCount = session.getInteger("USER_COUNT");
                if (0 == userCount) {
                    return Boolean.FALSE;
                } else if (1 == userCount) {
                    return Boolean.TRUE;
                } else {
                    throw new HypersonicException("Could not determine user existence.");
                }
            } else {
                throw new HypersonicException("Could not determine user existence.");
            }
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
    public Boolean doesExistEMailReservation(final Token token) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_DOES_EXIST_EMAIL_RESERVATION_UK);
            session.setString(1, token.getValue());
            session.executeQuery();
            if (session.nextResult()) {
                final int reservationCount = session.getInteger("RESERVATION_COUNT");
                if (0 == reservationCount) {
                    return Boolean.FALSE;
                } else if (1 == reservationCount) {
                    return Boolean.TRUE;
                } else {
                    throw new HypersonicException("Could not determine e-mail reservation existence.");
                }
            } else {
                throw new HypersonicException("Could not determine e-mail reservation existence.");
            }
        } finally {
            session.close();
        }
    }

    /**
     * Determine whether or not temporary credentials exist for the token.
     * 
     * @param token
     *            A <code>Token</code>.
     * @return True if temporary credentials exist, false otherwise.
     */
    public Boolean doesExistTemporaryCredentials(final User user,
            final Token token) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_DOES_EXIST_TEMPORARY_CREDENTIALS_UK);
            session.setLong(1, user.getLocalId());
            session.setString(2, token.getValue());
            session.executeQuery();
            if (session.nextResult()) {
                final int credentialCount = session.getInteger("CREDENTIAL_COUNT");
                if (0 == credentialCount) {
                    return Boolean.FALSE;
                } else if (1 == credentialCount) {
                    return Boolean.TRUE;
                } else {
                    throw new HypersonicException("Could not determine .");
                }
            } else {
                throw new HypersonicException("Could not determine .");
            }
        } finally {
            session.close();
        }
    }

    /**
     * Determine if any users exist (reference) the product release.
     * 
     * @param product
     *            A <code>Product</code>.
     * @param release
     *            A <code>Release</code>.
     * @return True if any users reference the product release.
     */
    public Boolean doesExistUser(final Product product, final Release release) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_USER_COUNT_BY_PRODUCT_RELEASE);
            session.setLong(1, product.getId());
            session.setLong(2, release.getId());
            session.executeQuery();
            if (session.nextResult()) {
                final int userCount = session.getInteger("USER_COUNT");
                if (0 == userCount) {
                    return Boolean.FALSE;
                } else if (0 < userCount) {
                    return Boolean.TRUE;
                } else {
                    throw new HypersonicException("Could not determine .");
                }
            } else {
                throw new HypersonicException("Could not determine .");
            }
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
    public Boolean doesExistUsernameReservation(final Token token) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_DOES_EXIST_USERNAME_RESERVATION_UK);
            session.setString(1, token.getValue());
            session.executeQuery();
            if (session.nextResult()) {
                final int reservationCount = session.getInteger("RESERVATION_COUNT");
                if (0 == reservationCount) {
                    return Boolean.FALSE;
                } else if (1 == reservationCount) {
                    return Boolean.TRUE;
                } else {
                    throw new HypersonicException("Could not determine username reservation existence.");
                }
            } else {
                throw new HypersonicException("Could not determine username reservation existence.");
            }
        } finally {
            session.close();
        }
    }

    /**
     * Determine if a user is active.
     * 
     * @param user
     *            A <code>User</code>.
     * @return True if the user is active.
     */
    public Boolean isActive(final User user) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_ACTIVE);
            session.setLong(1, user.getLocalId());
            session.executeQuery();
            if (session.nextResult()) {
                return session.getBoolean("ACTIVE");
            } else {
                return null;
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

    public User read(final Credentials credentials) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_BY_CREDENTIALS);
            session.setString(1, credentials.getUsername());
            session.setString(2, credentials.getPassword());
            session.setBoolean(3, Boolean.FALSE);
            session.executeQuery();
            if (session.nextResult()) {
                return extract(session);
            } else {
                return null;
            }
        } catch (final Throwable t) {
            throw translateError(session, t);
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

    /**
     * Read all features across all products for the user.
     * 
     * @param user
     *            A <code>User</code>.
     * @return A <code>List<Feature></code>.
     */
    public List<Feature> readFeatures(final User user) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_FEATURES);
            session.setLong(1, user.getLocalId());
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

    public List<Feature> readProductFeatures(final User user, final String name) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_PRODUCT_FEATURES);
            session.setLong(1, user.getLocalId());
            session.setString(2, name);
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
     * Open a user's vcard.
     * 
     * @param T
     *            A <code>UserVCardType</code>.
     * @param userId
     *            A user id <code>Long</code>.
     * @param vcard
     *            An instance of <code>T</code>.
     */
    public <T extends UserVCard> T readVCard(final Long userId, final T vcard,
            final VCardReader<T> reader) throws IOException {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_VCARD);
            session.setLong(1, userId);
            session.executeQuery();
            if (session.nextResult()) {
                return session.getVCard("VCARD", vcard, reader);
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

    /**
     * Read the verified e-mail address count for a user.
     * 
     * @param user
     *            A <code>User</code>.
     * @return An <code>Integer</code>.
     */
    public Integer readVerifiedEMailCount(final User user) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_EMAIL_COUNT);
            session.setLong(1, user.getLocalId());
            session.setBoolean(2, Boolean.TRUE);
            session.executeQuery();
            if (session.nextResult()) {
                return session.getInteger("EMAIL_COUNT");
            } else {
                return null;
            }
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Activate a list of profiles.
     * 
     * @param profileList
     *            A <code>List<Profile></code>.
     */
    public void updateActive(final List<Profile> profileList) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_UPDATE_ACTIVE);
            for (final Profile profile : profileList) {
                session.setBoolean(1, profile.isActive());
                session.setLong(2, profile.getLocalId());
                if (1 != session.executeUpdate()) {
                    throw panic("Could not update active.");
                }
            }
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Update the user features.
     * 
     * @param user
     *            A <code>User</code>.
     * @param featureList
     *            A <code>List<Feature></code>.
     */
    public void updateFeatures(final User user, final List<Feature> featureList) {
        final HypersonicSession session = openSession();
        try {
            final List<Feature> existingFeatureList = readFeatures(user);
            session.prepareStatement(SQL_DELETE_FEATURE_REL);
            session.setLong(1, user.getLocalId());
            for (final Feature feature : existingFeatureList) {
                session.setLong(2, feature.getFeatureId());
                if (1 != session.executeUpdate()) {
                    throw panic("Cannot delete feature.");
                }
            }

            session.prepareStatement(SQL_CREATE_FEATURE_REL);
            session.setLong(1, user.getLocalId());
            for (final Feature feature : featureList) {
                session.setLong(2, feature.getFeatureId());
                if (1 != session.executeUpdate()) {
                    throw panic("Cannot create feature.");
                }
            }
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Update a permanent record of an invitation for a user.
     * 
     * @param session
     *            A <code>HypersonicSession</code>.
     * @param user
     *            A <code>User</code>.
     * @param invitation
     *            A <code>ContactInvitation</code>.
     * @param acceptedOn
     *            A <code>Calendar</code>.
     */
    public void updateInvitation(final User user,
            final ContactInvitation invitation, final Calendar acceptedOn) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_UPDATE_INVITATION);
            session.setCalendar(1, acceptedOn);
            session.setLong(2, user.getLocalId());
            session.setLong(3, invitation.getCreatedBy().getLocalId());
            session.setCalendar(4, invitation.getCreatedOn());
            if (1 != session.executeUpdate()) {
                throw panic("Cannot update invitation.");
            }

            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Update the user's password.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param credentials
     *            A profile's <code>Credentials</code>.
     * @param newPassword
     *            A new password <code>String</code>.
     */
    public void updatePassword(final JabberId userId,
            final Credentials credentials, final String newPassword) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_UPDATE_PASSWORD);
            session.setString(1, newPassword);
            session.setString(2, credentials.getUsername());
            session.setString(3, credentials.getPassword());
            if (1 != session.executeUpdate())
                throw new HypersonicException("User password cannot be updated.");

            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Update a user/product release relationship.
     * 
     * @param user
     *            A <code>User</code>.
     * @param product
     *            A <code>Product</code>.
     * @param release
     *            A <code>Release</code>.
     */
    public void updateProductRelease(final User user, final Product product,
            final Release release) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_UPDATE_PRODUCT_RELEASE);
            session.setLong(1, release.getId());
            session.setLong(2, user.getLocalId());
            session.setLong(3, product.getId());
            if (1 != session.executeUpdate())
                throw panic("Could not update product release.");
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
     * @param T
     *            The <code>UserVCard</code> type.
     * @param userId
     *            The user id <code>Long</code>.
     * @param vcard
     *            The vcard <code>T</code>.
     */
    public <T extends UserVCard> void updateVCard(final Long userId,
            final T vcard, final VCardWriter<T> vcardWriter) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_UPDATE_VCARD);
            session.setVCard(1, vcard, vcardWriter);
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
     * Create a permanent record of an invitation for a user.
     * 
     * @param session
     *            A <code>HypersonicSession</code>.
     * @param user
     *            A <code>User</code>.
     * @param invitation
     *            A <code>ContactInvitation</code>.
     */
    void createInvitation(final HypersonicSession session, final User user,
            final ContactInvitation invitation) {
        session.prepareStatement(SQL_CREATE_INVITATION);
        session.setLong(1, user.getLocalId());
        session.setLong(2, invitation.getCreatedBy().getLocalId());
        session.setCalendar(3, invitation.getCreatedOn());
        if (1 != session.executeUpdate()) {
            throw panic("Cannot create invitation.");
        }
    }

    /**
     * Create a permanent record of an invitation for a user.
     * 
     * @param session
     *            A <code>HypersonicSession</code>.
     * @param user
     *            A <code>User</code>.
     * @param invitation
     *            A <code>ContactInvitation</code>.
     */
    void deleteInvitation(final HypersonicSession session, final User user,
            final ContactInvitation invitation) {
        session.prepareStatement(SQL_DELETE_INVITATION);
        session.setLong(1, user.getLocalId());
        session.setLong(2, invitation.getCreatedBy().getLocalId());
        session.setCalendar(3, invitation.getCreatedOn());
        final int rowCount = session.executeUpdate();
        if (0 != rowCount && 1 != rowCount) {
            throw panic("Cannot delete invitation.");
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
        feature.setName(Feature.Name.valueOf(session.getString("FEATURE_NAME")));
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
