/*
 * Dec 1, 2005
 */
package com.thinkparity.desdemona.model.session;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Calendar;

import com.thinkparity.codebase.Constants;
import com.thinkparity.codebase.FileSystem;
import com.thinkparity.codebase.assertion.Assert;

/**
 * <b>Title:</b>thinkParity DesdemonaModel Session<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Session {

    /** A session creation date <code>Calendar</code>. */
    private Calendar createdOn;

    /** A session expiry date <code>Calendar</code>. */
    private Calendar expiresOn;

    /** A session id <code>String</code>. */
    private String id;

    /** The user's <code>InetAddress</code>. */
    private InetAddress inetAddress;

    /** The user's temporary directory root. */
    private FileSystem tempFileSystem;

    /**
     * Create Session.
     * 
     */
    Session() {
        super();
    }

    /**
     * @see com.thinkparity.desdemona.model.session.Session#createTempDirectory()
     *
     */
    public File createTempDirectory() throws IOException {
        final String tempFileSuffix = new StringBuffer(".")
                .append(System.currentTimeMillis())
                .toString();
        return createTempDirectory(tempFileSuffix);
    }

    /**
     * @see com.thinkparity.desdemona.model.session.Session#createTempDirectory(java.lang.String)
     *
     */
    public File createTempDirectory(final String suffix) throws IOException {
        final File tempDirectory = new File(tempFileSystem.getRoot(), suffix);
        Assert.assertTrue(tempDirectory.mkdir(),
                "Could not create temp directory {0}.",
                tempDirectory.getAbsolutePath());
        return tempDirectory;
    }

    /**
     * @see com.thinkparity.desdemona.model.session.Session#createTempFile()
     *
     */
    public File createTempFile() throws IOException {
        final String tempFileSuffix = new StringBuffer(".")
                .append(System.currentTimeMillis())
                .toString();
        return createTempFile(tempFileSuffix);
    }

    /**
     * @see com.thinkparity.desdemona.model.session.Session#createTempFile(java.lang.String)
     *
     */
    public File createTempFile(final String suffix) throws IOException {
        return File.createTempFile(Constants.File.TEMP_FILE_PREFIX,
                suffix, tempFileSystem.getRoot());
    }

    /**
     * Obtain createdOn.
     *
     * @return A Calendar.
     */
    public Calendar getCreatedOn() {
        return createdOn;
    }

    /**
     * Obtain expiresOn.
     *
     * @return A Calendar.
     */
    public Calendar getExpiresOn() {
        return expiresOn;
    }

    /**
     * Obtain id.
     *
     * @return A String.
     */
    public String getId() {
        return id;
    }

    /**
     * @see com.thinkparity.desdemona.model.session.Session#getInetAddress()
     *
     */
    public InetAddress getInetAddress() {
        return inetAddress;
    }

    /**
     * Obtain tempFileSystem.
     * 
     * @return A FileSystem.
     */
    public FileSystem getTempFileSystem() {
        return tempFileSystem;
    }

    /**
     * Destroy the session.
     *
     */
    void destroy() {
        if (null != tempFileSystem) {
            tempFileSystem.deleteTree();
        }
    }

    /**
     * Set createdOn.
     *
     * @param createdOn
     *      A Calendar.
     */
    void setCreatedOn(final Calendar createdOn) {
        this.createdOn = createdOn;
    }

    /**
     * Set expiresOn.
     *
     * @param expiresOn
     *      A Calendar.
     */
    void setExpiresOn(final Calendar expiresOn) {
        this.expiresOn = expiresOn;
    }

    /**
     * Set id.
     *
     * @param id
     *      A String.
     */
    void setId(final String id) {
        this.id = id;
    }

    /**
     * Set inetAddress.
     * 
     * @param inetAddress
     *            An <code>InetAddress</code>.
     */
    void setInetAddress(final InetAddress inetAddress) {
        this.inetAddress = inetAddress;
    }

    /**
     * Set temp root.
     * 
     * @param tempRoot
     *            A temporary root directory <code>File</code>.
     */
    void setTempRoot(final File tempRoot) {
        this.tempFileSystem = new FileSystem(tempRoot);
    }
}
