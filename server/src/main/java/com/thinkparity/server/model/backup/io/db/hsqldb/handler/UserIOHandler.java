/*
 * Apr 20, 2006
 */
package com.thinkparity.ophelia.model.io.db.hsqldb.handler;

import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.user.UserFlag;

import com.thinkparity.ophelia.model.io.db.hsqldb.HypersonicException;
import com.thinkparity.ophelia.model.io.db.hsqldb.Session;

/**
 * <b>Title:</b>thinkParity OpheliaModel User IO<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.10
 */
public final class UserIOHandler extends AbstractIOHandler implements
        com.thinkparity.ophelia.model.io.handler.UserIOHandler {

    /** Sql to create a user. */
    private static final String SQL_CREATE =
        new StringBuilder("insert into PARITY_USER ")
        .append("(JABBER_ID,NAME,ORGANIZATION,TITLE,FLAGS) ")
        .append("values (?,?,?,?,?)")
        .toString();

    /** Sql to read a user by their jabber id. */
    private static final String SQL_READ_BY_JABBER_ID =
        new StringBuilder("select U.USER_ID,U.JABBER_ID,U.NAME,")
        .append("U.ORGANIZATION,U.TITLE,U.FLAGS ")
        .append("from PARITY_USER U ")
        .append("where U.JABBER_ID=?")
        .toString();

    private static final String SQL_READ_BY_USER_ID =
        new StringBuilder("select U.USER_ID,U.JABBER_ID,U.NAME,")
        .append("U.ORGANIZATION,U.TITLE,U.FLAGS ")
        .append("from PARITY_USER U ")
        .append("where U.USER_ID=?")
        .toString();

    /** Sql to read the user flags. */
    private static final String SQL_READ_FLAGS =
        new StringBuilder("select PU.FLAGS ")
        .append("from PARITY_USER PU ")
        .append("where PU.USER_ID=?")
        .toString();

    /** Sql to read a user's local user id. */
    private static final String SQL_READ_LOCAL_ID =
        new StringBuilder("select U.USER_ID ")
        .append("from PARITY_USER U ")
        .append("where U.JABBER_ID=?")
        .toString();

    /** Sql to read a user's user id. */
    private static final String SQL_READ_USER_ID =
        new StringBuilder("select U.JABBER_ID ")
        .append("from PARITY_USER U ")
        .append("where U.USER_ID=?")
        .toString();

    /** Sql to update a user. */
    private static final String SQL_UPDATE =
        new StringBuilder("update PARITY_USER ")
        .append("set NAME=?,ORGANIZATION=?,TITLE=?,FLAGS=? ")
        .append("where USER_ID=?")
        .toString();

    /** Sql to update the user flags. */
    private static final String SQL_UPDATE_FLAGS =
        new StringBuilder("update PARITY_USER ")
        .append("set FLAGS=? where USER_ID=?")
        .toString();

    /**
     * Create a UserIOHandler.
     * 
     * @param dataSource
     *            An sql <code>DataSource</code>.
     */
    public UserIOHandler(final DataSource dataSource) {
        super(dataSource);
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.UserIOHandler#applyFlags(java.lang.Long, java.util.List)
     *
     */
    public void applyFlags(final Long userId, final List<UserFlag> flags) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_UPDATE_FLAGS);
            session.setUserFlags(1, flags);
            session.setLong(2, userId);
            if (1 != session.executeUpdate())
                throw new HypersonicException("Could not update user flags.");
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.UserIOHandler#create(com.thinkparity.codebase.model.user.User)
     * 
     */
    public void create(final User user) {
        final Session session = openSession();
        try {
            create(session, user);
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.UserIOHandler#read(com.thinkparity.codebase.jabber.JabberId)
     */
    public User read(final JabberId jabberId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_BY_JABBER_ID);
            session.setQualifiedUsername(1, jabberId);
            session.executeQuery();

            if (session.nextResult()) {
                return extractUser(session);
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.UserIOHandler#read(java.lang.Long)
     */
    public User read(final Long userId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_BY_USER_ID);
            session.setLong(1, userId);
            session.executeQuery();

            if (session.nextResult()) {
                return extractUser(session);
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.UserIOHandler#readFlags(java.lang.Long)
     *
     */
    public List<UserFlag> readFlags(final Long userId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_FLAGS);
            session.setLong(1, userId);
            session.executeQuery();
            if (session.nextResult()) {
                return session.getUserFlags("FLAGS");
            } else {
                return Collections.emptyList();
            }
        } finally {
            session.close();
        }
    }

    /**
     * Create the user.
     * 
     * @param session
     *            A <code>Session</code>.
     * @param user
     *            A <code>User</code>.
     * @param features
     *            A <code>List</code> of <code>Feature</code>s.
     */
    void create(final Session session, final User user) {
        session.prepareStatement(SQL_CREATE);
        session.setQualifiedUsername(1, user.getId());
        session.setString(2, user.getName());
        session.setString(3, user.getOrganization());
        session.setString(4, user.getTitle());
        session.setUserFlags(5, user.getFlags());
        if(1 != session.executeUpdate())
            throw translateError("Could not create user {0}.", user);

        user.setLocalId(session.getIdentity("PARITY_USER"));
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
        u.setFlags(session.getUserFlags("FLAGS"));
        u.setId(session.getQualifiedUsername("JABBER_ID"));
        u.setLocalId(session.getLong("USER_ID"));
        u.setName(session.getString("NAME"));
        u.setOrganization(session.getString("ORGANIZATION"));
        u.setTitle(session.getString("TITLE"));
        return u;
    }

    /**
     * Read the local user id for the jabber id.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A local user id <code>Long</code>.
     */
    Long readLocalId(final Session session, final JabberId userId) {
        session.prepareStatement(SQL_READ_LOCAL_ID);
        session.setQualifiedUsername(1, userId);
        session.executeQuery();
        if (session.nextResult()) {
            return session.getLong("USER_ID");
        } else {
            return null;
        }
    }

    /**
     * Read the user id for the local id.
     * 
     * @param userId
     *            A user id <code>Long</code>.
     * @return A local user id <code>Long</code>.
     */
    JabberId readUserId(final Session session, final Long userId) {
        session.prepareStatement(SQL_READ_USER_ID);
        session.setLong(1, userId);
        session.executeQuery();
        if (session.nextResult()) {
            return session.getQualifiedUsername("JABBER_ID");
        } else {
            return null;
        }
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
        session.setUserFlags(4, user.getFlags());
        session.setLong(5, user.getLocalId());
        if (1 != session.executeUpdate())
            throw new HypersonicException("Could not update user.");
    }
}
