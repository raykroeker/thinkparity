/*
 * Created On: Jul 20, 2006 2:43:22 PM
 */
package com.thinkparity.desdemona.model.io.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.email.EMailBuilder;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.model.profile.VerificationKey;

import com.thinkparity.desdemona.model.io.hsqldb.HypersonicSession;
import com.thinkparity.desdemona.model.profile.Feature;


/**
 * @author raymond@thinkparity.com
 * @version
 */
public class UserSql extends AbstractSql {

    /** Sql to create an email address. */
    private static final String SQL_CREATE_EMAIL =
            new StringBuffer("insert into parityUserEmail ")
            .append("(username,email,verified,verificationKey) ")
            .append("values (?,?,?,?)")
            .toString();
        
    /** Sql to delete an email address. */
    private static final String SQL_DELETE_EMAIL =
            new StringBuffer("delete from PARITYUSEREMAIL ")
            .append("where USERNAME=? and EMAIL=?")
            .toString();

    /** Sql to read email addresses. */
    private static final String SQL_READ_EMAIL =
            new StringBuffer("select PUE.USERNAME,PUE.EMAIL,PUE.VERIFIED ")
            .append("from parityUserEmail PUE ")
            .append("inner join jiveUser JU on PUE.USERNAME = JU.USERNAME ")
            .append("where JU.USERNAME=? and PUE.EMAIL=? ")
            .append("and PUE.VERIFICATIONKEY=?")
            .toString();

    /** Sql to read e-mail addresses. */
    private static final String SQL_READ_EMAILS =
            new StringBuffer("select PUE.USERNAME,PUE.EMAIL,PUE.VERIFIED ")
            .append("from parityUserEmail PUE ")
            .append("inner join jiveUser JU on PUE.USERNAME = JU.USERNAME ")
            .append("where JU.USERNAME=? and PUE.VERIFIED=?")
            .toString();

    /** Read all custom features for the user. */
    private static final String SQL_READ_FEATURES =
            new StringBuffer("select PF.FEATURE_ID,PF.FEATURE_NAME ")
            .append("from jiveUser JU ")
            .append("inner join PARITY_USER_FEATURE_REL PUFR on JU.USERNAME=PUFR.USERNAME ")
            .append("inner join PARITY_FEATURE PF on PUFR.FEATURE_ID=PF.FEATURE_ID ")
            .append("where JU.USERNAME=?")
            .toString();

    /** Sql to read the user profile's security answer. */
    private static final String SQL_READ_PROFILE_SECURITY_ANSWER =
            new StringBuffer("select PUP.SECURITYANSWER ")
            .append("from PARITYUSERPROFILE PUP ")
            .append("where PUP.USERNAME=?")
            .toString();

    /** Sql to read the user profile's security question. */
    private static final String SQL_READ_PROFILE_SECURITY_QUESTION =
            new StringBuffer("select PUP.SECURITYQUESTION ")
            .append("from PARITYUSERPROFILE PUP ")
            .append("where PUP.USERNAME=?")
            .toString();

    /** Sql to read a username from an e-mail address. */
    private static final String SQL_READ_USERNAME =
            new StringBuffer("select JU.USERNAME ")
            .append("from parityUserEmail PUE ")
            .append("inner join jiveUser JU on PUE.USERNAME=JU.USERNAME ")
            .append("where PUE.EMAIL=?")
            .toString();

    private static final String SQL_VERIFY_EMAIL =
            new StringBuffer("update PARITYUSEREMAIL ")
            .append("set VERIFIED=?, VERIFICATIONKEY=?")
            .append("where USERNAME=? and EMAIL=? and VERIFICATIONKEY=?")
            .toString();

    /** Create UserSql. */
    public UserSql() { super(); }

    public void createEmail(final JabberId userId, final EMail email,
            final VerificationKey key) throws SQLException {
        Connection cx = null;
        PreparedStatement ps = null;
        try {
            cx = getCx();
            logStatement(SQL_CREATE_EMAIL);
            ps = cx.prepareStatement(SQL_CREATE_EMAIL);
            ps.setString(1, userId.getUsername());
            ps.setString(2, email.toString());
            ps.setBoolean(3, Boolean.FALSE);
            ps.setString(4, key.getKey());
            if (1 != ps.executeUpdate())
                throw new SQLException("COULD NOT CREATE EMAIL");
            cx.commit();
        } finally {
           close(cx, ps);
        }
    }

