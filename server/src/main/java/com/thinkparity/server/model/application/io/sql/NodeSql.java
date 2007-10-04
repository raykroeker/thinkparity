/*
 * Created On:  8-Aug-07 4:56:00 PM
 */
package com.thinkparity.desdemona.model.io.sql;

import com.thinkparity.desdemona.model.io.hsqldb.HypersonicSession;
import com.thinkparity.desdemona.model.node.Node;
import com.thinkparity.desdemona.model.node.NodeCredentials;
import com.thinkparity.desdemona.model.node.NodeSession;


/**
 * <b>Title:</b>thinkParity Desdemona Model Node SQL<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class NodeSql extends AbstractSql {

    /** Sql to create a new session. */
    private static final String SQL_CREATE_SESSION =
        new StringBuilder("insert into TPSD_NODE_SESSION ")
        .append("(NODE_ID,TOKEN,CREATED_ON) ")
        .append("values (?,?,?)")
        .toString();

    /** Sql to create a new session. */
    private static final String SQL_DELETE_SESSION =
        new StringBuilder("delete from TPSD_NODE_SESSION ")
        .append("where TOKEN=?")
        .toString();

    /** Sql to read a node by its credentials. */
    private static final String SQL_READ_BY_CREDENTIALS =
        new StringBuilder("select N.NODE_ID,N.NODE_USERNAME ")
        .append("from TPSD_NODE N ")
        .append("where N.NODE_USERNAME=? and N.NODE_PASSWORD=?")
        .toString();

    /** Sql to read a node by its session. */
    private static final String SQL_READ_BY_SESSION =
        new StringBuilder("select N.NODE_ID,N.NODE_USERNAME ")
        .append("from TPSD_NODE N ")
        .append("inner join TPSD_NODE_SESSION NS on NS.NODE_ID=N.NODE_ID ")
        .append("where NS.TOKEN=?")
        .toString();

    /**
     * Create NodeSql.
     *
     */
    public NodeSql() {
        super();
    }

    /**
     * Create a session for a node.
     * 
     * @param node
     *            A <code>Node</code>.
     * @param session
     *            A <code>NodeSession</code>.
     */
    public void createSession(final Node node, final NodeSession session) {
        final HypersonicSession dbSession = openSession();
        try {
            dbSession.prepareStatement(SQL_CREATE_SESSION);
            dbSession.setLong(1, node.getId());
            dbSession.setString(2, session.getId());
            dbSession.setCalendar(3, session.getCreatedOn());
            if (1 != dbSession.executeUpdate()) {
                throw panic("Could not create node session.");
            }

            dbSession.commit();
        } catch (final Throwable t) {
            throw translateError(dbSession, t);
        } finally {
            dbSession.close();
        }
    }

    /**
     * Delete a session for a node.
     * 
     * @param session
     *            A <code>NodeSession</code>.
     */
    public void deleteSession(final NodeSession session) {
        final HypersonicSession dbSession = openSession();
        try {
            dbSession.prepareStatement(SQL_DELETE_SESSION);
            dbSession.setString(1, session.getId());
            if (1 != dbSession.executeUpdate()) {
                throw panic("Could not delete node session.");
            }

            dbSession.commit();
        } catch (final Throwable t) {
            throw translateError(dbSession, t);
        } finally {
            dbSession.close();
        }
    }

    /**
     * Read a node.
     * 
     * @param credentials
     *            A set of <code>NodeCredentials</code>.
     * @return A <code>Node</code>.
     */
    public Node read(final NodeCredentials credentials) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_BY_CREDENTIALS);
            session.setString(1, credentials.getUsername());
            session.setString(2, credentials.getPassword());
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
     * Read a node.
     * 
     * @param session
     *            A <code>NodeSession</code>.
     * @return A <code>Node</code>.
     */
    public Node read(final NodeSession session) {
        final HypersonicSession dbSession = openSession();
        try {
            dbSession.prepareStatement(SQL_READ_BY_SESSION);
            dbSession.setString(1, session.getId());
            dbSession.executeQuery();
            if (dbSession.nextResult()) {
                return extract(dbSession);
            } else {
                return null;
            }
        } catch (final Throwable t) {
            throw translateError(dbSession, t);
        } finally {
            dbSession.close();
        }
    }

    /**
     * Extract a node from a database session.
     * 
     * @param session
     *            A <code>HypersonicSession</code>.
     * @return A <code>Node</code>.
     */
    private Node extract(final HypersonicSession session) {
        final Node node = new Node();
        node.setId(session.getLong("NODE_ID"));
        node.setUsername(session.getString("NODE_USERNAME"));
        return node;
    }
}
