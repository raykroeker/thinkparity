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

    /**
     * Retrieve the parity image name. If it is set within the system properties
     * use it; otherwise read the default value.
     * 
     * @param properties
     *            The default properties.
     * @return The parity image name.
     */
    private static String getName(final Properties properties) {
        // allow override of the image via a system property
        final Properties systemProperties = System.getProperties();
        if(systemProperties.containsKey(PropertyNames.ParityImageName)) {
            return systemProperties.getProperty(PropertyNames.ParityImageName);
        }
        else {
            ThinkParity.checkProperty(properties, PropertyNames.ParityImageName);
            return properties.getProperty(PropertyNames.ParityImageName);
        }
    }

    /** The image class path. */
    private URL[] classPath;

    /** The image library path. */
    private String libraryPath;

    /** The main class arguments. */
    private String[] mainArgs;

    /** The main class. */
    private String mainClassName;

    /** The image name. */
    private final String name;

    /** The image configuration. */
    private final Properties properties;

    /** The image root directory. */
    private final File root;

    /** Create Image. */
    public Image(final Properties properties) {
        super();
        this.name = getName(properties);
        this.root = new File(Directories.ParityInstall, name);
        ThinkParity.checkFileExists(root);
        this.properties = new Properties();
    }

    /** Execute the image. */
    public void execute() {
        if(!isMounted()) { throw new IllegalStateException(); }

        // set last run property
        final Calendar lastRun = Calendar.getInstance();
        properties.setProperty(PropertyNames.ParityImageLastRun, DateFormats.ImageLastRun.format(lastRun.getTime()));

        // set library path
        System.setProperty(PropertyNames.JavaLibraryPath, libraryPath);

        // save image configuration
        try { PropertiesUtil.store(properties, new File(root, FileNames.ThinkParityImageProperties), Sundry.ThinkParityImageHeader); }
        catch(final IOException iox) { throw new ThinkParityException("", iox); }

        // create class loader
        final ClassLoader classLoader = URLClassLoader.newInstance(classPath, null);
        try {
            // execute
            final Class mainClass = classLoader.loadClass(mainClassName);
            final Method mainMethod = mainClass.getMethod("main", new Class[] {mainArgs.getClass()});
            mainMethod.invoke(null, new Object[] {mainArgs});
        }
        catch(final ClassNotFoundException cnfx) { throw new ThinkParityException("", cnfx); }
        catch(final NoSuchMethodException nsmx) { throw new ThinkParityException("", nsmx); }
        catch(final IllegalAccessException iax) { throw new ThinkParityException("", iax); }
        catch(final InvocationTargetException itx) { throw new ThinkParityException("", itx); }
    }

    /**
     * Obtain the name
     * 
     * @return A name string.
     */
    public String getName() { return name; }

    /** Mount the image. */
    public void mount() {
        if(isMounted()) { throw new IllegalStateException(); }

        ThinkParity.checkFileExists(root, FileNames.ThinkParityImageProperties);
        // load the image configuration
        try { PropertiesUtil.load(properties, new File(root, FileNames.ThinkParityImageProperties)); }
        catch(final IOException iox) { throw new ThinkParityException("", iox); }

        // set the class path
        ThinkParity.checkProperty(properties, PropertyNames.ParityImageClassPath);
        final StringTokenizer classPath = new StringTokenizer(properties.getProperty(PropertyNames.ParityImageClassPath), ",");
        final List<URL> imageClassPath = new ArrayList<URL>();
        while(classPath.hasMoreTokens()) {
            try { imageClassPath.add(new File(root, classPath.nextToken()).toURL()); }
            catch(final MalformedURLException murlx) { throw new ThinkParityException("", murlx); }
        }
        this.classPath = imageClassPath.toArray(new URL[] {});

        // set the main class
        ThinkParity.checkProperty(properties, PropertyNames.ParityImageMain);
        mainClassName = properties.getProperty(PropertyNames.ParityImageMain);

        // set the main args
        ThinkParity.checkProperty(properties, PropertyNames.ParityImageMainArgs);
        final StringTokenizer mainArgs = new StringTokenizer(properties.getProperty(PropertyNames.ParityImageMainArgs), ",");
        final List<String> imageMainArgs = new ArrayList<String>();
        while(mainArgs.hasMoreTokens()) {
            imageMainArgs.add(mainArgs.nextToken());
        }
        this.mainArgs = imageMainArgs.toArray(new String[] {});

        // set the library path
        ThinkParity.checkProperty(properties, PropertyNames.ParityImageLibraryPath);
        final StringTokenizer libraryPath = new StringTokenizer(properties.getProperty(PropertyNames.ParityImageLibraryPath), ",");
        final StringBuffer imageLibraryPath = new StringBuffer();
        while(libraryPath.hasMoreTokens()) {
            imageLibraryPath.append(new File(root, libraryPath.nextToken()).getAbsolutePath());
            if(libraryPath.hasMoreTokens()) { imageLibraryPath.append(File.pathSeparator); }
        }
        this.libraryPath = imageLibraryPath.toString();
    }

    /**
     * Determine whether or not the image has been mounted.
     * 
     * @return True if the image has been mounted; false otherwise.
     */
    private Boolean isMounted() {
        return null != classPath && null != libraryPath
                && null != mainClassName && null != mainArgs;
    }
}
