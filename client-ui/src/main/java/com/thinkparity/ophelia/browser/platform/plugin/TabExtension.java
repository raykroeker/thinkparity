/*
 * Created On: Sep 21, 2006 11:03:19 AM
 */
package com.thinkparity.ophelia.browser.platform.plugin;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class TabExtension<ATA extends AbstractTabAvatar<ATM>, ATM extends AbstractTabModel, ACP extends AbstractContentProvider>
        extends AbstractExtension {

    /**
     * Create TabExtension.
     * 
     */
    protected TabExtension() {
        super();
    }

    /**
     * Create the tab's avatar.
     * 
     * @return A tab avatar.
     */
    public abstract ATA createAvatar();

    /**
     * Create the tab's model.
     * 
     * @return A tab model.
     */
    public abstract ATM createModel();

    /**
     * Obtain the tab avatar's content provider.
     * 
     * @return A <code>ContentProvider</code>.
     */
    public abstract ACP getProvider();

    public abstract String getText();
}
