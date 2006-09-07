/*
 * Apr 20, 2006
 */
package com.thinkparity.model.parity.model.io.db.hsqldb.handler;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.model.parity.model.io.db.hsqldb.HypersonicException;
import com.thinkparity.model.parity.model.io.db.hsqldb.Session;
import com.thinkparity.model.xmpp.user.User;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class UserIOHandler extends AbstractIOHandler implements
        com.thinkparity.model.parity.model.io.handler.UserIOHandler {

    private static final String SQL_CREATE =
        new StringBuffer("insert into USER ")
        .append("(JABBER_ID,NAME,ORGANIZATION,TITLE) ")
        .append("values (?,?,?,?)")
        .toString();

    /** Sql to update a user. */
    private static final String SQL_UPDATE =
            new StringBuffer("update USER ")
            .append("set NAME=?,ORGANIZATION=?,TITLE=? ")
            .append("where USER_ID=?")
            .toString();

    /** Sql to read a user by their jabber id. */
    private static final String SQL_READ_BY_JABBER_ID =
        new StringBuffer("select U.USER_ID,U.JABBER_ID,U.NAME,")
        .append("U.ORGANIZATION,U.TITLE ")
        .append("from USER U ")
        .append("where U.JABBER_ID=?")
        .toString();

    private static final String SQL_READ_BY_USER_ID =
        new StringBuffer("select U.USER_ID,U.JABBER_ID,U.NAME,")
        .append("U.ORGANIZATION,U.TITLE ")
        .append("from USER U ")
        .append("where U.USER_ID=?")
        .toString();

    /**
     * Obtain an api id.
     * 
     * @param api
     *            An api.
     * @return An error id.
     */
    private static StringBuffer getApiId(final String api) {
        return getIOId("USER").append(" ").append(api);
    }

    /**
     * Obtain an error id.
     * 
     * @param api
     *            An api.
     * @param error
     *            An error.
     * @return An error id.
     */
    private static String getErrorId(final String api, final String error) {
        return getApiId(api).append(" ").append(error).toString();
    }

    /**
     * Create a UserIOHandler.
     */
    public UserIOHandler() { super(); }

    /**
     * @see com.thinkparity.model.parity.model.io.handler.UserIOHandler#create(com.thinkparity.model.xmpp.user.User)
     */
    public void create(final User user) {
        final Session session = openSession();
        try {
            create(session, user);
            session.commit();
        }
        catch(final HypersonicException hx) {
            session.rollback();
            throw hx;
        }
        finally { session.close(); }
    }

    /**
     * @see com.thinkparity.model.parity.model.io.handler.UserIOHandler#read(com.thinkparity.codebase.jabber.JabberId)
     */
    public User read(final JabberId jabberId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_BY_JABBER_ID);
            session.setQualifiedUsername(1, jabberId);
            session.executeQuery();

            if(session.nextResult()) { return extractUser(session); }
            else { return null; }
        }
        catch(final HypersonicException hx) {
            session.rollback();
            throw hx;
        }
        finally { session.close(); }
    }

    /**
     * @see com.thinkparity.model.parity.model.io.handler.UserIOHandler#read(java.lang.Long)
     */
    public User read(final Long userId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_BY_USER_ID);
            session.setLong(1, userId);
            session.executeQuery();

            if(session.nextResult()) { return extractUser(session); }
            else { return null; }
        }
        catch(final HypersonicException hx) {
            session.rollback();
            throw hx;
        }
        finally { session.close(); }
    }

    /**
     * Update a user.
     * 
     * @param session
     *            A database <code>Session</code>.
     * @param user
     *            A <code>User</code>.
     */
    void update(final Session session, final User user) {
        session.prepareStatement(SQL_UPDATE);
        session.setString(1, user.getName());
        session.setString(2, user.getOrganization());
        session.setString(3, user.getTitle());
        session.setLong(4, user.getLocalId());
        if (1 != session.executeUpdate())
            throw new HypersonicException(getErrorId("UPDATE", "CANNOT UPDATE USER"));
    }

    /**
     * Create a user.
     * 
     * @param session
     *            A database session.
     * @param user
     *            A user.
     */
    void create(final Session session, final User user) {
        session.prepareStatement(SQL_CREATE);
        session.setQualifiedUsername(1, user.getId());
        session.setString(2, user.getName());
        session.setString(3, user.getOrganization());
        session.setString(4, user.getTitle());
        if(1 != session.executeUpdate())
            throw new HypersonicException(getErrorId("[CREATE]", "[COULD NOT CREATE USER]"));
        user.setLocalId(session.getIdentity());
    }

    /**
     * Extract a user from a database session.  The fields required are:<ul>
     * <li>JABBER_ID - <code>JabberId</code>
     * <li>USER_ID - <code>Long</code>
     * <li>NAME - <code>String</code>
     * <li>ORGANIZATION - <code>String</code>
     * <li>TITLE - <code>String</code></ul>
     * 
     * @param session
     *            The database session.
     * @return A user.
     */
    User extractUser(final Session session) {
        final User u = new User();
        u.setId(session.getQualifiedUsername("JABBER_ID"));
        u.setLocalId(session.getLong("USER_ID"));
        u.setName(session.getString("NAME"));
        u.setOrganization(session.getString("ORGANIZATION"));
        u.setTitle(session.getString("TITLE"));
        return u;
    }
}
