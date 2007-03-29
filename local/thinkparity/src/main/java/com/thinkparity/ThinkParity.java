/*
 * Created On: May 25, 2006 10:40:09 PM
 * $Id$
 */
package com.thinkparity;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import com.thinkparity.Constants.Directories;
import com.thinkparity.Constants.FileNames;
import com.thinkparity.Constants.PropertyNames;
import com.thinkparity.Constants.Sundry;

/**
 * The thinkParity client application entry point.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class ThinkParity {

    /**
     * The thinkParity client application entry point.
     * 
     * @param args
     *            The command line arguments.
     */
    public static void main(String[] args) {
        try {
            new ThinkParity().executeImage();
        } catch (final Throwable t) {
            t.printStackTrace(System.err);
            System.exit(1);
        }
    }

    /**
     * Look for the file; and if it is not found; report an error and exit.
     *
     * @param file
     *      A file.
     */
    static void checkFileExists(final File file) {
        if(!file.exists()) {
            System.err.println("File " + file.getName() + " was not found.");
            System.exit(1);
        }
    }

    /**
     * Look for the file; and if it is not found; report an error and exit.
     * 
     * @param parent
     *            The parent abstract pathname
     * @param child
     *            The child pathname string
     * @see File#File(File, String)
     */
    static void checkFileExists(final File parent, final String child) {
        final File file = new File(parent, child);
        if(!file.exists()) {
            System.err.println("File " + parent.getName() + "/" + child + " was not found.");
            System.exit(1);
        }
    }

    /**
     * Look for a key in the set of properties and if it is not found; report
     * an error and exit.
     *
     * @param properties
     *      The properties to check.
     * @param key
     *      The property key to look for.
     */
    static void checkProperty(final Properties properties, final String key) {
        final String value = properties.getProperty(key, null);
        if(null == value) {
            System.err.println("Property " + key + " was not found.");
            System.exit(1);
        }
    }

    /**
     * Look for the key in the system properties and if it is not found;
     * report an error and exit.
     *
     * @param key
     *      A property key.
     */
    static void checkSystemProperty(final String key) {
        checkProperty(System.getProperties(), key);
    }

    /** The configuration <code>Properties</code>. */
    private final Properties properties;

    /** The configuration <code>File</code>. */
    private final File propertiesFile;

    /**
     * Create ThinkParity.
     *
     */
    private ThinkParity() {
        super();
        checkSystemProperty(PropertyNames.ThinkParity.Dir);
        this.properties = new Properties();
        this.propertiesFile = new File(Directories.ThinkParity.Dir, FileNames.ThinkParityProperties);
        ThinkParity.checkFileExists(propertiesFile);
    }

    /**
     * Execute an image.
     *
     */
    private void executeImage() throws IOException {
        loadProperties();
        final Image image = new Image(getProperty(PropertyNames.ThinkParity.Image));
        try {
            image.mount();
            image.execute();
        } finally {
            storeProperties();
        }
    }

    /**
     * Obtain a property from thinkParity.properties.
     * 
     * @param key
     *            A property key <code>String</code>.
     * @return A property value <code>String</code>.
     */
    private String getProperty(final String key) {
        return properties.getProperty(key);
    }

    /**
     * Obtain a system property.
     * 
     * @param key
     *            A property key <code>String</code>.
     * @return A property value <code>String</code>.
     */
    private String getSystemProperty(final String key) {
        return System.getProperty(key);
    }

    /**
     * Obtain a system property.
     * 
     * @param key
     *            A property key <code>String</code>.
     * @return A property value <code>String</code>.
     */
    private boolean isSetSystemProperty(final String key) {
        final String value = getSystemProperty(key);
        if (null == value || 0 == value.length()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Load thinkParity.properties. If a system property value for the image is
     * set, set the corresponding value within the properties.
     * 
     */
    private void loadProperties() throws IOException {
        PropertiesUtil.load(properties, propertiesFile);
        if (isSetSystemProperty(PropertyNames.ThinkParity.Image)) {
            setProperty(PropertyNames.ThinkParity.Image,
                    getSystemProperty(PropertyNames.ThinkParity.Image));
        }
    }

    /**
     * Set a property in thinkParity.properties.
     * 
     * @param key
     *            A property key <code>String</code>.
     * @param value
     *            A property value <code>String</code>.
     * @return The previous property value.
     */
    private String setProperty(final String key, final String value) {
        return (String) properties.setProperty(key, value);
    }

    /**
     * Save thinkParity.properties.
     *
     */
    private void storeProperties() throws IOException {
        PropertiesUtil.store(properties, propertiesFile, Sundry.ThinkParityHeader);
    }
}
