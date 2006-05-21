/*
 * Created On: Sat May 20 2006 09:42 PDT
 * $Id$
 */
package com.thinkparity.model.parity.model.release;

import java.io.File;

import com.thinkparity.codebase.OS;
import com.thinkparity.codebase.OSUtil;
import com.thinkparity.codebase.StringUtil;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.Constants;
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
     * Initialize the filesystem root for a release.
     * 
     * @param release
     *            A release.
     * @return A directory.
     */
    private static File initRoot(final Release release) {
        final String relPath =
            StringUtil.searchAndReplace(release.getGroupId(), ".", File.separator)
            .append(File.separator).append(release.getArtifactId())
            .append(File.separator).append(release.getVersion())
            .append(File.separator).toString();
        final File root = new File(Constants.Directories.DOWNLOAD_ROOT, relPath);
        if(!root.exists()) {
            Assert.assertTrue(
                    "[FILE SYSTEM HELPER] [CANNOT INITIALIZE]", root.mkdirs());
        }
        return root;
    }

    /** The release file system directories. */
    private File core, lib, libNative;

    /** The release file system root. */
    private final File root;

    /**
     * Create DirectoryHelper.
     *
     * @param release
     *      A release.
     */
    FileSystemHelper(final Context context, final Release release) {
        super();
        this.root = FileSystemHelper.initRoot(release);
    }

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

    private File initCore() {
        if(null == core) { core = initDir(root, Constants.DirectoryNames.CORE); }
        return core;
    }

    private File initDir(final File parent, final String child) {
        final File dir = new File(parent, child);
        if(!dir.exists()) { Assert.assertTrue("", dir.mkdir()); }
        return dir;
    }

    private File initLib() {
        if(null == lib) { lib = initDir(root, Constants.DirectoryNames.LIB); }
        return lib;
    }

    private File initLibNative() {
        if(null == libNative) {
            if(isWin32()) {
                libNative = initDir(lib, Constants.DirectoryNames.LIB_NATIVE_WIN32);
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
}