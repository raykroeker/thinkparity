/*
 * Created On: Jun 10, 2006 10:26:01 AM
 */
package com.thinkparity.browser.platform.firstrun;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.browser.BrowserException;
import com.thinkparity.browser.platform.Platform;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.contact.ContactModel;
import com.thinkparity.model.parity.model.profile.Profile;
import com.thinkparity.model.parity.model.profile.ProfileModel;
import com.thinkparity.model.parity.model.session.Credentials;
import com.thinkparity.model.parity.model.session.SessionModel;
import com.thinkparity.model.parity.model.workspace.WorkspaceModel;


/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class FirstRunHelper {

    /**
     * Obtain a logging api id.
     * 
     * @param api
     *            The api.
     * @return A logging api id.
     */
    private static final StringBuffer getApiId(final String api) {
        return getLogId().append(" ").append(api);
    }

    /**
     * Obtain a logging id.
     * 
     * @return A logging id.
     */
    private static final StringBuffer getLogId() {
        return new StringBuffer("[LBROWSER] [PLATFORM] [FIRST RUN HELPER]");
    }

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

    /** The thinkParity workspace model. */
    private final WorkspaceModel wModel;

    /** Create FirstRunHelper. */
    public FirstRunHelper(final Platform platform) {
        super();
        this.logger = platform.getLogger(getClass());
        this.profileModel = platform.getModelFactory().getProfileModel(getClass());
        this.contactModel = platform.getModelFactory().getContactModel(getClass());
        this.sessionModel = platform.getModelFactory().getSessionModel(getClass());
        this.wModel = platform.getModelFactory().getWorkspaceModel(getClass());
    }

    /**
     * Execute first run functionality for the browser platform.
     * 
     * @return True if first run completed.
     */
    public Boolean firstRun() {
        Assert.assertTrue(
                getApiId("[FIRST RUN] [PLATFROM HAS ALREADY BEEN RUN]"),
                isFirstRun());
        loginAvatar = new LoginAvatar(this);
        openWindow(loginAvatar.getTitle(), loginAvatar);

        final String username = loginAvatar.getUsername();
        final String password = loginAvatar.getPassword();
        if(null != username && null != password) {
            final Credentials credentials = new Credentials();
            credentials.setPassword(password);
            credentials.setUsername(username);
            try { sessionModel.login(credentials); }
            catch(final ParityException px) { throw new BrowserException("", px); }
            Assert.assertTrue("", sessionModel.isLoggedIn());

            final Profile profile = profileModel.read();
            userProfileAvatar = new UserProfileAvatar(this);
            userProfileAvatar.setInput(profile);
            openWindow(userProfileAvatar.getTitle(), userProfileAvatar);
            
            final String name = userProfileAvatar.getFullName();
            final String email = userProfileAvatar.getEmail();
            if(null != name && null != email) {
                profile.addEmail(email);
                profileModel.update(profile);
                contactModel.download();
                return Boolean.TRUE;
            }
            else { return Boolean.FALSE; }
        }
        else { return Boolean.FALSE; }
    }

    /**
     * Determine if this is the first time the platform has been run.
     * 
     * @return True if this is the first time the platform has been run.
     */
    public Boolean isFirstRun() { return wModel.isFirstRun(); }

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
