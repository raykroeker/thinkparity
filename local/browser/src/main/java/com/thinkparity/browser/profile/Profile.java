/*
 * Created On: Fri Jun 02 2006 15:47 PDT
 * $Id$
 */
package com.thinkparity.browser.profile;

import java.io.File;

import com.thinkparity.codebase.FileSystem;
import com.thinkparity.codebase.FileUtil;
import com.thinkparity.codebase.assertion.Assert;

/**
 * A profile is a single configuration that can be opened by the browser.
 *
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class Profile {

    /** The profile file system. */
    private FileSystem fileSystem;

    /** Create Profile. */
    Profile() { super(); }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(final Object obj) {
        if(null != obj && obj instanceof Profile) {
            return ((Profile) obj).fileSystem.equals(fileSystem);
        }
        return false;
    }

    /**
     * Obtain the name of the profile.
     *
     * @return The profile name.
     */
    public String getName() { return fileSystem.getRoot().getName(); }

    /**
     * Obtain the profile archive path.
     * 
     * @return The profile's archive path.
     */
    public String getParityArchive() {
        final File archive = new File(fileSystem.getRoot(), "Archive");
        return archive.getAbsolutePath();
    }

    /**
     * Obtain the profile's workspace path.
     * 
     * @return The profile's workspace path.
     */
    public String getParityWorkspace() {
        return fileSystem.getRoot().getAbsolutePath();
    }

    /** @see java.lang.Object#hashCode() */
    public int hashCode() { return fileSystem.hashCode(); }

    /** @see java.lang.Object#toString() */
    public String toString() { return getName(); }

    /**
     * Delete the profile.
     *
     */
    void delete() { FileUtil.deleteTree(fileSystem.getRoot()); }

    /**
     * Rename the profile.
     * 
     * @param name
     *            The new name.
     */
    void rename(final String name) {
        final File targetRoot = new File(fileSystem.getRoot().getParentFile(), name);
        Assert.assertTrue(
                "[LBROWSER] [PROFILE] [CANNOT RENAME PROFILE]",
                fileSystem.getRoot().renameTo(targetRoot));
        setFileSystem(new FileSystem(targetRoot));
    }

    /**
     * Set fileSystem.
     *
     * @param fileSystem The FileSystem.
     */
    void setFileSystem(final FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }
}
