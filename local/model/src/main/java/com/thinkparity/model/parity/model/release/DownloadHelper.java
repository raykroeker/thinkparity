/*
 * Created On: Fri May 19 2006 19:58 PDT
 * $Id$
 */
package com.thinkparity.model.parity.model.release;

import java.io.File;
import java.io.IOException;

import com.thinkparity.codebase.FileUtil;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.model.Context;
import com.thinkparity.model.parity.model.library.InternalLibraryModel;
import com.thinkparity.model.parity.model.library.LibraryModel;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.Release;

/**
 * The download helper is used by various methods in the release model impl.  It
 * contains the directory structure for a release; as well as some other helper
 * routines for library file location.
 *
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
class DownloadHelper {

    /** A parity internal library interface. */
    private final InternalLibraryModel ilModel;

    /** A file system helper. */
    private final FileSystemHelper fsHelper;

    /**
     * Create DownloadHelper.
     *
     * @param release
     *      A release.
     */
    DownloadHelper(final Context context, final Release release) {
        super();
        this.fsHelper = new FileSystemHelper(context, release);
        this.ilModel = LibraryModel.getInternalModel(context);
    }

    /**
     * Determine whether or not the library has been downloaded.
     *
     * @param library
     *      A library.
     * @return True if the library has been downloaded.
     */
    Boolean isDownloaded(final Library library) {
        final File libraryFile = fsHelper.getFile(library);
        return libraryFile.exists()
                && libraryFile.canRead()
                && !libraryFile.canWrite();
    }

    /**
     * Download a library.
     *
     * @param library
     *      A library.
     */
    void download(final Library library) throws IOException {
        final File libraryFile = fsHelper.getFile(library);
        FileUtil.writeBytes(libraryFile, autobox(ilModel.readBytes(library.getId())));
        // TODO Download a checksum
        Assert.assertTrue(
            "[RMODEL] [RELEASE] [DOWNLOAD HELPER] [CANNOT MARK LIBRARY AS READ ONLY]",
            libraryFile.setReadOnly());
    }

    /**
     * Using the JDK's autoboxing functionality convert an object byte array to
     * an primitve byte array.
     * 
     * @param bytes
     *            An object byte array.
     * @return A primitive byte array.
     */
    private byte[] autobox(final Byte[] bytes) {
        final byte[] boxed = new byte[bytes.length];
        for(int i = 0; i < bytes.length; i++) boxed[i] = bytes[i];
        return boxed;
    }


}
