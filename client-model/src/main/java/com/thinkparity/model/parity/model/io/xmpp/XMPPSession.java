/*
 * Created On: Fri May 12 2006 10:33 PDT
 * $Id$
 */
package com.thinkparity.model.parity.model.io.xmpp;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;

import org.jivesoftware.smack.XMPPConnection;

import com.thinkparity.codebase.JVMUniqueId;
import com.thinkparity.codebase.StackUtil;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.Constants.Jabber;
import com.thinkparity.model.parity.model.session.Credentials;
import com.thinkparity.model.xmpp.JabberId;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.Release;

/**
 * The parity bootstrap's io xmpp session.
 *
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class XMPPSession {

    /** An apache logger. */
    protected final Logger logger;

    /** A smack xmpp connection. */
    private final XMPPConnection connection;

    /** A jvm unique id. */
    private final JVMUniqueId uniqueId;

    /** An xmpp method definition. */
    private XMPPMethod xmppMethod;

    /** The xmpp method's response. */
    private XMPPMethodResponse xmppMethodResponse;

    /** Create XMPPSession. */
    XMPPSession(final String serverHost, final Integer serverPort) {
        super();
        try {
            this.connection = new XMPPConnection(serverHost, serverPort);
            this.connection.loginAnonymously();
        }
        catch(final org.jivesoftware.smack.XMPPException xmppx) {
            throw new XMPPException(xmppx);
        }
        this.logger = Logger.getLogger(getClass());
        this.uniqueId = JVMUniqueId.nextId();
    }

    /** Create XMPPSession. */
    XMPPSession(final String serverHost, final Integer serverPort,
            final Credentials credentials) {
        super();
        try {
            this.connection = new XMPPConnection(serverHost, serverPort);
            this.connection.login(credentials.getUsername(), credentials.getPassword(), Jabber.RESOURCE);
        }
        catch(final org.jivesoftware.smack.XMPPException xmppx) {
            throw new XMPPException(xmppx);
        }
        this.logger = Logger.getLogger(getClass());
        this.uniqueId = JVMUniqueId.nextId();
    }

    /** Close this session. */
    public void close() {
        this.xmppMethod = null;
        this.xmppMethodResponse = null;
        XMPPSessionManager.close(this);
    }

    /** Commit the session transaction. */
    public void commit() { /* ? */ }

    /**
     * Determine whether or not the remote method invocation has a result.
     *
     * @return True if the method invocation has a result.
     */
    public Boolean containsResult() {
        assertMethod("[CONTAINS RESULT]");
        assertMethodResponse("[CONTAINS RESULT]");
        return xmppMethodResponse.containsResult();
    }

    /** @see java.lang.Object#equals(java.lang.Object) */
    public boolean equals(final Object obj) {
		if(null != obj && obj instanceof XMPPSession)
			return ((XMPPSession) obj).uniqueId.equals(uniqueId);
		return false;
    }

    /** Execute the xmpp method. */
    public void execute() { xmppMethodResponse = execute(xmppMethod); }

    public byte[] getBytes(final String name) {
        return xmppMethodResponse.readBytes(name);
    }

    public Calendar getCalendar(final String name) {
        return xmppMethodResponse.readResultCalendar(name);
    }

    /**
     * Obtain a library list result value.
     * 
     * @param name
     *            The result name.
     * @return The result value.
     */
    public List<Library> getLibraries(final String name) {
        return xmppMethodResponse.readResultLibraries(name);
    }

    /**
     * Obatain a release list result value.
     * 
     * @param name
     *            The result name.
     * @return The result value.
     */
    public List<Release> getReleases(final String name) {
        return xmppMethodResponse.readResultReleases(name);
    }

    public Library.Type getLibraryType(final String name) {
        return xmppMethodResponse.readResultLibraryType(name);
    }

    /**
     * Obtain a long result value.
     * 
     * @param name
     *            The result name.
     * @return The result value.
     */
    public Long getLong(final String name) {
        return xmppMethodResponse.readResultLong(name);
    }

    public byte[] getSmallBytes(final String name) {
        return xmppMethodResponse.readSmallBytes(name);
    }

    /**
     * Obtain a string result value.
     * 
     * @param name
     *            The result name.
     * @return The result value.
     */
    public String getString(final String name) {
        return xmppMethodResponse.readResultString(name);
    }

    /** @see java.lang.Object#hashCode() */
    public int hashCode() { return uniqueId.hashCode(); }

    /**
     * Rollback the xmpp transaction.
     *
     */
    public void rollback() {
        Assert.assertNotYetImplemented("XMPPSession#rollback() [" + StackUtil.getCallerClassAndMethodName() + "]");
    }

    /**
     * Set a list of jabber id parameters.
     * 
     * @param listName
     *            The list name.
     * @param name
     *            The list item name.
     * @param values
     *            The list of values.
     */
    public final void setJabberIdParameters(final String listName,
            final String name, final List<JabberId> values) {
        assertMethod("[SET JABBER ID PARAMETERS]");
        debugJabberIdParameters(listName, name, values);
        xmppMethod.setJabberIdParameters(listName, name, values);
    }

    /**
     * Set a list of long parameters.
     * 
     * @param listName
     *            The list name.
     * @param name
     *            The list item name.
     * @param longs
     *            The list of values.
     */
    public final void setLongParameters(final String listName, final String name,
            final List<Long> longs) {
        assertMethod("[SET LONG PARAMETERS]");
        debugLongParameters(listName, name, longs);
        xmppMethod.setLongParameters(listName, name, longs);
    }

    public void setParameter(final String name, final byte[] value) {
        assertMethod("[SET PARAMETER]");
        debugParameter(name, value);
        xmppMethod.setParameter(name, value);
    }

    public void setParameter(final String name, final Library.Type value) {
        assertMethod("[SET PARAMETER]");
        debugParameter(name, value);
        xmppMethod.setParameter(name, value);
    }

    /**
     * Set a named parameter for the remote method.
     * 
     * @param name
     *            The parameter name.
     * @param value
     *            The parameter value.
     */
    public void setParameter(final String name, final Long value) {
        assertMethod("[SET PARAMETER]");
        debugParameter(name, value);
        xmppMethod.setParameter(name, value);
    }

    /**
     * Set a named parameter for the remote method.
     * 
     * @param name
     *            The parameter name.
     * @param value
     *            The parameter value.
     */
    public void setParameter(final String name, final String value) {
        assertMethod("[SET PARAMETER]");
        debugParameter(name, value);
        xmppMethod.setParameter(name, value);
    }

    /**
     * Set a named parameter for the remote method.
     * 
     * @param name
     *            The parameter name.
     * @param value
     *            The paramter value.
     */
    public final void setParameter(final String name, final UUID value) {
        assertMethod("[SET PARAMETER]");
        debugParameter(name, value);
        xmppMethod.setParameter(name, value);
    }

    /**
     * Set the remote method to be invoked.
     *
     * @param remoteMethod.
     *      A remote method.
     */
    public void setRemoteMethod(final String methodName) {
        assertNoMethod("[SET REMOTE METHOD]");
        xmppMethod = new XMPPMethod(methodName);
        xmppMethodResponse = null;
    }

    private void assertMethod(final String assertion) {
        Assert.assertNotNull("[LBROWSER BOOTSTRAP] [XMPP IO] " + assertion +
                " [REMOTE METHOD IS NOT DEFINED]", xmppMethod);
    }

    private void assertMethodResponse(final String assertion) {
        Assert.assertNotNull("[LBROWSER BOOTSTRAP] [XMPP IO] " + assertion +
                " [REMOTE METHOD RESPONSE IS NOT DEFINED]", xmppMethodResponse);
    }

    /**
     * Assert the remote method has not yet been defined.
     *
     * @param assertion
     *      The assertion.
     */
    private void assertNoMethod(final String assertion) {
        Assert.assertIsNull("[LBROWSER BOOTSTRAP] [XMPP IO] " + assertion +
                " [REMOTE METHOD ALREADY DEFINED]", xmppMethod);
        Assert.assertIsNull("[LBROWSER BOOTSTRAP] [XMPP IO] " + assertion +
                " [REMOTE METHOD RESPONSE ALREADY DEFINED]", xmppMethodResponse);
    }

    private final void debugJabberIdParameters(final String listName,
            final String name, final List<JabberId> values) {}

    private final void debugLongParameters(final String listName,
            final String name, final List<Long> values) {}

    private void debugParameter(final String name, final byte[] value) {}

    private void debugParameter(final String name, final Library.Type value) {}

    /**
     * Debug a long parameter.
     * 
     * @param name
     *            The parameter name.
     * @param value
     *            The parameter value.
     */
    private void debugParameter(final String name, final Long value) {
        debugParameter(name, null == value ? "null" : value.toString());
    }

    /**
     * Debug a string parameter.
     * 
     * @param name
     *            The parameter name.
     * @param value
     *            The parameter value.
     */
    private void debugParameter(final String name, final String value) {
        logger.debug(new StringBuffer("[LMODEL] [IO] [XMPP] [")
                .append(name).append("] [")
                .append(value).append("]"));
    }

    /**
     * Debug a unique id paramter.
     * 
     * @param name
     *            The paramter name.
     * @param value
     *            The paramter value.
     */
    private final void debugParameter(final String name, final UUID value) {
        debugParameter(name, null == value ? "null" : value.toString());
    }

    /**
     * Execute an xmpp method and return the xmpp response.
     * 
     * @param xmppMethod
     *            An xmpp method.
     * @return An xmpp method response.
     */
    private XMPPMethodResponse execute(final XMPPMethod xmppMethod) {
        return xmppMethod.execute(connection);
    }
}
