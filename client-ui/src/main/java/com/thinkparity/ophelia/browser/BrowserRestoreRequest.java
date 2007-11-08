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

    /** An apache logger. */
    private final Log4JWrapper logger;

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

    /** The enabled <code>boolean</code>, used to disable all requests if an exception is thrown. */
    private boolean enabled;

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
        this.enabled = true;
    }

    /**
     * Determine if there is a browser restore request.
     * This method must be fast.
     * 
     * @return True if there is a browser restore request.
     */
    public Boolean isBrowserRestoreRequest() {
        Boolean request = Boolean.FALSE;
        if (enabled) {
            try {
                request = restoreRequestFile.exists();
            } catch (final SecurityException se) {
                logger.logError(se, "Browser restore request could not determine file {0} existence.", restoreRequestFile);
                enabled = false;
            }
        }
        return request;
    }

    /**
     * Set the browser restore request.
     * 
     * @param browserRestoreRequest
     *            A browser restore request <code>Boolean</code>.
     */
    public void setBrowserRestoreRequest(final Boolean browserRestoreRequest) {
        if (enabled && browserRestoreRequest != isBrowserRestoreRequest()) {
            if (browserRestoreRequest) {
                createRestoreRequestFile();
            } else {
                deleteRestoreRequestFile();
            }
        }
    }

    /**
     * Create the restore request file.
     */
    private void createRestoreRequestFile() {
        try {
            if (!restoreRequestPath.exists()) {
                if (!restoreRequestPath.mkdir()) {
                    logger.logError("Browser restore request could not create path {0}.", restoreRequestPath);
                    enabled = false;
                    return;
                }
            }
            if (!restoreRequestFile.createNewFile()) {
                logger.logError("Browser restore request could not create file {0}.", restoreRequestFile);
                enabled = false;
            }
        } catch (final IOException iox) {
            logger.logError(iox, "Browser restore request could not create file {0}.", restoreRequestFile);
            enabled = false;
        } catch (final SecurityException se) {
            logger.logError(se, "Browser restore request could not create file {0}.", restoreRequestFile);
            enabled = false;
        }
    }

    /**
     * Delete the restore request file.
     */
    private void deleteRestoreRequestFile() {
        try {
            if (!restoreRequestFile.delete()) {
                logger.logError("Browser restore request could not delete file {0}.", restoreRequestFile);
                enabled = false;
            }
        } catch(final SecurityException se) {
            logger.logError(se, "Browser restore request could not delete file {0}.", restoreRequestFile);
            enabled = false;
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
}
