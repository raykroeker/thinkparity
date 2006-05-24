/*
 * Created On: May 20, 2006 10:39:14 AM
 * $Id$
 */
package com.thinkparity.model.parity.model.release;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.model.Context;
import com.thinkparity.model.parity.model.release.helper.AbstractHelper;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.Release;

/**
 * The class loader helper creates a class loader for a set of libraries in a
 * given release.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
class ClassLoaderHelper extends AbstractHelper {

    /** A download helper. */
    private final DownloadHelper dHelper;

    /** A file system helper. */
    private final FileSystemHelper fsHelper;

    /** Create ClassLoaderHelper. */
    ClassLoaderHelper(final Context context, final Release release) {
        super();
        this.dHelper = new DownloadHelper(context, release);
        this.fsHelper = new FileSystemHelper(context, release);
    }

    /**
     * Obtain a class loader for a single library.
     * 
     * @param library
     *            A library.
     * @return A class loader.
     */
    ClassLoader getClassLoader(final Library library)
            throws MalformedURLException {
        final URL[] urls = new URL[] {getURL(library)};
        return getClassLoader(urls);
    }

    /**
     * Obtain a class loader for a list of libraries.
     * 
     * @param libraries
     *            A list of libraries.
     * @return A class loader.
     * @throws MalformedURLException
     */
    ClassLoader getClassLoader(final List<Library> libraries)
            throws MalformedURLException {
        final List<URL> urls = new LinkedList<URL>();
        for(final Library library : libraries) {
            Assert.assertTrue(
                    "[CLASS LOADER HELPER] [LIBRARY NOT YET DOWNLOADED]",
                    dHelper.isDownloaded(library));
            urls.add(getURL(library));
        }
        return getClassLoader(urls.toArray(new URL[] {}));
    }

    /**
     * Print the class path for each library to the default logger.
     * 
     * @param level
     *            The level at which to print.
     * @param libraries
     *            A list of libraries for which to compute the class path.
     */
    void printClassPath(final Level level, final List<Library> libraries) {
        printClassPath(logger, level, libraries);
    }

    /**
     * Print the class path for each library to a logger.
     * 
     * @param logger
     *            The logger to print to.
     * @param level
     *            The level at which to print.
     * @param libraries
     *            A list of libraries for which to compute the class path.
     */
    void printClassPath(final Logger logger, final Level level,
            final List<Library> libraries) {
        if(logger.isEnabledFor(level)) {
            final String message =  "[CLASS LOADER HELPER] [CLASS PATH ELEMENT] [{0}] [{1}]";
            for(final Library library : libraries) {
                Assert.assertTrue(
                        "[CLASS LOADER HELPER] [LIBRARY NOT YET DOWNLOADED]",
                        dHelper.isDownloaded(library));
                StringBuffer pathElement = new StringBuffer();
                try { pathElement.append(getURL(library).getFile()); }
                catch(final MalformedURLException murlx) {
                    pathElement.append(" - MALFORMED");
                }
    
                logger.log(level, MessageFormat.format(message, new Object[] {
                        library.toString(),
                        pathElement
                }));
            }
        }
    }

    /**
     * Create a class loader.
     * 
     * @param urls
     *            A list of urls.
     * @return A class loader.
     */
    private ClassLoader getClassLoader(final URL[] urls) {
        return URLClassLoader.newInstance(urls, null);
    }

    /**
     * Obtain the URL for the library.
     * 
     * @param library
     *            A library.
     * @return A url.
     * @throws MalformedURLException
     */
    private URL getURL(final Library library) throws MalformedURLException {
        return fsHelper.getFile(library).toURL();
    }
}
