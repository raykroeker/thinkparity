/*
 * Created On:  28-Sep-07 9:42:01 AM
 */
package com.thinkparity.desdemona.model.node;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * <b>Title:</b>thinkParity Desdemona Model Duplicate Node Session Exception<br>
 * <b>Description:</b>A server node with the given credentials already has a
 * session.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class NodeException extends Exception {

    /** A message pattern. */
    private static final Map<Code, String> PATTERNS;

    static {
        PATTERNS = new HashMap<Code, String>(1, 1.0F);
        PATTERNS.put(Code.DUPLICATE_SESSION, "Node {0} is already online.");
    }

    /**
     * Create NodeException.
     * 
     * @param node
     *            A <code>Node</code>.
     * @param credentials
     *            A set of <code>NodeCredentials</code>.
     */
    NodeException(final Code code, final NodeCredentials credentials) {
        super(MessageFormat.format(PATTERNS.get(code), credentials.getUsername()));
    }

    /** <b>Title:</b>Node Exception Code<br> */
    enum Code { DUPLICATE_SESSION }
}
