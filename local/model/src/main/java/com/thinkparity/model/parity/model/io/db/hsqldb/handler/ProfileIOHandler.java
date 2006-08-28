/*
 * Created On: Jul 17, 2006 12:41:13 PM
 */
package com.thinkparity.model.parity.model.io.db.hsqldb.handler;

import java.util.ArrayList;
import java.util.List;

import com.thinkparity.codebase.VCardBuilder;

import com.thinkparity.model.parity.model.io.db.hsqldb.HypersonicException;
import com.thinkparity.model.parity.model.io.db.hsqldb.Session;
import com.thinkparity.model.parity.model.profile.Profile;
import com.thinkparity.model.profile.ProfileEMail;
import com.thinkparity.model.xmpp.JabberId;

/**
 * @author raymond@thinkparity.com
 * @version
 */
public class ProfileIOHandler extends AbstractIOHandler implements
        com.thinkparity.model.parity.model.io.handler.ProfileIOHandler {

    /** Sql to create a profile. */
    private static final String SQL_CREATE =
            new StringBuffer("insert into PROFILE ")
            .append("(PROFILE_ID,PROFILE_VCARD) ")
            .append("values (?,?)")
            .toString();

    /** Sql to create an e-mail address. */
    private static final String SQL_CREATE_EMAIL =
            new StringBuffer("insert into PROFILE_EMAIL_REL ")
            .append("(PROFILE_ID,EMAIL_ID,VERIFIED) ")
            .append("values (?,?,?)")
            .toString();

    /** Sql to delete an email. */
    private static final String SQL_DELETE_EMAIL =
            new StringBuffer("delete from ")
            .append("PROFILE_EMAIL_REL ")
            .append("where PROFILE_ID=? and EMAIL_ID=?")
            .toString();

    /** Sql to read the profile by jabber id. */
    private static final String SQL_READ =
            new StringBuffer("select P.PROFILE_ID,U.JABBER_ID,U.NAME,")
            .append("U.ORGANIZATION,U.TITLE,P.PROFILE_VCARD ")
            .append("from PROFILE P ")
            .append("inner join USER U on P.PROFILE_ID=U.USER_ID ")
            .append("where U.JABBER_ID=?")
            .toString();

    /** Sql to read a profile email. */
    private static final String SQL_READ_EMAIL =
            new StringBuffer("select P.PROFILE_ID,E.EMAIL_ID,E.EMAIL,")
            .append("PER.VERIFIED ")
            .append("from PROFILE_EMAIL_REL PER ")
            .append("inner join PROFILE P on PER.PROFILE_ID=P.PROFILE_ID ")
            .append("inner join EMAIL E ON PER.EMAIL_ID=E.EMAIL_ID ")
            .append("where P.PROFILE_ID=? AND E.EMAIL_ID=?")
            .toString();

    /** Sql to read the e-mail addresses. */
    private static final String SQL_READ_EMAILS =
            new StringBuffer("select P.PROFILE_ID,E.EMAIL_ID,E.EMAIL,")
            .append("PER.VERIFIED ")
            .append("from PROFILE_EMAIL_REL PER ")
            .append("inner join PROFILE P on PER.PROFILE_ID=P.PROFILE_ID ")
            .append("inner join EMAIL E on PER.EMAIL_ID=E.EMAIL_ID ")
            .append("where P.PROFILE_ID=?")
            .toString();

    /** Sql to update a profile. */
    private static final String SQL_UPDATE =
            new StringBuffer("update PROFILE ")
            .append("set PROFILE_VCARD=? ")
            .append("where PROFILE_ID=?")
            .toString();

    private static StringBuffer getApiId(final String api) {
        return getIOId("[PROFILE]").append(" ").append(api);
    }

    private static String getErrorId(final String api, final String error) {
        return getApiId(api).append(" ").append(error).toString();
    }

    /** Email db io. */
    private final EmailIOHandler emailIO;

    /** User db io. */
    private final UserIOHandler userIO;

    /** Create ProfileIOHandler. */
    public ProfileIOHandler() {
        super();
        this.userIO = new UserIOHandler();
        this.emailIO = new EmailIOHandler();
    }

    /**
     * @see com.thinkparity.model.parity.model.io.handler.ProfileIOHandler#create(com.thinkparity.model.parity.model.profile.Profile)
     */
    public void create(final Profile profile, final List<ProfileEMail> emails) {
        final Session session = openSession();
        try {
            userIO.create(session, profile);
            session.prepareStatement(SQL_CREATE);
            session.setLong(1, profile.getLocalId());
            session.setString(2, profile.getVCard().toXML());
            if(1 != session.executeUpdate())
                throw new HypersonicException(getErrorId("[CREATE]", "[CANNOT CREATE PROFILE]"));
            session.commit();
        }
        catch(final HypersonicException hx) {
            session.rollback();
            throw hx;
        }
        finally { session.close(); }
    }

    /**
     * @see com.thinkparity.model.parity.model.io.handler.ProfileIOHandler#createEmail(com.thinkparity.model.profile.ProfileEMail)
     */
    public void createEmail(final ProfileEMail email) {
        final Session session = openSession();
        try {
            final Long emailId = emailIO.create(session, email.getEmail());
            email.setEmailId(emailId);

            session.prepareStatement(SQL_CREATE_EMAIL);
            session.setLong(1, email.getProfileId());
            session.setLong(2, email.getEmailId());
            session.setBoolean(3, email.isVerified());
            if (1 != session.executeUpdate())
                throw new HypersonicException("COULD NOT CREATE EMAIL");

            session.commit();
        } catch (final HypersonicException hx) {
            session.rollback();
            throw hx;
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.model.parity.model.io.handler.ProfileIOHandler#deleteEmail(java.lang.Long)
     */
    public void deleteEmail(final Long profileId, final Long emailId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_DELETE_EMAIL);
            session.setLong(1, profileId);
            session.setLong(2, emailId);
            if (1 != session.executeUpdate())
                throw new HypersonicException("COULD NOT DELETE EMAIL");
            emailIO.delete(session, emailId);
            session.commit();
        } catch (final HypersonicException hx) {
            session.rollback();
            throw hx;
        } finally { 
            session.close();
        }
    }

    /**
     * @see com.thinkparity.model.parity.model.io.handler.ProfileIOHandler#read()
     */
    public Profile read(final JabberId jabberId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ);
            session.setQualifiedUsername(1, jabberId);
            session.executeQuery();
            if(session.nextResult()) { return extractProfile(session); }
            else { return null; }
        }
        finally { session.close(); }
    }

    /**
     * @see com.thinkparity.model.parity.model.io.handler.ProfileIOHandler#readEmail(java.lang.Long, java.lang.Long)
     */
    public ProfileEMail readEmail(final Long profileId, final Long emailId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_EMAIL);
            session.setLong(1, profileId);
            session.setLong(2, emailId);
            session.executeQuery();
            if (session.nextResult()) {
                return extractEMail(session);
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

    public List<ProfileEMail> readEmails(final Long profileId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_EMAILS);
            session.setLong(1, profileId);
            session.executeQuery();
            final List<ProfileEMail> emails = new ArrayList<ProfileEMail>();
            while(session.nextResult()) {
                emails.add(extractEMail(session));
            }
            return emails;
        }
        finally { session.close(); }
    }

    /**
     * @see com.thinkparity.model.parity.model.io.handler.ProfileIOHandler#update(com.thinkparity.model.parity.model.profile.Profile)
     */
    public void update(final Profile profile) {
        final Session session = openSession();
        try {
            userIO.update(session, profile);

            session.prepareStatement(SQL_UPDATE);
            session.setString(1, profile.getVCard().toXML());
            session.setLong(2, profile.getLocalId());
            if(1 != session.executeUpdate())
                throw new HypersonicException(getErrorId("[UPDATE]", "[CANNOT UPDATE PROFILE]"));

            session.commit();
        }
        catch(final HypersonicException hx) {
            session.rollback();
            throw hx;
        }
        finally { session.close(); }

    }

    /**
     * Extract a profile email from the session.
     * 
     * @param session
     *            A database session.
     * @return A profile email.
     */
    ProfileEMail extractEMail(final Session session) {
        final ProfileEMail email = new ProfileEMail();
        email.setEmail(session.getEMail("EMAIL"));
        email.setEmailId(session.getLong("EMAIL_ID"));
        email.setProfileId(session.getLong("PROFILE_ID"));
        email.setVerified(session.getBoolean("VERIFIED"));
        return email;
    }

    /**
     * Extract the profile from the database session.
     * 
     * @param session
     *            A database session.
     * @return A profile.
     */
    Profile extractProfile(final Session session) {
        final Profile profile = new Profile();
        profile.setId(session.getQualifiedUsername("JABBER_ID"));
        profile.setLocalId(session.getLong("PROFILE_ID"));
        profile.setName(session.getString("NAME"));
        profile.setOrganization(session.getString("ORGANIZATION"));
        profile.setTitle(session.getString("TITLE"));
        profile.setVCard(VCardBuilder.createVCard(session.getString("PROFILE_VCARD")));
        return profile;
    }
}
