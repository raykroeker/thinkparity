/*
 * Created On: May 25, 2006 10:53:29 PM
 * $Id$
 */
package com.thinkparity;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import com.thinkparity.codebase.JVMUtil;

import com.thinkparity.Constants.DateFormats;
import com.thinkparity.Constants.Directories;
import com.thinkparity.Constants.FileNames;
import com.thinkparity.Constants.Files;
import com.thinkparity.Constants.PropertyNames;
import com.thinkparity.Constants.Sundry;

/**
 * <b>Title:</b>thinkParity OpheliaUI Image<br>
 * <b>Description:</b>A thinkParity image is a directory structure that can be
 * run.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.8
 */
final class Image {

    /**
     * Format a system property as a string.
     * 
     * @param key
     *            The property key.
     * @param value
     *            The property value.
     * @return A formatted -Dkey=value <code>String</code>.
     */
    private static String formatSystemProperty(final String key,
            final Properties properties) {
        return formatSystemProperty(key, properties.getProperty(key));
    }

    /**
     * Format a system property as a string.
     * 
     * @param key
     *            The property key.
     * @param value
     *            The property value.
     * @return A formatted -Dkey=value <code>String</code>.
     */
    private static String formatSystemProperty(final String key,
            final String value) {
        return MessageFormat.format("-D{0}={1}", key, value);
    }

    /** The image class path. */
    private String[] classPath;

    /** The java library path. */
    private String javaLibraryPath;

