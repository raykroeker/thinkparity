/*
 * Created On: Jul 21, 2006 2:08:16 PM
 */
package com.thinkparity.codebase.model.util.http;

import com.thinkparity.codebase.Application;

/**
 * <b>Title:</b>thinkParity Link Factory<br>
 * <b>Description:</b>A thinkParity Link Factory is a factory used to create
 * links for an application.
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 * @see Link
 * @see Application
 */
public class LinkFactory {

    /** A singleton instance of a link factory. */
    private static final LinkFactory SINGLETON;

    static {
        SINGLETON = new LinkFactory();
    }

    /**
     * Obtain an instance of a link factory.
     * 
     * @return A <code>LinkFactory</code>.
     */
    public static LinkFactory getInstance() {
        return SINGLETON;
    }

    /** The link protocol. */
    private final String protocol;

    /** The link web host. */
    private final String webHost;

    /** The link web port. */
    private final Integer webPort;

    /**
     * Create LinkFactory.
     * 
     */
    private LinkFactory() {
        super();
        this.protocol = "http://";
        this.webHost = "www.thinkparity.com";
        this.webPort = 80;
    }

    /**
     * Create a link.
     * 
     * @return A <code>Link</code>.
     */
    public Link create() {
        final StringBuilder linkRoot = new StringBuilder(protocol)
            .append(webHost);
        // if we are not on 80 or 443 we need to specify the port
        if (80 != webPort && 443 != webPort)
            linkRoot.append(":").append(webPort);
        return new Link(linkRoot.toString());
    }

    /**
     * Create a link.
     * 
     * @param action
     *            A thinkParity action <code>String</code>.
     * @return A <code>Link</code>.
     */
    public Link create(final String action) { 
        final Link link = create();
        link.addContext(action);
        return link;
    }

    /**
     * Create the display portion of the link.
     * 
     * @return The display portion of the link.
     */
    public String createDisplay() {
        return new StringBuilder(webHost).toString();
    }
}
