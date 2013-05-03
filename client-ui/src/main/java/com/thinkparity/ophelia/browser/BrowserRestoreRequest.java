/**
 * Created On: 6-Nov-07 11:09:02 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser;

import java.io.File;
import java.io.IOException;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.ophelia.browser.Constants.DirectoryNames;
import com.thinkparity.ophelia.browser.Constants.FileNames;
import com.thinkparity.ophelia.browser.profile.Profile;

/**
 * @author robert@thinkparity.com
 * @version $Revision$
 */
public final class BrowserRestoreRequest {

    /** A singleton instance of <code>BrowserRestoreRequest</code>. */
    private static BrowserRestoreRequest INSTANCE;

    /**
     * Create an instance of <code>BrowserRestoreRequest</code>.
     * 
     * @param profile
     *            The <code>Profile</code>.
     * 
     * @return An instance of <code>BrowserRestoreRequest</code>.
     */
    public static BrowserRestoreRequest create(final Profile profile) {
        Assert.assertIsNull("The browser restore request has already been created.", INSTANCE);
        INSTANCE = new BrowserRestoreRequest(profile);
        return BrowserRestoreRequest.getInstance();
    }

    /**
     * Obtain an instance of <code>BrowserRestoreRequest</code>.
     * 
     * @return An instance of <code>BrowserRestoreRequest</code>.
     */
    public static BrowserRestoreRequest getInstance() {
        Assert.assertNotNull("The browser restore request has not yet been created.", INSTANCE);
        return INSTANCE;
    }

    /** The last modified time <code>long</code>. */
    private long lastModified;

    /** An apache logger. */
    private final Log4JWrapper logger;

    /** The browser restore request file <code>File</code>. */
    private final File restoreRequestFile;

    /** The browser restore request path <code>File</code>. */
    private final File restoreRequestPath;

    /**
     * Create a BrowserRestoreRequest.
     * 
     * @param profile
     *            The <code>Profile</code>.
     */
    private BrowserRestoreRequest(final Profile profile) {
        super();
        this.logger = new Log4JWrapper(getClass());
        this.restoreRequestPath = getRestoreRequestPath(profile);
        this.restoreRequestFile = getRestoreRequestFile(restoreRequestPath);
        this.lastModified = System.currentTimeMillis();
    }

    /**
     * Determine if there is a browser restore request.
     * 
     * This method is called often and should be fast.
     * The method is 3 or 4 times faster when the file does not yet exist.
     * 
     * If this method fails because an exception is thrown then the consequence is
     * the browser will not be restored, but hopefully the next call will succeed,
     * thus delaying the restore by one timer interval.
     * 
     * @return True if there is a browser restore request.
     */
    public Boolean isBrowserRestoreRequest() {
        Boolean exists;
        try {
            exists = restoreRequestFile.exists();
        } catch (final SecurityException se) {
            logger.logError(se, "Browser restore request could not determine file {0} existence.", restoreRequestFile);
            return Boolean.FALSE;
        }
        if (exists) {
            return isModified(restoreRequestFile);
        } else {
            return Boolean.FALSE;
        }
    }

    /**
     * Set the browser restore request.
     * 
     * @param browserRestoreRequest
     *            A browser restore request <code>Boolean</code>.
     */
    public void setBrowserRestoreRequest(final Boolean browserRestoreRequest) {
        if (browserRestoreRequest) {
            updateRestoreRequestFile();
        } else {
            // the time stamp is set rather than deleting the file, because deleting
            // the file can occasionally fail which in this case is bad.
            this.lastModified = System.currentTimeMillis();
        }
    }

    /**
     * Get the restore request file.
     * 
     * @param restoreRequestPath
     *            A restore request path <code>File</code>.
     * @return The <code>File</code> restore request file.
     */
    private File getRestoreRequestFile(final File restoreRequestPath) {
        return new File(restoreRequestPath, FileNames.Workspace.RESTORE_REQUEST);
    }

    /**
     * Get the restore request path.
     * 
     * @param profile
     *            A <code>Profile</code>.
     * @return The <code>File</code> restore request path.
     */
    private File getRestoreRequestPath(final Profile profile) {
        return new File(profile.getParityWorkspace(), DirectoryNames.Workspace.TEMP);
    }

    /**
     * Determine if the file has been modified.
     * 
     * @param file
     *            A <code>File</code>.
     * @return True if the file has been modified.
     */
    private Boolean isModified(final File file) {
        return (file.lastModified() > this.lastModified);
    }

    /**
     * Create the restore request file if it does not exist, and timestamp it.
     * 
     * If this method fails because an exception is thrown then the consequence is
     * the browser will not be restored, however, the user can try again.
     */
    private void updateRestoreRequestFile() {
        try {
            if (!restoreRequestPath.exists()) {
                if (!restoreRequestPath.mkdir()) {
                    logger.logError("Browser restore request could not create path {0}.", restoreRequestPath);
                    return;
                }
            }
            if (!restoreRequestFile.exists()) {
                if (!restoreRequestFile.createNewFile()) {
                    logger.logError("Browser restore request could not create file {0}.", restoreRequestFile);
                    return;
                }
            }
            if (!restoreRequestFile.setLastModified(System.currentTimeMillis())) {
                logger.logError("Browser restore request could not timestamp file {0}.", restoreRequestFile);
            }
        } catch (final IOException iox) {
            logger.logError(iox, "Browser restore request could not create file {0}.", restoreRequestFile);
        } catch (final SecurityException se) {
            logger.logError(se, "Browser restore request could not create file {0}.", restoreRequestFile);
        }
    }
}
