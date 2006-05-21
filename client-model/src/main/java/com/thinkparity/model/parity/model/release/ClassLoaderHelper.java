/*
 * Created On: May 20, 2006 10:39:14 AM
 * $Id$
 */
package com.thinkparity.model.parity.model.release;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedList;
import java.util.List;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.model.Context;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.Release;

/**
 * The class loader helper creates a class loader for a set of libraries in a
 * given release.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
class ClassLoaderHelper {

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
