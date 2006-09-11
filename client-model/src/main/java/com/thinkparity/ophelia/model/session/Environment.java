/*
 * Created On: Aug 5, 2006 10:16:01 AM
 */
package com.thinkparity.ophelia.model.session;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class Environment {

    /** The server host. */
    private transient String serverHost;

    /** The server port. */
    private transient Integer serverPort;

    /** Create Environment. */
    Environment() { super(); }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if(null != obj && obj instanceof Environment) {
            return ((Environment) obj).serverHost.equals(serverHost) &&
                    ((Environment) obj).serverPort.equals(serverPort);
        }
        return false;
    }

    /**
     * Obtain the serverHost
     *
     * @return The String.
     */
    public String getServerHost() {
        return serverHost;
    }

    /**
     * Obtain the serverPort
     *
     * @return The port.
     */
    public Integer getServerPort() {
        return serverPort;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return new StringBuffer(getClass().getName())
                .append(serverHost).append(serverPort)
                .toString().hashCode();
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new StringBuffer(getClass().getName()).append("//")
                .append(serverHost)
                .append("/").append(serverPort)
                .toString();
    }

    /**
     * Set serverHost.
     *
     * @param serverHost The String.
     */
    void setServerHost(final String serverHost) { this.serverHost = serverHost; }

    /**
     * Set serverPort.
     *
     * @param serverPort The port.
     */
    void setServerPort(final Integer serverPort) { this.serverPort = serverPort; }
}
