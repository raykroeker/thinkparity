/*
 * Created On: Jul 21, 2006 2:08:16 PM
 */
package com.thinkparity.codebase.http;

import java.util.HashMap;
import java.util.Map;

import com.thinkparity.codebase.Application;
import com.thinkparity.codebase.ApplicationNature;
import com.thinkparity.codebase.Mode;
import com.thinkparity.codebase.assertion.Assert;

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

    /** A map of all operating modes to their web prefix. */
    private static final Map<Mode, String> PREFIXES;

    /** A map of all http applications to their web suffix. */
    private static final Map<Application, Map<Mode, String>> SUFFIXES;

    static {
        PREFIXES = new HashMap<Mode, String>(3, 1.0F);
        PREFIXES.put(Mode.DEVELOPMENT, "http://");
        PREFIXES.put(Mode.TESTING, "https://");
        PREFIXES.put(Mode.PRODUCTION, "https://");

        SUFFIXES = new HashMap<Application, Map<Mode, String>>(1, 1.0F);
        final Map<Mode, String> ROSALINE = new HashMap<Mode, String>();
        ROSALINE.put(Mode.DEVELOPMENT, ":8082/katherina");
        ROSALINE.put(Mode.TESTING, ":8081/katherina");
        ROSALINE.put(Mode.PRODUCTION, ":8080/katherina");
        SUFFIXES.put(Application.ROSALINE, ROSALINE);
    }

    /**
     * Obtain an instance of a link factory.
     * 
     * @param application.
     *            A thinkParity application.
     * @return A link factory.
     */
    public static LinkFactory getInstance(final Application application,
            final Mode mode) {
        Assert.assertTrue(
                "[CANNOT GENERATE LINKS FOR] [" + application + "]",
                application.isNatured(ApplicationNature.HTTP));
        return new LinkFactory(application, mode);
    }

    /** The link prefix. */
    private final String prefix;

    /** The link suffix. */
    private final String suffix;

    /** Create LinkFactory. */
    private LinkFactory(final Application application, final Mode mode) {
        super();
        prefix = PREFIXES.get(mode);
        suffix = SUFFIXES.get(application).get(mode);
    }

    /**
     * Create a link.
     * 
     * @return A link.
     */
    public Link create(final String module, final String action) { 
        final String linkRoot = new StringBuffer(prefix)
                .append("thinkparity.dyndns.org").append(suffix).toString();
        final Link link = new Link(linkRoot);
        link.addContext(module);
        link.addContext(action);
        return link;
    }
}
