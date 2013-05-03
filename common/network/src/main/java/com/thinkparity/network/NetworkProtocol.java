/*
 * Created On:  16-Aug-07 11:46:34 AM
 */
package com.thinkparity.network;

/**
 * <b>Title:</b>thinkParity Network Protocol<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class NetworkProtocol {

    /**
     * Obtain an instance of a protocol.
     * 
     * @param name
     *            A name <code>String</code>.
     * @return A <code>NetworkProtocol</code>.
     */
    public static NetworkProtocol getProtocol(final String name) {
        return new NetworkProtocol(name, Boolean.FALSE);
    }

    /**
     * Obtain an instance of a protocol.
     * 
     * @param name
     *            A name <code>String</code>.
     * @return A <code>NetworkProtocol</code>.
     */
    public static NetworkProtocol getSecureProtocol(final String name) {
        return new NetworkProtocol(name, Boolean.TRUE);
    }

    /** The protocol name. */
    private String name;

    /** Whether or not the protocol is secure. */
    private Boolean secure;

    /**
     * Create NetworkProtocol.
     * 
     * @param name
     *            A protocol name <code>String</code>.
     * @param secure
     *            Whether or not the protocol is secure.
     */
    private NetworkProtocol(final String name, final Boolean secure) {
        super();
        setName(name);
        setSecure(secure);
    }

    /**
     * Obtain the name.
     *
     * @return A <code>String</code>.
     */
    public String getName() {
        return name;
    }

    /**
     * Obtain the secure.
     *
     * @return A <code>Boolean</code>.
     */
    public Boolean isSecure() {
        return secure;
    }

    /**
     * Set the name.
     *
     * @param name
     *		A <code>String</code>.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Set the secure.
     *
     * @param secure
     *		A <code>Boolean</code>.
     */
    public void setSecure(Boolean secure) {
        this.secure = secure;
    }
}
