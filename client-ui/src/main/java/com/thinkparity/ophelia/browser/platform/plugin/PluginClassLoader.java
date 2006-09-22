/*
 * Created On: Sep 20, 2006 2:00:07 PM
 */
package com.thinkparity.ophelia.browser.platform.plugin;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import com.thinkparity.codebase.FileSystem;

import com.thinkparity.ophelia.browser.Constants.FileExtensions;


/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class PluginClassLoader {

    /** A file filter for jar files. */
    private static final FileFilter JAR_FILE_FILTER;

    /** A file filter for resource files. */
    private static final FileFilter RESOURCE_FILE_FILTER;

    static {
        JAR_FILE_FILTER = new FileFilter() {
            public boolean accept(final File pathname) {
                return pathname.isFile()
                        && pathname.getName().toLowerCase().endsWith(
                                FileExtensions.JAR);
            }
        };
        RESOURCE_FILE_FILTER = new FileFilter() {
            public boolean accept(final File pathname) {
                return pathname.isFile();
            }
        };
    }

    /** The class loader. */
    private final ClassLoader classLoader;

    /** The extracted plugin's file system. */
    private final FileSystem fileSystem;

    /**
     * Create PluginClassLoader.
     * 
     * @param extractor
     *            A plugin extractor.
     */
    PluginClassLoader(final FileSystem fileSystem) throws IOException {
        super();
        this.fileSystem = fileSystem;
        this.classLoader = createClassLoader();
    }

    PluginExtension loadExtension(
            final PluginExtensionMetaInfo extensionMetaInfo,
            final PluginServices pluginServices) throws InstantiationException,
            IllegalAccessException, InvocationTargetException,
            ClassNotFoundException, NoSuchMethodException {
        return (PluginExtension) load(
                extensionMetaInfo.getExtensionClass(), pluginServices);
    }

    Plugin loadPlugin(final PluginMetaInfo metaInfo,
            final PluginServices pluginServices) throws InstantiationException,
            IllegalAccessException, ClassNotFoundException,
            InvocationTargetException, NoSuchMethodException {
        return (Plugin) load(metaInfo.getPluginClass(), pluginServices);
    }

    /**
     * Create the plugin class loader. Use the lib directory and any resources
     * in the root of the plugin's root directory.
     * 
     * @return A class loader.
     * @throws IOException
     */
    private ClassLoader createClassLoader() throws IOException {
        final List<URL> classpath = new ArrayList<URL>();
        classpath.addAll(getLibs());
        classpath.addAll(getResources());
        return new URLClassLoader(classpath.toArray(new URL[] {}));
    }

    /**
     * Obtain a list of all of the libraries for the plugin.
     * 
     * @return A <code>List&lt;URL&gt;</code>.
     * @throws IOException
     */
    private List<URL> getLibs() throws IOException {
        final List<URL> libs = new ArrayList<URL>();
        final File[] libFiles = fileSystem.list("/", JAR_FILE_FILTER);
        for (final File libFile : libFiles) {
            libs.add(libFile.toURL());
        }
        return libs;
    }

    /**
     * Obtain a list of all of the resources for the plugin.
     * 
     * @return A <code>List&lt;URL&gt;</code>.
     * @throws IOException
     */
    private List<URL> getResources() throws IOException {
        final List<URL> resources = new ArrayList<URL>();
        final File[] resourceFiles = fileSystem.list("/", RESOURCE_FILE_FILTER);
        for (final File resourceFile : resourceFiles) {
            resources.add(resourceFile.toURL());
        }
        return resources;
    }

    /**
     * Load a plugin class providing plugin services.
     * 
     * @param name
     *            A plugin class.
     * @param pluginServices
     *            The plugin services.
     * @return The plugin class.
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     */
    private Object load(final String name, final PluginServices pluginServices)
            throws InstantiationException, IllegalAccessException,
            InvocationTargetException, ClassNotFoundException,
            NoSuchMethodException {
        final Class loadedClass = classLoader.loadClass(name);
        final Constructor constructor =
            loadedClass.getConstructor(new Class[] { PluginServices.class });
        return constructor.newInstance(new Object[] { pluginServices });
    }
}
