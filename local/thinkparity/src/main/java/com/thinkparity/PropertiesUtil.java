/*
 * Created On: May 26, 2006 12:04:29 AM
 * $Id$
 */
package com.thinkparity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * <b>Title:</b>thinkParity OpheliaUI Properties Util<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class PropertiesUtil {

    /** Line separator. */
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    /**
     * Load properties from a file.
     * 
     * @param properties
     *            A java properties object.
     * @param file
     *            A java properties file.
     * @throws IOException
     */
    static void load(final Properties properties, final File file) throws IOException {
        final FileInputStream fis = new FileInputStream(file);
        try {
            properties.load(fis);
        } finally {
            fis.close();
        }
    }

    /**
     * Print the properties to standard out.
     *
     */
    static void print(final String title, final Properties properties) {
        final StringBuffer buffer =
                new StringBuffer("--- ").append(title).append(" Properties ---")
                .append(LINE_SEPARATOR);
        for(final Object key : properties.keySet()) {
            buffer.append(key).append(":")
                    .append(properties.getProperty(key.toString()))
                    .append(LINE_SEPARATOR);
        }
        System.out.println(buffer);
    }

    /**
     * Store properties to a file.
     * 
     * @param properties
     *            A java properties object.
     * @param file
     *            A java properties file.
     * @param comments
     *            Java properties comments.
     * @throws IOException
     */
    static void store(final Properties properties, final File file,
            final String comments) throws IOException {
        final FileOutputStream fos = new FileOutputStream(file);
        try {
            properties.store(fos, comments);
        } finally {
            fos.flush();
            fos.close();
        }
    }

    /** Create PropertiesUtil. */
    private PropertiesUtil() { super(); }
}
