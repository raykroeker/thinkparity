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
import java.util.Locale;
import java.util.ResourceBundle;

import com.thinkparity.codebase.FileSystem;

import com.thinkparity.ophelia.browser.Constants.FileExtensions;

/**
 * <b>Title:</b>thinkParity Browser Platform Plugin Class Loader<br>
 * <b>Description:</b>The plugin class loader uses <code>FileSystem</code> to
 * create a classpath for a plugin. It then reads a plugin's meta info to load
 * the plugin class itself as well as any extensions.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 * @see #loadExtension(PluginExtensionMetaInfo, PluginServices)
 * @see #loadPlugin(PluginMetaInfo, PluginServices)
 */
class PluginClassLoader extends PluginUtility {

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

    /** The underlying <code>ClassLoader</code>. */
    private ClassLoader classLoader;

    /** The plugin's <code>FileSystem</code>. */
    private final FileSystem fileSystem;

    /**
     * Create PluginClassLoader.
     * 
     * @param fileSystem
     *            A file system within which the plugin resides.
     */
    PluginClassLoader(final FileSystem fileSystem) {
        super();
        this.fileSystem = fileSystem;
    }

    /**
     * Load a resource bundle.
     * 
     * @param baseName
     *            The bundle base name.
     * @param locale
     *            The bundle locale.
     * @return A resource bundle.
     */
    ResourceBundle loadBundle(final String baseName, final Locale locale) {
        try {
            return ResourceBundle.getBundle(baseName, locale, lazyCreateClassLoader());
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Load a plugin extension. Requires the extension's fully-qualified class
     * name.
     * 
     * @param extensionClassName
     *            The extension's fully-qualified class name.
     * @return An instance of a plugin extension.
     */
    PluginExtension loadExtension(final String extensionClassName) {
        try {
            return (PluginExtension) load(extensionClassName);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Load a plugin class. Requires the plugin's fully-qualified class name.
     * 
     * @param pluginClassName
     *            The plugin's fully-qualified class name.
     */
    Plugin loadPlugin(final String pluginClassName) {
        try {
            return (Plugin) load(pluginClassName);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Obtain a list of all of the jar files in the lib directory.
     * 
     * @return A <code>List&lt;URL&gt;</code>.
     * @throws IOException
     */
    private List<URL> getLibs() throws IOException {
        final List<URL> libs = new ArrayList<URL>();
        final File[] libFiles = fileSystem.list("lib", JAR_FILE_FILTER);
        for (final File libFile : libFiles) {
            libs.add(libFile.toURL());
        }
        return libs;
    }

    /**
     * Obtain a list of all of the resources in the root directory.
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
     * Create a class loader. Load all jar files in the lib directory; as well
     * as all resources in the root of the file system.
     * 
     * @return A class loader.
     * @throws IOException
     */
    private ClassLoader lazyCreateClassLoader() throws IOException {
        if (null == classLoader) {
            final List<URL> classpath = new ArrayList<URL>();
            classpath.addAll(getLibs());
            classpath.addAll(getResources());
            classLoader = new URLClassLoader(classpath.toArray(new URL[] {}));
        }
        return classLoader;
    }

    /**
     * Load a class. Requires the class have a constructor taking an instance of
     * <code>PluginServices</code>.
     * 
     * @param name
     *            A class name.
     * @param pluginServices
     *            A copy of plugin services to pass to the constructor.
     * @return An instance of a class.
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private Object load(final String name) throws IOException,
            ClassNotFoundException, NoSuchMethodException,
            InvocationTargetException, IllegalAccessException,
            InstantiationException {
        final Class loadedClass = lazyCreateClassLoader().loadClass(name);
        final Constructor constructor =
            loadedClass.getConstructor(new Class[] {});
        return constructor.newInstance(new Object[] {});
    }
}
