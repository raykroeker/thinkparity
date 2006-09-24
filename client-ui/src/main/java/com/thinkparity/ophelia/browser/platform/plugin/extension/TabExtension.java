/*
 * Created On: Sep 21, 2006 11:03:19 AM
 */
package com.thinkparity.ophelia.browser.platform.plugin.extension;


/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class TabExtension<TEA extends TabExtensionAvatar<TEM>,
        TEM extends TabExtensionModel, ECP extends TabExtensionModelContentProvider>
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
    public abstract TEA createAvatar();

    /**
     * Create the tab's model.
     * 
     * @return A tab model.
     */
    public abstract TEM createModel();

    /**
     * Obtain the tab avatar's content provider.
     * 
     * @return A <code>ContentProvider</code>.
     */
    public abstract ECP getProvider();

    public abstract String getText();
}
