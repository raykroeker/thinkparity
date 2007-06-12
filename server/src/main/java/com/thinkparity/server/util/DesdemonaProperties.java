/*
 * Created On:  29-May-07 3:56:28 PM
 */
package com.thinkparity.desdemona.util;

import java.io.PrintStream;
import java.util.Properties;

import com.thinkparity.codebase.PropertiesUtil;

/**
 * <b>Title:</b>thinkParity DesdemonaModel Properties<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class DesdemonaProperties extends Properties {

    /** A singleton instance of <code>DesdemonaProperties</code>. */
    private static final DesdemonaProperties SINGLETON;

    static {
        SINGLETON = new DesdemonaProperties();
    }

    /**
     * Obtain an instance of desdemona properties.
     * 
     * @return An instance of <code>DesdemonaProperties</code>.
     */
    public static DesdemonaProperties getInstance() {
        return SINGLETON;
    }

    /**
     * Create DesdemonaProperties.
     *
     */
    private DesdemonaProperties() {
        super();
    }

    /**
     * Print the properties to the print stream.
     * 
     * @param printStream
     *            A <code>PrintStream</code>.
     */
    public void println(final PrintStream printStream) {
        final StringBuffer buffer = new StringBuffer();
        PropertiesUtil.print(buffer, this);
        printStream.append(buffer);
    }
}
