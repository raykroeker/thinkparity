/*
 * Created On: Sep 21, 2006 11:04:14 AM
 */
package com.thinkparity.ophelia.browser.platform.plugin.extension;

import java.util.ResourceBundle;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.l10n.L18nContext;
import com.thinkparity.codebase.l10n.ResourceBundleHelper;

import com.thinkparity.ophelia.browser.platform.plugin.PluginExtension;
import com.thinkparity.ophelia.browser.platform.plugin.PluginServices;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class AbstractExtension implements PluginExtension {

    /** A localization resource bundle. */
    private ResourceBundle localization;

    /** A localization context. */
    private String localizationContext;

    /** A localization helper. */
    private ResourceBundleHelper localizationHelper;

    /**
     * Create AbstractExtension.
     * 
     */
    protected AbstractExtension() {
        super();
    }

    /**
     * Obtain the extensions localization.
     * 
     * @return A <code>ResourceBundle</code>.
     */
    public final ResourceBundle getLocalization() {
        Assert.assertNotNull(localization,
                "Localization has not been initialized.");
        return localization;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.plugin.PluginExtension#getLocalizationContext()
     */
    public final String getLocalizationContext() {
        Assert.assertNotNull(localizationContext,
                "Localization has not been initialized.");
        return localizationContext;
    }

    /**
     * Obtain the localization context for the extension.
     * 
     * @return A localization context <code>String</code>.
     */
    protected final String getLocalizedFormattedString(final String patternKey,
            final Object... arguments) {
        return localizationHelper.getString(patternKey, arguments);
    }

    /**
     * Obtain the localization context for the extension.
     * 
     * @return A localization context <code>String</code>.
     */
    protected final String getLocalizedString(final String key) {
        return localizationHelper.getString(key);
    }

    /**
     * Initialize the extension's localization.
     * 
     * @param services
     *            The plugin's <code>PluginServices</code>.
     * @param baseName
     *            The resource bundle's base name <code>String</code>.
     */
    protected void initializeLocalization(final PluginServices services,
            final String baseName, final String localizationContext) {
        this.localization = services.getBundle(baseName);
        this.localizationContext = localizationContext;
        this.localizationHelper =
            new ResourceBundleHelper(localization, new L18nContext() {
                public String getLookupContext() {
                    return AbstractExtension.this.localizationContext;
                }
            });
    }
}
