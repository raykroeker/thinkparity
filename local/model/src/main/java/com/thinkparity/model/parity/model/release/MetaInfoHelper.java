/*
 * Created On: May 21, 2006 9:07:02 AM
 * $Id$
 */
package com.thinkparity.model.parity.model.release;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import com.thinkparity.model.parity.model.Context;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.Release;


/**
 * Using a class path helper can retreive parity specifiy meta information.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
class MetaInfoHelper {

    /** A class loader helper. */
    private final ClassLoaderHelper clHelper;

    private final List<Library> libraries;

    /** Create MetaInfo. */
    MetaInfoHelper(final Context context, final Release release,
            final List<Library> libraries) {
        super();
        this.clHelper = new ClassLoaderHelper(context, release);
        this.libraries = libraries;
    }

    /**
     * Obtain the meta info for this library.
     * 
     * @param library
     *            A library.
     * @return Library meta info.
     * @throws IOException
     *             If the meta info configuration cannot be loaded.
     * @throws MalformedURLException
     *             If the library's class loader cannot be instantiated.
     */
    MetaInfo getMetaInfo(final Library library) throws IOException,
            MalformedURLException {
        return new MetaInfo(
                clHelper.getClassLoader(library),
                clHelper.getClassLoader(libraries));
    }
}
