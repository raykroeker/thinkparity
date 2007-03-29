/*
 * Created On: May 25, 2006 10:53:29 PM
 * $Id$
 */
package com.thinkparity;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import com.thinkparity.Constants.DateFormats;
import com.thinkparity.Constants.Directories;
import com.thinkparity.Constants.FileNames;
import com.thinkparity.Constants.PropertyNames;
import com.thinkparity.Constants.Sundry;

/**
 * A thinkParity image is a directory structure that can be run.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class Image {

    /** The image class path. */
    private URL[] classPath;

    /** The java library path. */
    private String javaLibraryPath;

    /** The main class arguments. */
    private String[] mainArgs;

    /** The main class. */
    private String mainClassName;

    /** The image configuration. */
    private final Properties properties;

    /** The image configuration file. */
    private final File propertiesFile;

    /** The image root directory. */
    private final File rootDirectory;

    /**
     * Create Image.
     * 
     * @param name
     *            An image <code>String</code>.
     */
    Image(final String image) {
        super();
        this.rootDirectory = new File(Directories.ThinkParity.Dir, image);
        ThinkParity.checkFileExists(rootDirectory);
        this.properties = new Properties();
        this.propertiesFile = new File(rootDirectory, FileNames.ThinkParityImageProperties);
        ThinkParity.checkFileExists(propertiesFile);
    }

    /**
     * Execute the image.  Ensure the image is mounted; build the class and
     * library paths then invoke the main class.
     *
     */
    void execute() {
        if (!isMounted())
            throw new IllegalStateException("Image not mounted.");
        // set last run property
        final Calendar lastRun = Calendar.getInstance();
        setProperty(PropertyNames.ThinkParity.ImageLastRun, DateFormats.ImageLastRun.format(lastRun.getTime()));
        // set library path
        setSystemProperty(PropertyNames.System.JavaLibraryPath, javaLibraryPath);
        // set product
        setSystemProperty(PropertyNames.ThinkParity.ProductName, properties);
        // set release
        setSystemProperty(PropertyNames.ThinkParity.ReleaseName, properties);
        // store image configuration
        try {
            storeProperties();
        } catch (final IOException iox) {
            throw new ThinkParityException("Could not save image configuration.", iox);
        }
        // execute
        final ClassLoader classLoader = URLClassLoader.newInstance(classPath,
                Thread.currentThread().getContextClassLoader());
        Thread.currentThread().setContextClassLoader(classLoader);
        try {
            final Class<?> mainClass = classLoader.loadClass(mainClassName);
            final Method mainMethod = mainClass.getMethod("main", new Class[] {mainArgs.getClass()});
            mainMethod.invoke(null, new Object[] {mainArgs});
        } catch (final ClassNotFoundException cnfx) {
            throw new ThinkParityException("Could not execute image.", cnfx);
        } catch (final NoSuchMethodException nsmx) {
            throw new ThinkParityException("Could not execute image.", nsmx);
        } catch(final IllegalAccessException iax) {
            throw new ThinkParityException("Could not execute image.", iax);
        } catch (final InvocationTargetException itx) {
            throw new ThinkParityException("Could not execute image.", itx);
        }
    }

    /**
     * Mount the image. Read the configuration in the image root directory and
     * build the class path, library path, main and main args.
     * 
     */
    void mount() {
        if (isMounted())
            throw new IllegalStateException();
        // load the image configuration
        try {
            loadProperties();
        } catch (final IOException iox) {
            throw new ThinkParityException("", iox);
        }
        // thinkparity.image-classpath
        ThinkParity.checkProperty(properties, PropertyNames.ThinkParity.ImageClassPath);
        final StringTokenizer classPath = new StringTokenizer(properties.getProperty(PropertyNames.ThinkParity.ImageClassPath), ",");
        final List<URL> imageClassPath = new ArrayList<URL>();
        while(classPath.hasMoreTokens()) {
            try {
                imageClassPath.add(new File(rootDirectory, classPath.nextToken()).toURI().toURL());
            } catch (final MalformedURLException murlx) {
                throw new ThinkParityException("", murlx);
            }
        }
        this.classPath = imageClassPath.toArray(new URL[] {});
        // thinkparity.image-main
        ThinkParity.checkProperty(properties, PropertyNames.ThinkParity.ImageMain);
        mainClassName = properties.getProperty(PropertyNames.ThinkParity.ImageMain);
        // thinkparity.image-mainargs
        ThinkParity.checkProperty(properties, PropertyNames.ThinkParity.ImageMainArgs);
        final StringTokenizer mainArgs = new StringTokenizer(properties.getProperty(PropertyNames.ThinkParity.ImageMainArgs), ",");
        final List<String> imageMainArgs = new ArrayList<String>();
        while(mainArgs.hasMoreTokens()) {
            imageMainArgs.add(mainArgs.nextToken());
        }
        this.mainArgs = imageMainArgs.toArray(new String[] {});
        // thinkparity.image-libararypath
        ThinkParity.checkProperty(properties, PropertyNames.ThinkParity.ImageLibraryPath);
        final StringTokenizer libraryPath = new StringTokenizer(properties.getProperty(PropertyNames.ThinkParity.ImageLibraryPath), ",");
        final StringBuffer imageLibraryPath = new StringBuffer();
        while(libraryPath.hasMoreTokens()) {
            imageLibraryPath.append(new File(rootDirectory, libraryPath.nextToken()).getAbsolutePath());
            if(libraryPath.hasMoreTokens()) { imageLibraryPath.append(File.pathSeparator); }
        }
        this.javaLibraryPath = imageLibraryPath.toString();
    }

    /**
     * Determine whether or not the image has been mounted.
     * 
     * @return True if the image has been mounted; false otherwise.
     */
    private Boolean isMounted() {
        return null != classPath && null != javaLibraryPath
                && null != mainClassName && null != mainArgs;
    }

    /**
     * Load the image properties.
     * 
     * @throws IOException
     */
    private void loadProperties() throws IOException {
        PropertiesUtil.load(properties, propertiesFile);
    }

    /**
     * Set an image property.
     * 
     * @param key
     *            A property key <code>String</code>.
     * @param value
     *            A property value <code>String</code>.
     */
    private void setProperty(final String key, final String value) {
        properties.setProperty(key, value);
    }


    /**
     * Set an system property.
     * 
     * @param key
     *            A property key <code>String</code>.
     * @param source
     *            A property source <code>Properties</code>.
     */
    private void setSystemProperty(final String key, final Properties source) {
        System.setProperty(key, source.getProperty(key));
    }

    /**
     * Set an system property.
     * 
     * @param key
     *            A property key <code>String</code>.
     * @param value
     *            A property value <code>String</code>.
     */
    private void setSystemProperty(final String key, final String value) {
        System.setProperty(key, value);
    }

    /**
     * Store the image properties.
     * 
     * @throws IOException
     */
    private void storeProperties() throws IOException {
        PropertiesUtil.store(properties, propertiesFile,
                Sundry.ThinkParityImageHeader);
    }
}
