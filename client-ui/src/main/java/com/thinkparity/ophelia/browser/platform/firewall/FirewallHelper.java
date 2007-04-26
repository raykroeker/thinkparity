/*
 * Created On:  25-Apr-07 3:57:03 PM
 */
package com.thinkparity.ophelia.browser.platform.firewall;

import com.thinkparity.ophelia.browser.Constants;
import com.thinkparity.ophelia.browser.platform.Platform;
import com.thinkparity.ophelia.browser.platform.event.LifeCycleAdapter;
import com.thinkparity.ophelia.browser.platform.event.LifeCycleEvent;
import com.thinkparity.ophelia.browser.util.firewall.FirewallAccessException;
import com.thinkparity.ophelia.browser.util.firewall.FirewallUtil;
import com.thinkparity.ophelia.browser.util.firewall.FirewallUtilProvider;

/**
 * <b>Title:</b>thinkParity OpheliaUI <br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class FirewallHelper implements Runnable {

    /** A generic platform specific <code>FirewallUtil</code>. */
    private final FirewallUtil firewallUtil;

    /** The <code>Platform</code>. */
    private final Platform platform;

    /** A <code>FirewallAccessErrorWindow</code>. */
    private FirewallAccessErrorWindow window;

    /**
     * Create FirewallHelper.
     *
     */
    public FirewallHelper(final Platform platform) {
        super();
        this.platform = platform;
        this.firewallUtil = FirewallUtilProvider.getInstance();
    }

    /**
     * Initialize the firewall helper. Check to see if a firewall is enabled,
     * and if so add the appropriate firewall rules. If the rules cannot be
     * added, maintain an appropriate dialogue.
     * 
     * @see java.lang.Runnable#run()
     */
    public void run() {
        if (firewallUtil.isEnabled()) {
            /* the firewall is enabled - try to add the appropriate rules */
            try {
                addFirewallRules();
            } catch (final FirewallAccessException fax) {
                getWindow().setVisibleAndWait();
            }
        }
    }

    /**
     * Add the rules to the firewall.
     * 
     * @throws FirewallAccessException
     *             if the firewal rules cannot be added
     */
    void addFirewallRules() throws FirewallAccessException {
        firewallUtil.addExecutable(Constants.Files.EXECUTABLE);
        platform.addListener(new LifeCycleAdapter() {
            @Override
            public void ending(final LifeCycleEvent e) {
                firewallUtil.removeExecutable(Constants.Files.EXECUTABLE);
            }
        });
    }

    private FirewallAccessErrorWindow getWindow() {
        if (null == window) {
            window = new FirewallAccessErrorWindow();
            window.setFirewallHelper(this);
        }
        return window;
    }
}