    public void deleteEmail(final JabberId userId, final EMail email)
            throws SQLException {
        Connection cx = null;
        PreparedStatement ps = null;
        try {
            cx = getCx();
            ps = cx.prepareStatement(SQL_DELETE_EMAIL);
            ps.setString(1, userId.getUsername());
            ps.setString(2, email.toString());
            if (1 != ps.executeUpdate())
                throw new SQLException("COULD NOT DELETE EMAIL");
            cx.commit();
        } finally {
           close(cx, ps);
        }
    }

    public EMail readEmail(final JabberId userId, final EMail email,
            final VerificationKey key) throws SQLException {
        Connection cx = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cx = getCx();
            ps = cx.prepareStatement(SQL_READ_EMAIL);
            ps.setString(1, userId.getUsername());
            ps.setString(2, email.toString());
            ps.setString(3, key.getKey());
            rs = ps.executeQuery();

            if (rs.next()) {
                return EMailBuilder.parse(rs.getString("EMAIL"));
            } else {
                return null;
            }
        } finally {
            close(cx, ps, rs);
        }
    }

    public List<EMail> readEmails(final JabberId jabberId,
            final Boolean verified) throws SQLException {
        Connection cx = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cx = getCx();
            ps = cx.prepareStatement(SQL_READ_EMAILS);
            ps.setString(1, jabberId.getUsername());
            ps.setBoolean(2, verified);
            rs = ps.executeQuery();
            final List<EMail> emails = new ArrayList<EMail>();
            while (rs.next()) {
                emails.add(extractEMail(rs));
            }
            return emails;
        }
        finally { close(cx, ps, rs); }
    }

    public List<Feature> readFeatures(final JabberId userId) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_FEATURES);
            session.setString(1, userId.getUsername());
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

    Feature extractFeature(final HypersonicSession session) {
        final Feature feature = new Feature();
        feature.setId(session.getLong("FEATURE_ID"));
        feature.setName(session.getString("FEATURE_NAME"));
        return feature;
    }

    /**
     * Read the user profile's security answer.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return The security question answer <code>String</code>.
     * @throws SQLException
     */
    public String readProfileSecurityAnswer(final JabberId userId)
            throws SQLException {
        Connection cx = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cx = getCx();
            ps = cx.prepareStatement(SQL_READ_PROFILE_SECURITY_ANSWER);
            ps.setString(1, userId.getUsername());
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("SECURITYANSWER");
            } else {
                return null;
            }
        } finally {
            close(cx, ps, rs);
        }
    }

    /**
     * Read the user profile's security answer.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return The security question answer <code>String</code>.
     * @throws SQLException
     */
    public String readProfileSecurityQuestion(final JabberId userId)
            throws SQLException {
        Connection cx = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cx = getCx();
            ps = cx.prepareStatement(SQL_READ_PROFILE_SECURITY_QUESTION);
            ps.setString(1, userId.getUsername());
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("SECURITYQUESTION");
            } else {
                return null;
            }
        } finally {
            close(cx, ps, rs);
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
        Connection cx = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cx = getCx();
            logStatement(SQL_READ_USERNAME);
            ps = cx.prepareStatement(SQL_READ_USERNAME);
            set(ps, 1, email.toString());
            rs = ps.executeQuery();
            if(rs.next()) {
                return rs.getString("USERNAME");
            } else {
                return null;
            }
        } finally {
            close(cx, ps, rs);
        }
    }

    public void verifyEmail(final JabberId userId, final EMail email,
            final VerificationKey key) throws SQLException {
        Connection cx = null;
        PreparedStatement ps = null;
        try {
            cx = getCx();
            ps = cx.prepareStatement(SQL_VERIFY_EMAIL);
            ps.setBoolean(1, Boolean.TRUE);
            ps.setString(2, null);
            ps.setString(3, userId.getUsername());
            ps.setString(4, email.toString());
            ps.setString(5, key.getKey());
            if (1 != ps.executeUpdate())
                throw new SQLException("COULD NOT VERIFY EMAIL");
            cx.commit();
        } catch(final SQLException sqlx) {
            cx.rollback();
            throw sqlx;
        } finally {
            close(cx, ps);
        }
    }

    EMail extractEMail(final ResultSet rs) throws SQLException {
        return EMailBuilder.parse(rs.getString("EMAIL"));
    }
}
