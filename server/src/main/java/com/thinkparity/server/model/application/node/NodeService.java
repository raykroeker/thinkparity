/*
 * Created On:  4-Sep-07 1:02:11 PM
 */
package com.thinkparity.desdemona.model.node;

import com.thinkparity.codebase.model.session.InvalidCredentialsException;

import com.thinkparity.desdemona.model.ModelFactory;


/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class NodeService {

    /** A singleton instance of the node service. */
    private static final NodeService SINGLETON;

    static {
        SINGLETON = new NodeService();
    }

    /**
     * Obtain an instance of the node service.
     * 
     * @return A <code>NodeService</code>.
     */
    public static NodeService getInstance() {
        return SINGLETON;
    }

    /** A node. */
    private Node node;

    /** A model factory. */
    private final NodeModel nodeModel;

    /** A node session. */
    private NodeSession session;

    /**
     * Create NodeService.
     *
     */
    private NodeService() {
        super();
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        this.nodeModel = ModelFactory.getInstance(loader).getNodeModel();
    }

    /**
     * Obtain the node.
     * 
     * @return A <code>Node</code>.
     */
    public Node getNode() {
        return node;
    }

    /**
     * Start the node service.
     * 
     */
    public void start(final NodeCredentials credentials)
            throws InvalidCredentialsException, NodeException {
        this.session = nodeModel.login(credentials);
        this.node = nodeModel.read(session);
    }

    /**
     * Stop the node service.
     * 
     */
    public void stop() {
        try {
            nodeModel.logout(session);
        } finally {
            session = null;
            node = null;
        }
    }
}
