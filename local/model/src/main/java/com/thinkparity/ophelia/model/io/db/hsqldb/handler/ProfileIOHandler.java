/*
 * Created On: Jul 17, 2006 12:41:13 PM
 */
package com.thinkparity.ophelia.model.io.db.hsqldb.handler;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.migrator.Feature;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.ProfileEMail;
import com.thinkparity.codebase.model.profile.ProfileVCard;

import com.thinkparity.ophelia.model.io.db.hsqldb.HypersonicException;
import com.thinkparity.ophelia.model.io.db.hsqldb.Session;

/**
 * <b>Title:</b>thinkParity Ophelia Model Profile IO Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ProfileIOHandler extends AbstractIOHandler implements
        com.thinkparity.ophelia.model.io.handler.ProfileIOHandler {

    /** Sql to create a profile. */
    private static final String SQL_CREATE =
        new StringBuilder("insert into PROFILE ")
        .append("(PROFILE_ID,PROFILE_VCARD) ")
        .append("values (?,?)")
        .toString();

    /** Sql to create an e-mail address. */
    private static final String SQL_CREATE_EMAIL =
        new StringBuilder("insert into PROFILE_EMAIL_REL ")
        .append("(PROFILE_ID,EMAIL_ID,VERIFIED) ")
        .append("values (?,?,?)")
        .toString();

    /** Sql to create a profile feature. */
    private static final String SQL_CREATE_FEATURE =
        new StringBuilder("insert into PROFILE_FEATURE ")
        .append("(PROFILE_ID,FEATURE_NAME) ")
        .append("values (?,?)")
        .toString();

    /** Sql to delete profile. */
    private static final String SQL_DELETE =
        new StringBuilder("delete from PROFILE ")
        .append("where PROFILE_ID=?")
        .toString();

    /** Sql to delete an email. */
    private static final String SQL_DELETE_EMAIL =
        new StringBuilder("delete from ")
        .append("PROFILE_EMAIL_REL ")
        .append("where PROFILE_ID=? and EMAIL_ID=?")
        .toString();

    /** Sql to delete profile features. */
    private static final String SQL_DELETE_FEATURES =
        new StringBuilder("delete from PROFILE_FEATURE ")
        .append("where PROFILE_ID=?")
        .toString();

    /** Sql to read the profile. */
    private static final String SQL_READ =
        new StringBuilder("select P.PROFILE_ID,U.JABBER_ID,U.NAME,")
        .append("U.ORGANIZATION,U.TITLE,U.FLAGS,P.PROFILE_VCARD ")
        .append("from PROFILE P ")
        .append("inner join PARITY_USER U on P.PROFILE_ID=U.USER_ID ")
        .append("order by P.PROFILE_ID desc")
        .toString();

    /** Sql to read the total disk usage. */
    private static final String SQL_READ_DISK_USAGE =
        new StringBuilder("select DV.CONTENT_SIZE ")
        .append("from DOCUMENT_VERSION DV ")
        .append("order by DV.DOCUMENT_ID asc, DV.DOCUMENT_VERSION_ID asc")
        .toString();

    /** Sql to read a profile email. */
    private static final String SQL_READ_EMAIL =
        new StringBuilder("select P.PROFILE_ID,E.EMAIL_ID,E.EMAIL,")
        .append("PER.VERIFIED ")
        .append("from PROFILE_EMAIL_REL PER ")
        .append("inner join PROFILE P on PER.PROFILE_ID=P.PROFILE_ID ")
        .append("inner join EMAIL E ON PER.EMAIL_ID=E.EMAIL_ID ")
        .append("where P.PROFILE_ID=? AND E.EMAIL_ID=?")
        .toString();

    /** Sql to read the e-mail addresses. */
    private static final String SQL_READ_EMAILS =
        new StringBuilder("select P.PROFILE_ID,E.EMAIL_ID,E.EMAIL,")
        .append("PER.VERIFIED ")
        .append("from PROFILE_EMAIL_REL PER ")
        .append("inner join PROFILE P on PER.PROFILE_ID=P.PROFILE_ID ")
        .append("inner join EMAIL E on PER.EMAIL_ID=E.EMAIL_ID ")
        .append("where P.PROFILE_ID=?")
        .toString();

    /** Sql to read the profile features. */
    private static final String SQL_READ_FEATURES =
        new StringBuilder("select PF.FEATURE_ID,PF.FEATURE_NAME ")
        .append("from PROFILE_FEATURE PF ")
        .append("inner join PROFILE P on PF.PROFILE_ID=P.PROFILE_ID ")
        .append("where P.PROFILE_ID=?")
        .toString();

    /** Sql to read the profile by its unique key. */
    private static final String SQL_READ_UK =
        new StringBuilder("select P.PROFILE_ID,U.JABBER_ID,U.NAME,")
        .append("U.ORGANIZATION,U.TITLE,U.FLAGS,P.PROFILE_VCARD ")
        .append("from PROFILE P ")
        .append("inner join PARITY_USER U on P.PROFILE_ID=U.USER_ID ")
        .append("where U.JABBER_ID=?")
        .toString();

    /** Sql to update a profile. */
    private static final String SQL_UPDATE =
        new StringBuilder("update PROFILE ")
        .append("set PROFILE_VCARD=? ")
        .append("where PROFILE_ID=?")
        .toString();

    /** Sql to verify a profile email. */
    private static final String SQL_VERIFY_EMAIL =
        new StringBuilder("update PROFILE_EMAIL_REL ")
        .append("set VERIFIED=? ")
        .append("where PROFILE_ID=? and EMAIL_ID=?")
        .toString();

    /** Email db io. */
    private final EmailIOHandler emailIO;

    /** User db io. */
    private final UserIOHandler userIO;

    /**
     * Create ProfileIOHandler.
     * 
     * @param dataSource
     *            An sql <code>DataSource</code>.
     */
    public ProfileIOHandler(final DataSource dataSource) {
        super(dataSource);
        this.userIO = new UserIOHandler(dataSource);
        this.emailIO = new EmailIOHandler(dataSource);
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ProfileIOHandler#create(com.thinkparity.codebase.model.profile.Profile)
     *
     */
    public void create(final Profile profile) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_CREATE);
            session.setLong(1, profile.getLocalId());
            session.setVCard(2, profile.getVCard());
            if(1 != session.executeUpdate())
                throw translateError("Could not create user profile {0}.", profile);

            session.prepareStatement(SQL_CREATE_FEATURE);
            session.setLong(1, profile.getLocalId());
            for (final Feature feature : profile.getFeatures()) {
                session.setString(2, feature.getName());
                if (1 != session.executeUpdate())
                    throw translateError(
                            "Could not create feature {0} for profile {1}.",
                            feature, profile);
            }

            userIO.update(session, profile);
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ProfileIOHandler#createEmail(com.thinkparity.codebase.model.profile.ProfileEMail)
     */
    public void createEmail(final Long profileId, final ProfileEMail email) {
        final Session session = openSession();
        try {
            final Long emailId = emailIO.create(session, email.getEmail());
            email.setEmailId(emailId);

            session.prepareStatement(SQL_CREATE_EMAIL);
            session.setLong(1, profileId);
            session.setLong(2, email.getEmailId());
            session.setBoolean(3, email.isVerified());
            if (1 != session.executeUpdate())
                throw new HypersonicException("COULD NOT CREATE EMAIL");
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ProfileIOHandler#delete(com.thinkparity.codebase.model.profile.Profile)
     *
     */
    @Override
    public void delete(final Profile profile) {
        final Session session = openSession();
        try {
            final int featureCount = profile.getFeatures().size();
            session.prepareStatement(SQL_DELETE_FEATURES);
            session.setLong(1, profile.getLocalId());
            if (featureCount != session.executeUpdate()) {
                throw new HypersonicException("Could not delete profile features.");
            }
            
            session.prepareStatement(SQL_DELETE);
            session.setLong(1, profile.getLocalId());
            if (1 != session.executeUpdate())
                throw new HypersonicException("Could not delete profile.");
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ProfileIOHandler#deleteEmail(java.lang.Long)
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
        } finally { 
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ProfileIOHandler#read()
     * 
     */
    public Profile read(final JabberId jabberId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_UK);
            session.setQualifiedUsername(1, jabberId);
            session.executeQuery();
            if (session.nextResult()) {
                return extractProfile(session);
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ProfileIOHandler#readDisUsage()
     *
     */
    public Long readDiskUsage() {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_DISK_USAGE);
            session.executeQuery();
            long diskUsage = 0;
            while (session.nextResult()) {
                diskUsage += session.getLong("CONTENT_SIZE").longValue();
            }
            return Long.valueOf(diskUsage);
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ProfileIOHandler#readEmail(java.lang.Long, java.lang.Long)
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
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ProfileIOHandler#readFeatures(java.lang.Long)
     * 
     */
    public List<Feature> readFeatures(final Long profileId) {
        final Session session = openSession();
        try {
            return readFeatures(session, profileId);
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ProfileIOHandler#update(com.thinkparity.codebase.model.profile.Profile)
     * 
     */
    public void update(final Profile profile) {
        final Session session = openSession();
        try {
            userIO.update(session, profile);

            session.prepareStatement(SQL_UPDATE);
            session.setVCard(1, profile.getVCard());
            session.setLong(2, profile.getLocalId());
            if(1 != session.executeUpdate())
                throw new HypersonicException("Could not update profile.");
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ProfileIOHandler#verifyEmail(java.lang.Long, java.lang.Long)
     */
    public void verifyEmail(final Long profileId, final Long emailId,
            final Boolean verified) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_VERIFY_EMAIL);
            session.setBoolean(1, verified);
            session.setLong(2, profileId);
            session.setLong(3, emailId);
            if (1 != session.executeUpdate())
                throw new HypersonicException("COULD NOT VERIFY EMAIL");
        } finally {
            session.close();
        }
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
        profile.setVCard(session.getVCard("PROFILE_VCARD", new ProfileVCard()));
        profile.setFlags(session.getUserFlags("FLAGS"));
        profile.setId(session.getQualifiedUsername("JABBER_ID"));
        profile.setLocalId(session.getLong("PROFILE_ID"));
        profile.setName(session.getString("NAME"));
        profile.setOrganization(session.getString("ORGANIZATION"));
        profile.setTitle(session.getString("TITLE"));

        profile.setFeatures(readFeatures(session, profile.getLocalId()));
        return profile;
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ProfileIOHandler#read()
     * 
     */
    Profile read(final Session session) {
        session.prepareStatement(SQL_READ);
        session.executeQuery();
        if (session.nextResult()) {
            final Profile profile = extractProfile(session);
            if (session.nextResult()) {
                // cheap way to ensure only one row
                throw new HypersonicException("Invalid profile state.");
            } else {
                return profile;
            }
        } else {
            return null;
        }
    }


    /**
     * Extract the feature from the session.
     * 
     * @param session
     *            A <code>Session</code>.
     * @return A <code>Feature</code>.
     */
    private Feature extractFeature(final Session session) {
        final Feature feature = new Feature();
        feature.setFeatureId(session.getLong("FEATURE_ID"));
        feature.setName(session.getString("FEATURE_NAME"));
        return feature;
    }

    /**
     * Read the profile features.
     * 
     * @param session
     *            A <code>Session</code>.
     * @param profileId
     *            A <code>Long</code>.
     * @return A <code>List<Feature></code>.
     */
    private List<Feature> readFeatures(final Session session, final Long profileId) {
        session.prepareStatement(SQL_READ_FEATURES);
        session.setLong(1, profileId);
        session.executeQuery();
        final List<Feature> features = new ArrayList<Feature>();
        while (session.nextResult()) {
            features.add(extractFeature(session));
        }
        return features;
    }
}
