/*
 * Created On:  Nov 19, 2007 11:23:25 AM
 */
package com.thinkparity.ophelia.support.ui.window;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.WindowConstants;

import com.thinkparity.codebase.ResourceUtil;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class WindowConfiguration extends Properties {

    /** The window configuration. */
    private static Properties configuration;

    /**
     * Initialize the window configuration.
     * 
     * @throws IOException
     */
    static void initialize() throws IOException {
        configuration = new Properties();
        final InputStream inputStream = ResourceUtil.getInputStream("window/window.properties");
        try {
            configuration.load(inputStream);
        } finally {
            inputStream.close();
        }
    }

    /**
     * Create WindowConfiguration.
     * 
     * @param id
     *            A <code>String</code>.
     */
    WindowConfiguration(final String id) {
        super();
    }

    /**
     * Obtain the window's default close operation.
     * 
     * @return An <code>int</code>.
     * @see WindowConstants#DISPOSE_ON_CLOSE
     * @see WindowConstants#DO_NOTHING_ON_CLOSE
     * @see WindowConstants#EXIT_ON_CLOSE
     * @see WindowConstants#HIDE_ON_CLOSE
     */
    int getDefaultCloseOperation() {
        /* TODO - WindowConfiguration#getDefaultCloseOperation() - implement
         * custom configuration */
        return WindowConstants.DISPOSE_ON_CLOSE;
    }
}
