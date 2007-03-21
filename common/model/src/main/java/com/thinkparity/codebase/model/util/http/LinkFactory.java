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
        Assert.assertTrue(
                "[CANNOT GENERATE LINKS FOR] [" + application + "]",
                application.isNatured(ApplicationNature.HTTP));
        return new LinkFactory(environment);
    }

    /** The link prefix. */
    private final String prefix;

    /** The link url. */
    private final String url;

    /** Create LinkFactory. */
    private LinkFactory(final Environment environment) {
        super();
        prefix = environment.isWebTLSEnabled() ? "https://" : "http://";
        url = environment.getWebHost();
    }

    /**
     * Create a root link.
     * 
     * @return A <code>Link</code>.
     */
    public Link create() {
        final String linkRoot = new StringBuffer(prefix).append(url).toString();
        return new Link(linkRoot);
    }

    /**
     * Create a link.
     * 
     * @param action
     *            A thinkParity action <code>String</code>.
     * @return A link.
     */
    public Link create(final String action) { 
        final Link link = create();
        link.addContext(action);
        return link;
    }
}
