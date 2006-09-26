/*
 * Created On: Sep 21, 2006 11:04:14 AM
 */
package com.thinkparity.ophelia.browser.platform.plugin;

import java.util.ResourceBundle;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.l10n.L18nContext;
import com.thinkparity.codebase.l10n.ResourceBundleHelper;

import com.thinkparity.ophelia.browser.platform.Platform.Connection;
import com.thinkparity.ophelia.browser.platform.plugin.extension.ActionExtension;
import com.thinkparity.ophelia.browser.platform.plugin.extension.TabExtension;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class PluginAbstractExtension implements PluginExtension {

    /** A localization resource bundle. */
    private ResourceBundle localization;

    /** A localization context. */
    private String localizationContext;

    /** A localization helper. */
    private ResourceBundleHelper localizationHelper;

    /** The plugin services. */
    private PluginServices services;

    /**
     * Create AbstractExtension.
     * 
     */
    protected PluginAbstractExtension() {
        super();
    }

    /**
     * Obtain the platform's connection state.
     * 
     * @return A <code>Connection</code>.
     */
    public final Connection getConnection() {
        Assert.assertNotNull(services,
                "Services have not been initialized for plugin extension {0}.", this);
        return services.getConnection();
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
     * Obtain an action extension for the plugin with the given name.
     * 
     * @param name
     *            An extension name.
     * @return A <code>ActionExtension</code>.
     */
    protected final ActionExtension getActionExtension(final String name) {
        Assert.assertNotNull(services,
                "Services have not been initialized for plugin extension {0}.", this);
        return services.getActionExtension(name);
    }

    /**
     * Obtain the localization context for the extension.
     * 
     * @return A localization context <code>String</code>.
     */
    protected String getLocalizedFormattedString(final String patternKey,
            final Object... arguments) {
        return localizationHelper.getString(patternKey, arguments);
    }

    /**
     * Obtain the localization context for the extension.
     * 
     * @return A localization context <code>String</code>.
     */
    protected String getLocalizedString(final String key) {
        return localizationHelper.getString(key);
    }

    /**
     * Obtain a tab extension for the plugin with the given name.
     * 
     * @param name
     *            An extension name.
     * @return A <code>TabExtension</code>.
     */
    protected final TabExtension getTabExtension(final String name) {
        Assert.assertNotNull(services,
                "Services have not been initialized for plugin extension {0}.", this);
        return services.getTabExtension(name);
    }

    /**
     * Initialize the extension's localization.
     * 
     * @param services
     *            The plugin's <code>PluginServices</code>.
     * @param baseName
     *            The resource bundle's base name <code>String</code>.
     */
    protected final void initializeLocalization(final PluginServices services,
            final String baseName, final String localizationContext) {
        Assert.assertIsNull(localization,
                "Localization for plugin extension {0} has already been initialized.", this);
        this.localization = services.getBundle(baseName);
        this.localizationContext = localizationContext;
        this.localizationHelper =
            new ResourceBundleHelper(localization, new L18nContext() {
                public String getLookupContext() {
                    return PluginAbstractExtension.this.localizationContext;
                }
            });
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.plugin.PluginExtension#initialize(com.thinkparity.ophelia.browser.platform.plugin.PluginServices)
     */
    protected final void initializeServices(final PluginServices services) {
        Assert.assertIsNull(this.services,
                "Services for plugin extension {0} have already been initialized.", this);
        this.services = services;
    }
}
