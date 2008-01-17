/*
 * Created On:  4-Sep-07 1:19:50 PM
 */
package com.thinkparity.desdemona.model.node;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Calendar;

import com.thinkparity.codebase.codec.MD5Util;

import com.thinkparity.codebase.model.session.InvalidCredentialsException;

import com.thinkparity.desdemona.model.AbstractModelImpl;
import com.thinkparity.desdemona.model.io.hsqldb.HypersonicException;
import com.thinkparity.desdemona.model.io.sql.NodeSql;
import com.thinkparity.desdemona.model.node.NodeException.Code;
import com.thinkparity.desdemona.util.DateTimeProvider;

/**
 * <b>Title:</b>thinkParity Desdemona Model Node Model Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class NodeModelImpl extends AbstractModelImpl implements
        NodeModel, InternalNodeModel {

    /** A node sql interface. */
    private NodeSql nodeSql;

    /**
     * Create NodeModelImpl.
     *
     */
    public NodeModelImpl() {
        super();
    }

    /**
     * @see com.thinkparity.desdemona.model.node.NodeModel#login(com.thinkparity.desdemona.model.node.NodeCredentials)
     *
     */
    @Override
    public NodeSession login(final NodeCredentials credentials)
            throws InvalidCredentialsException, NodeException {
        try {
            final Node node = nodeSql.read(encryptPassword(credentials));
            if (null == node) {
                throw new InvalidCredentialsException();
            } else {
                final NodeSession session = newSession(newSessionId());
                nodeSql.createSession(node, session);
                return session;
            }
        } catch (final HypersonicException hx) {
            if (HypersonicException.isDuplicateKey(hx)) {
                throw new NodeException(Code.DUPLICATE_SESSION, credentials);
            } else {
                throw panic(hx);
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.node.NodeModel#logout(com.thinkparity.desdemona.model.node.NodeSession)
     *
     */
    @Override
    public void logout(final NodeSession session) {
        try {
            nodeSql.deleteSession(session);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.node.NodeModel#read(com.thinkparity.desdemona.model.node.NodeSession)
     *
     */
    @Override
    public Node read(final NodeSession session) {
        try {
            return nodeSql.read(session);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.AbstractModelImpl#initialize()
     *
     */
    @Override
    protected void initialize() {
        this.nodeSql = new NodeSql();
    }

    /**
     * Encrypt the node credentials password.
     * 
     * @param credentials
     *            A set of <code>NodeCredentials</code>.
     * @return A set of <code>NodeCredentials</code>.
     * @throws GeneralSecurityException
     * @throws IOException
     */
    private NodeCredentials encryptPassword(final NodeCredentials credentials)
            throws GeneralSecurityException, IOException {
        final NodeCredentials clone = new NodeCredentials();
        clone.setUsername(credentials.getUsername());
        clone.setPassword(encrypt(credentials.getPassword()));
        return clone;
    }

    /**
     * Create a session.
     * 
     * @param createdOn
     *            A created on <code>Calendar</code>.
     * @param sessionId
     *            A session id <code>String</code>.
     * @return An instance of a <code>Session</code>.
     */
    private NodeSession newSession(final Calendar createdOn,
            final String sessionId) {
        final NodeSession session = new NodeSession();
        session.setCreatedOn(createdOn);
        session.setId(sessionId);
        return session;
    }

    /**
     * Create a session.
     * 
     * @param sessionId
     *            A session id <code>String</code>.
     * @param node
     *            A <code>Node</code>.
     * @return A <code>Session</code>.
     */
    private NodeSession newSession(final String sessionId) {
        final Calendar createdOn = DateTimeProvider.getCurrentDateTime();
        return newSession(createdOn, sessionId);
    }

    /**
     * Create a new session id.
     * 
     * @return A session id <code>String</code>.
     */
    private String newSessionId() {
        return MD5Util.md5Base64(String.valueOf(currentDateTime().getTimeInMillis()));
    }
}
