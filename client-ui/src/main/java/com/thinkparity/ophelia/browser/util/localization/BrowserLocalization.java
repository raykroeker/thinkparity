/**
 * Created On: 17-Apr-07 12:07:16 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.util.localization;

import java.io.InputStream;
import java.util.ResourceBundle;

import com.thinkparity.codebase.l10n.L18nContext;



/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class BrowserLocalization implements Localization {

    /**
     * Resource bundle.
     * 
     */
    private static final ResourceBundle RESOURCE_BUNDLE;

    static {
        RESOURCE_BUNDLE =
            ResourceBundleManager.getBundle(L18nResource.BROWSER);
    }

    /**
     * Resource bundler helper.
     * 
     */
    protected final ResourceBundleHelper bundleHelper;

    /**
     * Create a BrowserLocalization.
     * 
     * @param context
     *            The named context <code>String</code>.
     */
    public BrowserLocalization(final String l18nContext) {
        super();
        this.bundleHelper = new ResourceBundleHelper(RESOURCE_BUNDLE, l18nContext);
    }

    /**
     * Create BrowserLocalization.
     * 
     * @param l18nContext
     *            The localization context <code>L18nContext</code>.
     */
    public BrowserLocalization(final L18nContext l18nContext) {
        super();
        this.bundleHelper = new ResourceBundleHelper(RESOURCE_BUNDLE, l18nContext);
    }

    /**
     * Obtain the localised string for an enumerated type.
     * 
     * @param type
     *            An enumerated type.
     * @return The localised text.
     */
    public String getString(final Enum<?> type) {
        return getString(type.toString());
    }

    /**
     * Obtain the localised string for an enumerated type.
     * 
     * @param type
     *            An enumerated type.
     * @param arguments
     *            The arguments for the pattern.
     * @return The localised text.
     */
    public String getString(final Enum<?> type, final Object[] arguments) {
        return getString(type.toString(), arguments);
    }

    /**
     * Obtain a localised string.
     * 
     * @param localKey
     *            The key within the given context.
     * @return The localised text.
     */
    public String getString(final String localKey) {
        return bundleHelper.getString(localKey);
    }

    /**
     * Obtain a localised string.
     * 
     * @param localKey
     *            The key within the given context.
     * @param arguments
     *            The the arguments for the pattern.
     * @return The localised text.
     */
    public String getString(final String localKey, final Object[] arguments) {
        return bundleHelper.getString(localKey, arguments);
    }

    /**
     * Open a localized resource.
     * 
     * @param name
     *            The resource name <code>String</code>.
     * @return An <code>InputStream</code>.
     */
    public final InputStream openResource(final String name) {
        return ResourceBundleManager.openResource(name);
    }
}
