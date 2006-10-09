/*
 * Created On: Sep 21, 2006 2:31:43 PM
 */
package com.thinkparity.ophelia.browser.plugin.archive.tab;

import java.util.UUID;

import com.thinkparity.ophelia.browser.platform.plugin.PluginModelFactory;
import com.thinkparity.ophelia.browser.platform.plugin.PluginServices;
import com.thinkparity.ophelia.browser.platform.plugin.extension.TabPanelExtension;

import com.thinkparity.ophelia.model.archive.ArchiveModel;

/**
 * <b>Title:</b>The Archive Tab Extension<br>
 * <b>Description:<b>The archive tab functionality is implemented in this
 * extension.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ArchiveTab extends
        TabPanelExtension<ArchiveTabAvatar, ArchiveTabModel, ArchiveTabProvider> {

    /** The extension's localization base name. */
    private static final String LOCALIZATION_BASE_NAME;

    /** The extension's localization context. */
    private static final String LOCALIZATION_CONTEXT;

    static {
        LOCALIZATION_BASE_NAME = "localization/Archive_Messages";
        LOCALIZATION_CONTEXT = "ArchiveTab";
    }

    /** A thinkParity <code>ArchiveModel</code> interface. */
    private ArchiveModel archiveModel;

    /** The <code>ArchiveTabAvatar</code>. */
    private ArchiveTabAvatar avatar;

    /**
     * Create ArchiveTab.
     * 
     * @param metaInfo
     *            The extension's meta info.
     */
    public ArchiveTab() {
        super();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.plugin.extension.TabExtension#createAvatar()
     */
    @Override
    public ArchiveTabAvatar createAvatar() {
        this.avatar = new ArchiveTabAvatar(this);
        return avatar;
    }

    /**
     * Notify the plugin a container has been archived.
     * 
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     */
    public void fireContainerArchived(final UUID uniqueId) {
        if (null != avatar) {
            avatar.synchronizeContainer(uniqueId);
        }
    }

    /**
     * Notify the plugin a container has been restored.
     * 
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     */
    public void fireContainerRestored(final UUID uniqueId) {
        if (null != avatar) {
            avatar.synchronizeContainer(uniqueId);
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.plugin.extension.TabExtension#getProvider()
     */
    @Override
    public ArchiveTabProvider getProvider() {
        return new ArchiveTabProvider(archiveModel);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.plugin.extension.TabExtension#getText()
     */
    @Override
    public String getText() {
        return getLocalizedString("Text");
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.plugin.PluginExtension#initialize(com.thinkparity.ophelia.browser.platform.plugin.PluginServices)
     */
    public void initialize(final PluginServices services) {
        initializeLocalization(services, LOCALIZATION_BASE_NAME, LOCALIZATION_CONTEXT);
        initializeServices(services);
        final PluginModelFactory modelFactory = services.getModelFactory();
        this.archiveModel = modelFactory.getArchiveModel();
    }
}
