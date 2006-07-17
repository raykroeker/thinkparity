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
import com.thinkparity.model.parity.model.profile.ProfileModel;
import com.thinkparity.model.parity.model.session.Credentials;
import com.thinkparity.model.parity.model.session.SessionModel;


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

    /** The login avatar. */
    private LoginAvatar loginAvatar;

    /** The thinkParity profile interface. */
    private final ProfileModel pModel;

    /** The thinkParity session interface. */
    private final SessionModel sModel;

    /** The user profile avatar. */
    private UserProfileAvatar userProfileAvatar;

    /** The first run window. */
    private FirstRunWindow window;

    /** Create FirstRunHelper. */
    public FirstRunHelper(final Platform platform) {
        super();
        this.logger = platform.getLogger(getClass());
        this.pModel = platform.getModelFactory().getProfileModel(getClass());
        this.sModel = platform.getModelFactory().getSessionModel(getClass());
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
            try { sModel.login(credentials); }
            catch(final ParityException px) { throw new BrowserException("", px); }
            Assert.assertTrue("", sModel.isLoggedIn());

            return Boolean.TRUE;
        }
        else { return Boolean.FALSE; }
    }

    /**
     * Determine if this is the first time the platform has been run.
     * 
     * @return True if this is the first time the platform has been run.
     */
    public Boolean isFirstRun() { return null == pModel.read(); }

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
