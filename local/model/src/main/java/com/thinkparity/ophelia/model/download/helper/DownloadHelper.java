/*
 * Created On: 
 * $Id$
 */
package com.thinkparity.ophelia.model.download.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Properties;

import com.thinkparity.codebase.FileUtil;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.model.migrator.Library;
import com.thinkparity.codebase.model.migrator.Release;


import com.thinkparity.ophelia.model.AbstractModelImplHelper;
import com.thinkparity.ophelia.model.Context;
import com.thinkparity.ophelia.model.library.InternalLibraryModel;
import com.thinkparity.ophelia.model.library.LibraryModel;

/**
 * The download helper is used by various methods in the release model impl.  It
 * contains the directory structure for a release; as well as some other helper
 * routines for library file location.
 *
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class DownloadHelper extends AbstractModelImplHelper {

    private static final String MANIFEST_HEADER =
        "lBrowserBootstrap:  Manifest";

    /** A file system helper. */
    private final FileSystemHelper fsHelper;

    /** A parity internal library interface. */
    private final InternalLibraryModel ilModel;

    private final Properties manifest;

    /** A parity release. */
    private final Release release;

    public DownloadHelper(final Context context, final Release release) {
        super();
        this.fsHelper = FileSystemHelper.getDownloadHelper(release);
        this.ilModel = LibraryModel.getInternalModel(context);
        this.release = release;
        this.manifest = new Properties();
    }

    /**
     * Determine if a download is complete.
     * 
     * @return True if the download is complete; false otherwise.
     * @throws IOException
     */
    public Boolean isComplete() throws IOException {
        if(getManifestFile().exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(getManifestFile());
                manifest.load(new FileInputStream(getManifestFile()));
                File libraryFile;
                for(final Object key : manifest.keySet()) {
                    libraryFile = new File(fsHelper.getRoot(), manifest.getProperty((String) key));
                    if(!libraryFile.exists()) { return Boolean.FALSE; }
                }
                return Boolean.TRUE;
            }
            finally { fis.close(); }
        }
        else { return Boolean.FALSE; }
    }

    /**
     * Determine if the download has been started.
     * 
     * @return True if the download has been started; false otherwise.
     */
    public Boolean isStarted() { return getManifestFile().exists(); }

    /**
     * Resume the download. This will resume a download where it left off.
     * 
     * @throws IOException
     */
    public void resume() throws IOException {
        Assert.assertTrue(
                "[LMODEL] [DOWNLOAD MODEL] [DOWNLOAD RESUME] [DOWNLOAD NOT YET STARTED]",
                isStarted());
        Assert.assertNotTrue(
                "[LMODEL] [DOWNLOAD MODEL] [DOWNLOAD RESUME] [DOWNLOAD COMPLETE]",
                isComplete());
        File downloadFile;
        Long libraryId;
        for(final Object key : manifest.keySet()) {
            downloadFile = new File(fsHelper.getRoot(), manifest.getProperty((String) key));
            if(!downloadFile.getParentFile().exists()) {
                Assert.assertTrue("[DOWNLOAD HELPER] [RESUME] [CANNOT CREATE NEW FILE]",
                        downloadFile.getParentFile().mkdir());
            }
            libraryId = parseManifestKeyForId((String) key);
            if(!downloadFile.exists()) {
                logger.debug(MessageFormat.format(
                        "[LMODEL] [DOWNLOAD MODEL] [DOWNLOAD RESUME] [DOWNLOADING LIBRARY {0}]",
                        new Object[] {libraryId}));
                Assert.assertTrue(MessageFormat.format(
                        "[LMODEL] [DOWNLOAD MODEL] [DOWNLOAD RESUME] [LIBRARY FILE {0} CANNOT BE CREATED]",
                        new Object[] {downloadFile.getAbsolutePath()}),
                        downloadFile.createNewFile());
                FileUtil.writeBytes(downloadFile, ilModel.readBytes(libraryId).getBytes());
                Assert.assertTrue(MessageFormat.format(
                        "[LMODEL] [DOWNLOAD MODEL] [DOWNLOAD RESUME] [LIBRARY FILE {0} CANNOT MARKED AS READ ONLY]",
                        new Object[] {downloadFile.getAbsolutePath()}),
                        downloadFile.setReadOnly());
            }
            else {
                logger.debug(MessageFormat.format(
                        "[LMODEL] [DOWNLOAD MODEL] [DOWNLOAD RESUME] [LIBRARY {0} ALREADY DOWNLOADED]",
                        new Object[] {libraryId}));
            }
        }
    }

    /**
     * This will start the download of the list of libraries.
     * 
     * @param libraries
     *            A list of libraries.
     * @throws IOException
     */
    public void start(final List<Library> libraries) throws IOException {
        Assert.assertNotTrue(
                "[LMODEL] [DOWNLOAD MODEL] [DOWNLOAD START] [DOWNLOAD ROOT ALREADY EXISTS]",
                fsHelper.getRoot().exists());
        Assert.assertTrue(
                "[LMODEL] [DOWNLOAD MODEL] [DOWNLOAD START] [DOWNLOAD ROOT CANNOT BE CREATED]",
                fsHelper.getRoot().mkdirs());
        for(final Library library : libraries) {
            manifest.setProperty(getManifestKey(library), getManifestValue(library));
        }
        final FileOutputStream fos = new FileOutputStream(getManifestFile());
        try { manifest.store(fos, MANIFEST_HEADER); }
        finally {
            fos.flush();
            fos.close();
        }
    }

    /**
     * Obtain the manifest file for the download.
     * 
     * @return A file.
     */
    private File getManifestFile() {
        return new File(fsHelper.getRoot(), release.getVersion() + ".download");
    }

    /**
     * Obtain a manifest key for a library.
     * 
     * @param library
     *            A library.
     * @return A manifest key.
     */
    private String getManifestKey(final Library library) {
        return new StringBuffer()
            .append(library.getId())
            .append(":").append(library.getGroupId())
            .append(":").append(library.getArtifactId())
            .append(":").append(library.getVersion())
            .toString();
    }

    /**
     * Obtain a manifest value for a library.
     * 
     * @param library
     *            A library.
     * @return A manifest value.
     */
    private String getManifestValue(final Library library) {
        return library.getPath();
    }

    /**
     * Parse a manifest key for the library's id.
     * 
     * @param manifestKey
     *            A manifest key.
     * @return The library's id.
     */
    private Long parseManifestKeyForId(final String manifestKey) {
        return Long.valueOf(manifestKey.substring(0, manifestKey.indexOf(":")));
    }
}
