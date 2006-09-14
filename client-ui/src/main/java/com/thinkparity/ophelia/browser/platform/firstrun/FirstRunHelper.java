/*
 * Created On: Jun 10, 2006 10:26:01 AM
 */
package com.thinkparity.ophelia.browser.platform.firstrun;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.session.Credentials;

import com.thinkparity.ophelia.browser.BrowserException;
import com.thinkparity.ophelia.browser.platform.Platform;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.model.contact.ContactModel;
import com.thinkparity.ophelia.model.profile.ProfileModel;
import com.thinkparity.ophelia.model.session.SessionModel;
import com.thinkparity.ophelia.model.workspace.Workspace;
import com.thinkparity.ophelia.model.workspace.WorkspaceModel;


/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class FirstRunHelper {

    /** An apache logger. */
    final Logger logger;

    /** The thinkParity contact interface. */
    private final ContactModel contactModel;

    /** The login avatar. */
    private LoginAvatar loginAvatar;

    /** The thinkParity profile interface. */
    private final ProfileModel profileModel;

    /** The thinKParity session interface. */
    private final SessionModel sessionModel;

    /** The user profile avatar. */
    private UserProfileAvatar userProfileAvatar;

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
        this.profileModel = platform.getModelFactory().getProfileModel(getClass());
        this.contactModel = platform.getModelFactory().getContactModel(getClass());
        this.sessionModel = platform.getModelFactory().getSessionModel(getClass());
        this.workspace = platform.getModelFactory().getWorkspace(getClass());
        this.workspaceModel = platform.getModelFactory().getWorkspaceModel(getClass());
    }

    /**
     * Execute first run functionality for the browser platform.
     * 
     * @return True if first run completed.
     */
    public Boolean firstRun() {
        loginAvatar = new LoginAvatar(this);
        openWindow(loginAvatar.getTitle(), loginAvatar);

        final String username = loginAvatar.getUsername();
        final String password = loginAvatar.getPassword();
        if(null != username && null != password) {
            final Credentials credentials = new Credentials();
            credentials.setPassword(password);
            credentials.setUsername(username);
            sessionModel.login(credentials);
            Assert.assertTrue("", sessionModel.isLoggedIn());

            final Profile profile = profileModel.read();
            userProfileAvatar = new UserProfileAvatar(this);
            userProfileAvatar.setInput(profile);
            openWindow(userProfileAvatar.getTitle(), userProfileAvatar);
            
            profile.setName(userProfileAvatar.getFullName());
            profile.setOrganization(userProfileAvatar.getOrganization());
            profile.setTitle(userProfileAvatar.getTitle());
            profileModel.update(profile);
            contactModel.download();
            return Boolean.TRUE;
        }
        else { return Boolean.FALSE; }
    }

    /**
     * Determine if this is the first time the platform has been run.
     * 
     * @return True if this is the first time the platform has been run.
     */
    public Boolean isFirstRun() { return workspaceModel.isFirstRun(workspace); }

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
