/*
 * Created On: Sat May 27 2006 10:07 PDT
 * $Id$
 */
package com.thinkparity.ophelia.model.download.helper;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.MessageFormat;

import com.thinkparity.codebase.FileUtil;
import com.thinkparity.codebase.OS;
import com.thinkparity.codebase.OSUtil;
import com.thinkparity.codebase.StringUtil;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.model.migrator.Library;
import com.thinkparity.codebase.model.migrator.Release;


import com.thinkparity.ophelia.model.AbstractModelImplHelper;
import com.thinkparity.ophelia.model.Constants.Directories;
import com.thinkparity.ophelia.model.Constants.DirectoryNames;

/**
 * A helper class to manage a virtual filesystem for a release.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class FileSystemHelper extends AbstractModelImplHelper {

    /**
     * Obtain a file system helper for a downloaded release.
     * 
     * @return A release file system helper.
     */
    public static FileSystemHelper getDownloadHelper(final Release release) {
        final File root = new File(Directories.DOWNLOAD, release.getVersion());
        return new FileSystemHelper(root);
    }

    /**
     * Obtain a file system helper for the current installation.
     * 
     * @return An install file system helper.
     */
    public static FileSystemHelper getInstallHelper(final Release release) {
        final File root = new File(Directories.INSTALL, release.getVersion());
        return new FileSystemHelper(root);
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
    private FileSystemHelper(final File root) {
        super();
        this.root = root;
    }

    /** Delete the file system tree starting at the root. */
    public void deleteTree() { FileUtil.deleteTree(root); }

    public File[] getCoreFiles() { return initCore().listFiles(); }

    /**
     * Obtain the local file for the library.
     *
     * @param library
     *      A library.
     * @return A local file.
     */
    public File getFile(final Library library) {
        final StringBuffer localPath =
            StringUtil.searchAndReplace(library.getPath(), "/", File.separator);
        return new File(root, localPath.toString());
    }

    public File[] getLibFiles() { return initLib().listFiles(); }

    public File[] getLibNativeFiles() { return initLibNative().listFiles(); }

    /** Obtain the file system root. */
    public File getRoot() { return root; }

    /**
     * Install a core file.
     * 
     * @param coreFile
     *            A core file.
     */
    public void installCoreFile(final File coreFile) throws IOException {
        final File targetCoreFile = new File(initCore(), coreFile.getName());
        if(targetCoreFile.exists()) {
            Assert.assertTrue(
                    MessageFormat.format(
                            "[FILE SYSTEM HELPER] [INSTALL CORE FILE] [CANNOT DELETE TARGET CORE FILE] [{0}]",
                            new Object[] {targetCoreFile.getAbsolutePath()}),
                    targetCoreFile.delete());
        }
        FileUtil.copy(coreFile, targetCoreFile);
    }

    /**
     * Synchronize the file system with another.
     * 
     * @param fsHelper
     *            The file system to synchronize with.
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void synchronize(final FileSystemHelper fsHelper)
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

    private void backupDirectory(final File directory, final File backupDirectory) throws FileNotFoundException, IOException {
        File backupChild;
        for(final File file : directory.listFiles()) {
            if(file.isFile()) {
                FileUtil.copy(file, new File(backupDirectory, file.getName()));
            }
            else if(file.isDirectory()) {
                backupChild = new File(backupDirectory, file.getName());
                Assert.assertTrue(
                        MessageFormat.format(
                                "[FILE SYSTEM HELPER] [BACKUP DIRECTORY] [CANNOT CREATE BACKUP CHILD] [{0}]",
                                new Object[] {backupChild.getAbsolutePath()}),
                        backupChild.mkdir());
                backupDirectory(file, backupChild);
            }
        }
    }

    private File initCore() {
        if(null == core) { core = initDir(root, DirectoryNames.CORE); }
        return core;
    }

    private File initDir(final File parent, final String child) {
        final File dir = new File(parent, child);
        if(!dir.exists()) {
            Assert.assertTrue(
                MessageFormat.format(
                        "[FILE SYSTEM HELPER] [INIT DIR] [CANNOT CREATE DIR] [{0}]",
                        new Object[] {dir.getAbsolutePath()}),
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
                Assert.assertUnreachable(
                        MessageFormat.format(
                                "[FILE SYSTEM HELPER] [UNSUPPORTED OS] [{0}]",
                                new Object[] {OSUtil.getOS().toString()}));
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
                MessageFormat.format(
                        "[FILE SYSTEM HELPER] [SYNC DIR] [CANNOT CREATE TARGET BACKUP] [{0}]",
                        new Object[] {targetBackup.getAbsolutePath()}),
                targetBackup.mkdir());
        backupDirectory(target, targetBackup);
        // copy from source to target
        final File[] sourceFiles = source.listFiles();
        File targetFile;
        for(final File sourceFile : sourceFiles) {
            targetFile = new File(target, sourceFile.getName());
            if(targetFile.exists()) {
                Assert.assertTrue(
                        MessageFormat.format("CANNOT DELETE TARGET:  {0}",
                                new Object[] {targetFile.getAbsolutePath()}),
                                targetFile.delete());
            }
            FileUtil.copy(sourceFile, targetFile);
        }
    }
}
