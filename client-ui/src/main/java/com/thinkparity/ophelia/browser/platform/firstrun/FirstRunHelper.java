/*
 * Created On: Jun 10, 2006 10:26:01 AM
 */
package com.thinkparity.ophelia.browser.platform.firstrun;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.ophelia.browser.BrowserException;
import com.thinkparity.ophelia.browser.platform.Platform;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.model.session.LoginMonitor;
import com.thinkparity.ophelia.model.workspace.Workspace;
import com.thinkparity.ophelia.model.workspace.WorkspaceModel;


/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class FirstRunHelper {

    /** An apache logger. */
    final Logger logger;

    /** The login avatar. */
    private LoginAvatar loginAvatar;

    /** The first run window. */
    private FirstRunWindow window;

    /** The thinkParity <code>Workspace</code>. */
    private final Workspace workspace;

    /** The thinkParity workspace model. */
    private final WorkspaceModel workspaceModel;

    /** Create FirstRunHelper. */
    public FirstRunHelper(final Platform platform) {
        super();
        this.logger = platform.getLogger(getClass());
        this.workspace = platform.getModelFactory().getWorkspace(getClass());
        this.workspaceModel = platform.getModelFactory().getWorkspaceModel(getClass());
    }

    /**
     * Execute first run functionality for the browser platform.
     * 
     * @return True if first run completed.
     */
    public void firstRun() {
        loginAvatar = new LoginAvatar(this);
        openWindow(loginAvatar.getTitle(), loginAvatar);

        final String username = loginAvatar.getUsername();
        final String password = loginAvatar.getPassword();
        if(null != username && null != password) {
            final Credentials credentials = new Credentials();
            credentials.setPassword(password);
            credentials.setUsername(username);

            workspaceModel.initialize(workspace, new LoginMonitor() {
                public void notifyInvalidCredentials(final Credentials credentials) {
                }
                public Boolean confirmSynchronize() {
                    final ConfirmSynchronize confirmSynchronize = new ConfirmSynchronize();
                    openWindow("", confirmSynchronize);
                    return confirmSynchronize.didConfirm();
                }
            }, credentials);
        }
    }

    /**
     * Determine if this is the first time the platform has been run.
     * 
     * @return True if this is the first time the platform has been run.
     */
    public Boolean isFirstRun() {
        return !workspaceModel.isInitialized(workspace);
    }

    /**
     * Create a new manager window and open the avatar.
     * 
     * @param avatar
     *            An avatar to open.
     */
    private void openWindow(final String title, final Avatar avatar) {
        window = new FirstRunWindow();
        window.addWindowListener(new WindowAdapter() {
            public void windowClosed(final WindowEvent e) {
                synchronized(window) { window.notifyAll(); }
            }
        });
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() { window.open(title, avatar); }
            });
        }
        catch(final InterruptedException ix) { throw new BrowserException("", ix); }
        catch(final InvocationTargetException itx) { throw new BrowserException("", itx); }
        synchronized(window) {
            try { window.wait(); }
            catch(final InterruptedException ix) { throw new BrowserException("", ix); }
        }
    }
}