    /** The java virtual machine arguments. */
    private List<String> jvmArgs;

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
        this.rootDirectory = new File(Directories.ThinkParity.Directory, image);
        ThinkParity.checkFile(rootDirectory);
        this.properties = new Properties();
        this.propertiesFile = new File(rootDirectory, FileNames.ThinkParityImageProperties);
        ThinkParity.checkFile(propertiesFile);
    }

    /**
     * Execute the image. An image is executed by staring a new process that
     * will execute the virtual machine.
     * 
     */
    void execute() {
        if (!isMounted())
            throw new IllegalStateException("Image not mounted.");
        // set last run property
        final Calendar lastRun = Calendar.getInstance();
        setProperty(PropertyNames.ThinkParity.ImageLastRun, DateFormats.ImageLastRun.format(lastRun.getTime()));
        // store image configuration
        try {
            storeProperties();
        } catch (final IOException iox) {
            throw new ThinkParityException("Could not save image configuration.", iox);
        }
        // execute a new vm
        try {
            final JVMUtil jvmUtil = JVMUtil.getInstance();
            final List<String> jvmArgs = new ArrayList<String>();
            if (null != javaLibraryPath)
                jvmArgs.add(formatSystemProperty(PropertyNames.System.JavaLibraryPath, javaLibraryPath));
            jvmArgs.add(formatSystemProperty(PropertyNames.ThinkParity.Directory, System.getProperties()));
            jvmArgs.add(formatSystemProperty(PropertyNames.ThinkParity.Executable, Files.ThinkParity.Executable.getAbsolutePath()));
            jvmArgs.add(formatSystemProperty(PropertyNames.ThinkParity.ImageExecutable, jvmUtil.getExecutable().getAbsolutePath()));
            jvmArgs.add(formatSystemProperty(PropertyNames.ThinkParity.ProductName, properties));
            jvmArgs.add(formatSystemProperty(PropertyNames.ThinkParity.ReleaseName, properties));
            jvmArgs.add(formatSystemProperty(PropertyNames.ThinkParity.ReleaseOs, properties));
            jvmArgs.add(formatSystemProperty(PropertyNames.ThinkParity.Environment, System.getProperties()));
            jvmArgs.add(formatSystemProperty(PropertyNames.ThinkParity.Mode, System.getProperties()));
            jvmArgs.addAll(this.jvmArgs);
            final String[] jvmArgsArray = jvmArgs.toArray(new String[] {});
            jvmUtil.print(rootDirectory, jvmArgsArray, classPath, mainClassName,
                    mainArgs);
            final Process process = jvmUtil.execute(rootDirectory, jvmArgsArray,
                    classPath, mainClassName, mainArgs);
            final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while (null != (line = reader.readLine())) {
                System.out.println(line);
            }
        } catch (final Exception x) {
            throw new ThinkParityException("Could not execute image.", x);
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
        StringTokenizer tokenizer;
        String token;

        // thinkparity.image-jvmargs
        jvmArgs = new ArrayList<String>();
        if (properties.containsKey(PropertyNames.ThinkParity.ImageJVMArgs)) {
            tokenizer = new StringTokenizer(properties.getProperty(PropertyNames.ThinkParity.ImageJVMArgs), ",");
            while (tokenizer.hasMoreTokens()) {
                token = tokenizer.nextToken();
                if (!token.contains("=")) {
                    System.err.println("Invalid jvm arg format " + token + ".");
                    System.exit(1);
                }
                jvmArgs.add("-D" + token);
            }
        }

        // thinkparity.image-libararypath
        if (properties.containsKey(PropertyNames.ThinkParity.ImageLibraryPath)) {
            final StringBuffer imageLibraryPath = new StringBuffer();
            tokenizer = new StringTokenizer(properties.getProperty(PropertyNames.ThinkParity.ImageLibraryPath), ",");
            File libraryPathDirectory;
            while (tokenizer.hasMoreTokens()) {
                token = tokenizer.nextToken().replace('/', File.separatorChar);
                libraryPathDirectory = new File(rootDirectory, token);
                if (!libraryPathDirectory.exists()) {
                    System.err.println(MessageFormat.format("Library path directory {0} does not exist.", libraryPathDirectory));
                    System.exit(1);
                }
                if (!libraryPathDirectory.isDirectory()) {
                    System.err.println(MessageFormat.format("Library path directory {0} is not a directory.", libraryPathDirectory));
                    System.exit(1);
                }
                if (!libraryPathDirectory.canRead()) {
                    System.err.println(MessageFormat.format("Library path directory {0} cannot be read.", libraryPathDirectory));
                    System.exit(1);
                }
                imageLibraryPath.append(libraryPathDirectory.getAbsolutePath());
                if (tokenizer.hasMoreTokens()) {
                    imageLibraryPath.append(File.pathSeparator);
                }
            }
            this.javaLibraryPath = imageLibraryPath.toString();
        } else {
            this.javaLibraryPath = null;
        }

        // thinkparity.image-classpath
        ThinkParity.checkProperty(properties, PropertyNames.ThinkParity.ImageClassPath);
        tokenizer = new StringTokenizer(properties.getProperty(PropertyNames.ThinkParity.ImageClassPath), ",");
        final List<String> imageClassPath = new ArrayList<String>();
        File classPathFile;
        while (tokenizer.hasMoreTokens()) {
            token = tokenizer.nextToken().replace('/', File.separatorChar);
            classPathFile = new File(rootDirectory, token);
            if (!classPathFile.exists()) {
                System.err.println("Classpath file " + classPathFile.getName() + " was not found.");
                System.exit(1);
            }
            if (!classPathFile.canRead()) {
                System.err.println("Classpath file " + classPathFile.getName() + " could not be read.");
                System.exit(1);
            }
            imageClassPath.add(token);
        }
        this.classPath = imageClassPath.toArray(new String[] {});

        // thinkparity.image-main
        ThinkParity.checkProperty(properties, PropertyNames.ThinkParity.ImageMain);
        mainClassName = properties.getProperty(PropertyNames.ThinkParity.ImageMain);

        // thinkparity.image-mainargs
        final List<String> imageMainArgs = new ArrayList<String>();
        if (properties.containsKey(PropertyNames.ThinkParity.ImageMainArgs)) {
            tokenizer = new StringTokenizer(properties.getProperty(PropertyNames.ThinkParity.ImageMainArgs), ",");
            while (tokenizer.hasMoreTokens()) {
                imageMainArgs.add(tokenizer.nextToken());
            }
        }
        this.mainArgs = imageMainArgs.toArray(new String[] {});
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
     * Store the image properties.
     * 
     * @throws IOException
     */
    private void storeProperties() throws IOException {
        PropertiesUtil.store(properties, propertiesFile,
                Sundry.ThinkParityImageHeader);
    }
}
