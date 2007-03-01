/*
 * Apr 20, 2006
 */
package com.thinkparity.ophelia.model.io.db.hsqldb.handler;

import java.util.ArrayList;
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
        new StringBuffer("insert into PARITY_USER ")
        .append("(JABBER_ID,NAME,ORGANIZATION,TITLE) ")
        .append("values (?,?,?,?)")
        .toString();

    /** Sql to create a user flag. */
    private static final String SQL_CREATE_FLAG =
        new StringBuffer("insert into USER_FLAG_REL")
        .append("(USER_ID,USER_FLAG_ID) ")
        .append("values(?,?)")
        .toString();

    /** Sql to delete all user flags. */
    private static final String SQL_DELETE_FLAGS =
        new StringBuffer("delete from USER_FLAG_REL ")
        .append("where USER_ID=?")
        .toString();

    /** Sql to read a user by their jabber id. */
    private static final String SQL_READ_BY_JABBER_ID =
        new StringBuffer("select U.USER_ID,U.JABBER_ID,U.NAME,")
        .append("U.ORGANIZATION,U.TITLE ")
        .append("from PARITY_USER U ")
        .append("where U.JABBER_ID=?")
        .toString();

    private static final String SQL_READ_BY_USER_ID =
        new StringBuffer("select U.USER_ID,U.JABBER_ID,U.NAME,")
        .append("U.ORGANIZATION,U.TITLE ")
        .append("from PARITY_USER U ")
        .append("where U.USER_ID=?")
        .toString();

    /** Sql to read the user flag count. */
    private static final String SQL_READ_FLAG_COUNT =
        new StringBuffer("select COUNT(USER_FLAG_ID) \"USER_FLAG_COUNT\" ")
        .append("from USER_FLAG_REL UFR ")
        .append("where UFR.USER_ID=?")
        .toString();

    /** Sql to read the user flags. */
    private static final String SQL_READ_FLAGS =
        new StringBuffer("select UFR.USER_FLAG_ID ")
        .append("from USER_FLAG_REL UFR ")
        .append("where UFR.USER_ID=?")
        .toString();

    /** Sql to read a user's local user id. */
    private static final String SQL_READ_LOCAL_ID =
        new StringBuffer("select U.USER_ID ")
        .append("from PARITY_USER U ")
        .append("where U.JABBER_ID=?")
        .toString();

    /** Sql to read a user's user id. */
    private static final String SQL_READ_USER_ID =
        new StringBuffer("select U.JABBER_ID ")
        .append("from PARITY_USER U ")
        .append("where U.USER_ID=?")
        .toString();

    /** Sql to update a user. */
    private static final String SQL_UPDATE =
            new StringBuffer("update PARITY_USER ")
            .append("set NAME=?,ORGANIZATION=?,TITLE=? ")
            .append("where USER_ID=?")
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
            deleteFlags(session, userId);
            createFlags(session, userId, flags);
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
            final List<UserFlag> flags = new ArrayList<UserFlag>();
            while (session.nextResult()) {
                flags.add(session.getUserFlagFromInteger("USER_FLAG_ID"));
            }
            return flags;
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
        session.setLong(4, user.getLocalId());
        if (1 != session.executeUpdate())
            throw new HypersonicException("Could not update user.");
    }

    private void createFlags(final Session session, final Long userId,
            final List<UserFlag> flags) {
        session.prepareStatement(SQL_CREATE_FLAG);
        session.setLong(1, userId);
        for (final UserFlag flag : flags) {
            session.setTypeAsInteger(1, flag);
            if (1 != session.executeUpdate())
                throw translateError("Could not create flag {0} for user {1}.",
                        flag, userId);
        }
    }

    /**
     * Delete all flags for a user.
     * 
     * @param session
     *            A <code>Session</code>.
     * @param userId
     *            A user id <code>Long</code>.
     */
    private void deleteFlags(final Session session, final Long userId) {
        final Integer flagCount = readFlagCount(session, userId);
        session.prepareStatement(SQL_DELETE_FLAGS);
        session.setLong(1, userId);
        if (flagCount.intValue() != session.executeUpdate())
            throw translateError("Could not delete flags for user {0}", userId);
    }

    /**
     * Read the flag count for a user.
     * 
     * @param session
     *            A <code>Session</code>.
     * @param userId
     *            A user id <code>Long</code>.
     * @return A count <code>Integer</code> of all flags for the user.
     */
    private Integer readFlagCount(final Session session, final Long userId) {
        session.prepareStatement(SQL_READ_FLAG_COUNT);
        session.setLong(1, userId);
        session.executeQuery();
        if (session.nextResult()) {
            return session.getInteger("USER_FLAG_COUNT");
        } else {
            return null;
        }
    }
}
