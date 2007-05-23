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
 * <b>Title:</b>thinkParity OpheliaUI<br>
 * <b>Description:</b>The client application entry point.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.7
 */
public final class ThinkParity {

    /** A singleton instance of <code>ThinkParity</code>. */
    private static ThinkParity INSTANCE;

    /**
     * The thinkParity client application entry point.
     * 
     * @param args
     *            The command line arguments.
     */
    public static void main(String[] args) {
        try {
            getInstance().executeImage();
        } catch (final Throwable t) {
            t.printStackTrace(System.err);
            System.exit(1);
        }
    }

    /**
     * Set the image.
     * 
     * @param imageName
     *            An image name <code>String</code>.
     * @throws IOException
     *             if the image cannot be set
     */
    public static void setImage(final String imageName) throws IOException {
        // test to ensure validity
        new Image(imageName);
        getInstance().setProperty(PropertyNames.ThinkParity.Image, imageName);
        getInstance().storeProperties();
    }

    /**
     * Look for the file; and if it is not found, cannot be read from, or cannot
     * be written to; report an error and exit.
     * 
     * @param file
     *            A <code>File</code>.
     */
    static void checkFile(final File file) {
        if (!file.exists()) {
            System.err.println("File " + file.getAbsolutePath() + " was not found.");
            System.exit(1);
        }
        if (!file.canRead()) {
            System.err.println("File " + file.getAbsolutePath() + " cannot be read from.");
            System.exit(1);
        }
        if (!file.canWrite()) {
            System.err.println("File " + file.getAbsolutePath() + " cannot be written to.");
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

    /**
     * Obtain an instance of thinkParity.
     * 
     * @return An instance of <code>ThinkParity</code>.
     */
    private static ThinkParity getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new ThinkParity();
        }
        return INSTANCE;
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
        checkSystemProperty(PropertyNames.ThinkParity.Directory);
        checkSystemProperty(PropertyNames.ThinkParity.Executable);
        checkSystemProperty(PropertyNames.ThinkParity.Environment);
        checkSystemProperty(PropertyNames.ThinkParity.Mode);
        this.properties = new Properties();
        this.propertiesFile = new File(Directories.ThinkParity.Directory, FileNames.ThinkParityProperties);
        checkFile(propertiesFile);
    }

    /**
     * Execute an image.
     *
     */
    private void executeImage() throws IOException {
        loadProperties();
        final Image image = new Image(getProperty(PropertyNames.ThinkParity.Image));
        image.mount();
        image.execute();
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
     * Load thinkParity.properties.  Ensure that the image property is set.
     * 
     */
    private void loadProperties() throws IOException {
        PropertiesUtil.load(properties, propertiesFile);
        checkProperty(properties, PropertyNames.ThinkParity.Image);
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
