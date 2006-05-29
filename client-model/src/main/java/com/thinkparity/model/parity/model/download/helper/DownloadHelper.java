/*
 * Created On: 
 * $Id$
 */
package com.thinkparity.model.parity.model.download.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import com.thinkparity.codebase.FileUtil;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.model.AbstractModelImplHelper;
import com.thinkparity.model.parity.model.Context;
import com.thinkparity.model.parity.model.library.InternalLibraryModel;
import com.thinkparity.model.parity.model.library.LibraryModel;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.LibraryBytes;
import com.thinkparity.migrator.Release;

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
     * Download a library.
     *
     * @param library
     *      A library.
     */
    public void download(final Library library) throws IOException {
        final File libraryFile = fsHelper.getFile(library);
        LibraryBytes libraryBytes = ilModel.readBytes(library.getId());
        FileUtil.writeBytes(libraryFile, libraryBytes.getBytes());
        libraryBytes = null;
        Assert.assertTrue(
            "[RMODEL] [RELEASE] [DOWNLOAD HELPER] [CANNOT MARK LIBRARY AS READ ONLY]",
            libraryFile.setReadOnly());
    }

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
     * Determine whether or not the library has been downloaded.
     *
     * @param library
     *      A library.
     * @return True if the library has been downloaded.
     */
    public Boolean isDownloaded(final Library library) {
        if(isStarted()) {
            final File libraryFile = fsHelper.getFile(library);
            return libraryFile.exists()
                    && libraryFile.canRead()
                    && !libraryFile.canWrite();
        }
        else { return Boolean.FALSE; }
    }

    public Boolean isStarted() { return fsHelper.getRoot().exists(); }

    public void resume() throws IOException {
        Assert.assertTrue(
                "[DOWNLOAD HELPER] [RESUME] [DOWNLOAD NOT YET STARTED]",
                isStarted());
        Assert.assertNotTrue(
                "[DOWNLOAD HELPER] [RESUME] [DOWNLOAD COMPLETE]",
                isComplete());
        File downloadFile;
        Long libraryId;
        for(final Object key : manifest.keySet()) {
            downloadFile = new File(fsHelper.getRoot(), manifest.getProperty((String) key));
            if(!downloadFile.getParentFile().exists()) {
                Assert.assertTrue("[DOWNLOAD HELPER] [RESUME] [CANNOT CREATE NEW FILE]",
                        downloadFile.getParentFile().mkdir());
            }
            if(!downloadFile.exists()) {
                Assert.assertTrue("[DOWNLOAD HELPER] [RESUME] [CANNOT CREATE NEW FILE]",
                        downloadFile.createNewFile());
                libraryId = parseManifestKeyForId((String) key);
                FileUtil.writeBytes(downloadFile, ilModel.readBytes(libraryId).getBytes());
                Assert.assertTrue(
                    "[RMODEL] [RELEASE] [DOWNLOAD HELPER] [CANNOT MARK LIBRARY AS READ ONLY]",
                    downloadFile.setReadOnly());
            }
        }
    }

    public void start(final List<Library> libraries) throws IOException {
        Assert.assertNotTrue(
                "[DOWNLOAD HELPER] [START] [DOWNLOAD ROOT ALREADY EXISTS]",
                fsHelper.getRoot().exists());
        Assert.assertTrue(
                "[DOWNLOAD HELPER] [START] [CANNOT CREATE DOWNLOAD ROOT]",
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

    private File getManifestFile() {
        return new File(
                fsHelper.getRoot(), release.getVersion() + ".download");
    }

    private String getManifestKey(final Library library) {
        return new StringBuffer()
            .append(library.getId())
            .append(":").append(library.getGroupId())
            .append(":").append(library.getArtifactId())
            .append(":").append(library.getVersion())
            .toString();
    }

    private String getManifestValue(final Library library) {
        return library.getPath();
    }

    private Long parseManifestKeyForId(final String manifestKey) {
        return Long.valueOf(manifestKey.substring(0, manifestKey.indexOf(":")));
    }
}
