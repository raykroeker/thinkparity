/*
 * Created On: Sat May 20 2006 09:42 PDT
 * $Id$
 */
package com.thinkparity.model.parity.model.release;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.thinkparity.codebase.FileUtil;
import com.thinkparity.codebase.OS;
import com.thinkparity.codebase.OSUtil;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.Constants.Directories;
import com.thinkparity.model.Constants.DirectoryNames;
import com.thinkparity.model.parity.model.Context;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.Release;

/**
 * A helper class to manage a virtual filesystem for a release.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
class FileSystemHelper {

    /**
     * Initialize the download root directory.
     * 
     * @return The download root.
     */
    private static File initDownloadRoot() {
        final File downloadRoot = Directories.DOWNLOAD;
        if(!downloadRoot.exists()) {
            Assert.assertTrue(
                    "[FILE SYSTEM HELPER] [CANNOT INITIALIZE DOWNLOAD ROOT]",
                    downloadRoot.mkdir());
        }
        return downloadRoot;
    }

    /**
     * Initialize the file system root.
     * 
     * @return The file system root directory.
     */
    private static File initInstallRoot() {
        final File root = Directories.INSTALL;
        Assert.assertTrue(
                "[FILE SYSTEM HELPER] [CANNOT INITIALIZE INSTALL ROOT]",
                root.exists() && root.canRead() && root.canWrite());
        return root;
    }

    /**
     * Initialize the downloaded release root directory.
     * 
     * @param release
     *            A release.
     * @return A release download root.
     */
    private static File initReleaseRoot(final Release release) {
        final File root = new File(initDownloadRoot(), release.getVersion());
        if(!root.exists()) {
            Assert.assertTrue(
                    "[FILE SYSTEM HELPER] [CANNOT INITIALIZE RELEASE DOWNLOAD ROOT]",
                    root.mkdir());
        }
        return root;
    }

    /** The release file system directories. */
    private File core, lib, libNative;

    /** The release file system root. */
    private final File root;

    /**
     * Create FileSystemHelper.
     * 
     * @param context
     *            A parity calling context.
     */
    FileSystemHelper(final Context context) {
        super();
        this.root = FileSystemHelper.initInstallRoot();
    }

    /**
     * Create FileSystemHelper.
     * 
     * @param context
     *            A parity calling context.
     * @param release
     *            A release.
     */
    FileSystemHelper(final Context context, final Release release) {
        super();
        this.root = FileSystemHelper.initReleaseRoot(release);
    }

    /** Obtain the root of the download file system. */
    File getDownloadRoot() { return FileSystemHelper.initDownloadRoot(); }

    /**
     * Obtain the local file for the library.
     *
     * @param library
     *      A library.
     * @return A local file.
     */
    File getFile(final Library library) {
        if(library.isCore()) {
            return new File(initCore(), library.getFilename());
        }
        else {
            switch(library.getType()) {
                case JAVA:
                    return new File(initLib(), library.getFilename());
                case NATIVE:
                    return new File(initLibNative(), library.getFilename());
                default:
                    throw Assert.createUnreachable("[FILE SYSTEM HELPER] [UNKNOWN LIBRARY TYPE]");
            }
        }
    }

    /**
     * Synchronize the file system with another.
     * 
     * @param fsHelper
     *            The file system to synchronize with.
     * @throws FileNotFoundException
     * @throws IOException
     */
    void synchronize(final FileSystemHelper fsHelper)
            throws FileNotFoundException, IOException {
        synchronizeDir(fsHelper.initCore(), initCore());
        synchronizeDir(fsHelper.initLib(), initLib());
        final File[] backups = root.listFiles(new FileFilter() {
            public boolean accept(final File pathname) {
                final String name = pathname.getName();
                if(pathname.isDirectory()) {
                    if(name.equals("." + DirectoryNames.BIN)) { return true; }
                    if(name.equals("." + DirectoryNames.CORE)) { return true; }
                    if(name.equals("." + DirectoryNames.LIB)) { return true; }
                }
                return false;
            }
        });
        for(final File backup : backups) { FileUtil.deleteTree(backup); }
    }

    private File initCore() {
        if(null == core) { core = initDir(root, DirectoryNames.CORE); }
        return core;
    }

    private File initDir(final File parent, final String child) {
        final File dir = new File(parent, child);
        if(!dir.exists()) {
            Assert.assertTrue(
                "[FILE SYSTEM HELPER] [INIT DIR] [CANNOT CREATE DIR]",
                dir.mkdir());
        }
        return dir;
    }

    private File initLib() {
        if(null == lib) { lib = initDir(root, DirectoryNames.LIB); }
        return lib;
    }

    private File initLibNative() {
        if(null == libNative) {
            if(isWin32()) {
                libNative = initDir(lib, DirectoryNames.LIB_NATIVE_WIN32);
            }
            else {
                Assert.assertUnreachable("[RMODEL] [RELEASE] [DOWNLOAD HELPER] [UNSUPPORTED OS]");
            }
        }
        return libNative;
    }

    /**
     * Determine if the operating system is a win32 environment.
     *
     * @return True if the operating system is windows 2000 or windows xp.
     */
    private Boolean isWin32() {
        final OS os = OSUtil.getOS();
        return OS.WINDOWS_2000 == os || OS.WINDOWS_XP == os;
    }

    /**
     * Synchronize the source with the target directory.
     * 
     * @param source
     *            A directory.
     * @param target
     *            A directory.
     * @throws FileNotFoundException
     * @throws IOException
     */
    private void synchronizeDir(final File source, final File target)
            throws FileNotFoundException, IOException {
        // create a backup of the target
        final File targetBackup = new File(target.getParentFile(), "." + target.getName());
        Assert.assertTrue(
                "[FILE SYSTEM HELPER] [SYNC DIR] [CANNOT RENAME TARGET]",
                target.renameTo(targetBackup));
        // re-create the target
        Assert.assertTrue(
                "[FILE SYSTEM HELPER] [SYNC DIR] [CANNOT CREATE TARGET]",
                target.mkdir());
        // copy from source to target
        final File[] sourceFiles = source.listFiles();
        File targetFile;
        for(final File sourceFile : sourceFiles) {
            targetFile = new File(target, sourceFile.getName());
            if(targetFile.exists()) {
                Assert.assertTrue("[FILE SYSTEM HELPER] [SYNC DIR] [CANNOT DELETE TARGET]", targetFile.delete());
            }
            FileUtil.copy(sourceFile, targetFile);
        }
    }
}