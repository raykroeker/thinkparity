/*
 * Created On:  25-Apr-07 3:57:03 PM
 */
package com.thinkparity.ophelia.browser.platform.firewall;

import com.thinkparity.ophelia.browser.Constants;
import com.thinkparity.ophelia.browser.platform.Platform;
import com.thinkparity.ophelia.browser.util.firewall.FirewallAccessException;
import com.thinkparity.ophelia.browser.util.firewall.FirewallUtil;
import com.thinkparity.ophelia.browser.util.firewall.FirewallUtilProvider;
import com.thinkparity.ophelia.browser.util.swing.OpheliaJFrame;

/**
 * <b>Title:</b>thinkParity OpheliaUI <br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class FirewallHelper implements Runnable {

    /** A flag indicating whether or not the rules have been added. */
    private boolean firewallRulesAdded;

    /** A generic platform specific <code>FirewallUtil</code>. */
    private final FirewallUtil firewallUtil;

    /** A <code>OpheliaJFrame</code>. */
    private OpheliaJFrame window;

    /**
     * Create FirewallHelper.
     *
     */
    public FirewallHelper(final Platform platform) {
        super();
        this.firewallUtil = FirewallUtilProvider.getInstance();
        this.firewallRulesAdded = false;
    }

    /**
     * Deterine whether or not the firewall rules were added.
     * 
     * @return True if the firewall rules were added.
     */
    public Boolean didAddFirewallRules() {
        return Boolean.valueOf(firewallRulesAdded);
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
        firewallRulesAdded = true;
        Runtime.getRuntime().addShutdownHook(new Thread("TPS-OpheliaUI-FirewallRules") {
            @Override
            public void run() {
                firewallUtil.removeExecutable(Constants.Files.EXECUTABLE);
            }
        });
    }

    /**
     * Obtain the firewall access error window.
     * 
     * @return An <code>OpheliaJFrameWindow</code>.
     */
    private OpheliaJFrame getWindow() {
        if (null == window) {
            window = new FirewallAccessErrorWindow();
            ((FirewallAccessErrorWindow) window).setFirewallHelper(this);
        }
        return window;
    }
}
