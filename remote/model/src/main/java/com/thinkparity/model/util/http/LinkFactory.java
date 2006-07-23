/*
 * Created On: Jul 21, 2006 2:08:16 PM
 */
package com.thinkparity.model.util.http;

import java.util.HashMap;
import java.util.Map;

import com.thinkparity.codebase.Application;
import com.thinkparity.codebase.ApplicationNature;
import com.thinkparity.codebase.Mode;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.server.Version;
import com.thinkparity.server.ParityServerConstants.Internet.WWW;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class LinkFactory {

    /**
     * Obtain an instance of a link factory.
     * 
     * @param application.
     *            A thinkParity application.
     * @return A link factory.
     */
    public static LinkFactory getInstance(final Application application) {
        Assert.assertTrue("[CANNOT GENERATE LINKS FOR] [" + application + "]", application.isNatured(ApplicationNature.HTTP));
        return new LinkFactory(application);
    }

    /** A thinkParity application. */
    private final Application application;

    /** A map of all operating modes to their web prefix. */
    private final Map<Mode, String> prefixes;

    /** A map of all http applications to their web suffix. */
    private final Map<Application, String> suffixes;

    /** Create LinkFactory. */
    private LinkFactory(final Application application) {
        super();
        this.application = application;
        this.prefixes = new HashMap<Mode, String>(3, 1.0F);
        prefixes.put(Mode.DEVELOPMENT, "https://perth.");
        prefixes.put(Mode.TESTING, "https://brisbane.");
        prefixes.put(Mode.PRODUCTION, "https://adelaide.");
        this.suffixes = new HashMap<Application, String>(1, 1.0F);
        suffixes.put(Application.ROSALINE, "/rosaline");
    }

    /**
     * Create a link.
     * 
     * @return A link.
     */
    public Link create(final String module, final String action) { 
        final String prefix = prefixes.get(Version.getMode());
        final String suffix = suffixes.get(application);
        final String linkRoot = new StringBuffer(prefix)
                .append(WWW.BASE).append(suffix).toString();
        final Link link = new Link(linkRoot);
        link.addContext(module);
        link.addContext(action);
        return link;
    }
}
