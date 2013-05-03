/*
 * Created On:  4-Sep-07 1:12:49 PM
 */
package com.thinkparity.desdemona.model.node;

import com.thinkparity.codebase.model.session.InvalidCredentialsException;

/**
 * <b>Title:</b>thinkParity Desdemona Model Node Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface NodeModel {

    /**
     * Login.
     * 
     * @param credentials
     *            A set of <code>NodeCredentials</code>.
     * @return A <code>NodeSession</code>.
     * @throws InvalidCredentialsException
     *             if the credentials are invalid
     * @throws NodeException
     *             if the node already has a session elsewhere
     */
    NodeSession login(NodeCredentials credentials)
            throws InvalidCredentialsException, NodeException;

    /**
     * Logout.
     * 
     * @param session
     *            A <code>NodeSession</code>.
     * @throws InvalidCredentialsException
     *             if the credentials are invalid
     */
    void logout(NodeSession session);

    /**
     * Read the node.
     * 
     * @param session
     *            A <code>NodeSession</code>.
     * @return A <code>Node</code>.
     */
    Node read(NodeSession session);
}
