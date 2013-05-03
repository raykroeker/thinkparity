/*
 * Created On:  29-Sep-07 3:24:52 PM
 */
package com.thinkparity.adriana.backup.model.util;

import java.io.File;

/**
 * <b>Title:</b>thinkParity Adriana Backup Model Session<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Session {

    /** The command directory. */
    private File commandDirectory;

    /** A session id. */
    private String id;

    /** The amazon s3 storage bucket. */
    private String s3BucketName;

    /** The working directory. */
    private File workingDirectory;

    /**
     * Create Session.
     *
     */
    public Session() {
        super();
    }

    /**
     * Obtain the command directory.
     * 
     * @return A <code>File</code>.
     */
    public File getCommandDirectory() {
        return commandDirectory;
    }

    /**
     * Obtain the id.
     *
     * @return A <code>String</code>.
     */
    public String getId() {
        return id;
    }

    /**
     * Obtain the s3 bucket name.
     * 
     * @return A <code>String</code>.
     */
    public String getS3BucketName() {
        return s3BucketName;
    }

    /**
     * Obtain the workingDirectory.
     *
     * @return A <code>File</code>.
     */
    public File getWorkingDirectory() {
        return workingDirectory;
    }

    /**
     * Set the id.
     *
     * @param id
     *		A <code>String</code>.
     */
    public void setId(final String id) {
        this.id = id;
    }

    /**
     * Set the s3BucketName.
     *
     * @param s3BucketName
     *		A <code>String</code>.
     */
    public void setS3BucketName(final String s3BucketName) {
        this.s3BucketName = s3BucketName;
    }

    /**
     * Set the workingDirectory.
     *
     * @param workingDirectory
     *		A <code>File</code>.
     */
    public void setWorkingDirectory(final File workingDirectory) {
        this.workingDirectory = workingDirectory;
    }
}
