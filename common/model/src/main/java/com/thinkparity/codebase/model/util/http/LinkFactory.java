/*
 * Created On: Jul 21, 2006 2:08:16 PM
 */
package com.thinkparity.codebase.model.util.http;

import com.thinkparity.codebase.Application;
import com.thinkparity.codebase.ApplicationNature;
import com.thinkparity.codebase.Mode;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.codebase.model.session.Environment;

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

    /**
     * Obtain an instance of a link factory.
     * 
     * @param application
     *            A thinkParity application.
     * @param mode
     *            A thinkParity mode of operation.
     * @return A link factory.
     * @see Application
     * @see Mode
     */
    public static LinkFactory getInstance(final Application application,
            final Environment environment) {
        Assert.assertTrue(application.isNatured(ApplicationNature.HTTP),
                "Cannot generate links for {0}.", application);
        return new LinkFactory(environment);
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
     * @param environment
     *            An <code>Environment</code>.
     */
    private LinkFactory(final Environment environment) {
        super();
        this.protocol = environment.isWebTLSEnabled() ? "https://" : "http://";
        this.webHost = environment.getWebHost();
        this.webPort = environment.getWebPort();
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
}
