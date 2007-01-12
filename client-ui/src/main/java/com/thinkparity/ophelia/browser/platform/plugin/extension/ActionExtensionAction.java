/*
 * Created On: Sep 25, 2006 9:10:41 AM
 */
package com.thinkparity.ophelia.browser.platform.plugin.extension;

import com.thinkparity.ophelia.model.archive.ArchiveModel;
import com.thinkparity.ophelia.model.artifact.ArtifactModel;
import com.thinkparity.ophelia.model.contact.ContactModel;
import com.thinkparity.ophelia.model.container.ContainerModel;
import com.thinkparity.ophelia.model.document.DocumentModel;
import com.thinkparity.ophelia.model.profile.ProfileModel;
import com.thinkparity.ophelia.model.session.SessionModel;
import com.thinkparity.ophelia.model.user.UserModel;
import com.thinkparity.ophelia.model.workspace.Workspace;

import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.plugin.PluginModelFactory;
import com.thinkparity.ophelia.browser.platform.plugin.PluginServices;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class ActionExtensionAction extends AbstractAction {

    /** The action's extension. */
    private final ActionExtension extension;

    /** The extension action's model factory. */
    private final PluginModelFactory modelFactory;

    /**
     * Create ActionExtensionAction.
     * 
     */
    protected ActionExtensionAction(final PluginServices services,
            final ActionExtension extension) {
        super(extension);
        this.extension = extension;
        this.modelFactory = services.getModelFactory();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     */
    @Override
    public final void invoke(final Data data) {
        invoke(data.get(DataKey.SELECTION));
    }

    /**
     * Confirm with the user.
     * 
     * @param patternKey
     *            The confirmation message pattern key.
     * @param patternArguments
     *            The confirmation message pattern arguments.
     * @return The user's confirmation.
     */
    protected final Boolean confirm(final String patternKey,
            final Object... patternArguments) {
        return getBrowserApplication().confirmLocalized(
                getLocalizedFormattedString(patternKey, patternArguments));
    }

    /**
     * Obtain A thinkParity <code>ArchiveModel</code> interface.
     * 
     * @return A thinkParity <code>ArchiveModel</code> interface.
     */
    protected final ArchiveModel getArchiveModel() {
        return modelFactory.getArchiveModel();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#getArtifactModel()
     */
    @Override
    protected final ArtifactModel getArtifactModel() {
        // HACK This seems wierd.  Maybe obtaining a model from the action abstraction isn't correct.
        return null;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#getContactModel()
     */
    @Override
    protected final ContactModel getContactModel() {
        // HACK This seems wierd.  Maybe obtaining a model from the action abstraction isn't correct.
        return null;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#getContainerModel()
     */
    @Override
    protected final ContainerModel getContainerModel() {
        return modelFactory.getContainerModel();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#getDocumentModel()
     */
    @Override
    protected final DocumentModel getDocumentModel() {
        // HACK This seems wierd.  Maybe obtaining a model from the action abstraction isn't correct.
        return null;
    }

    protected final String getLocalizedFormattedString(final String patternKey,
            final Object... arguments) {
        return extension.getLocalizedFormattedString(patternKey, arguments);
    }

    protected final String getLocalizedString(final String key) {
        return extension.getLocalizedString(key);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#getProfileModel()
     */
    @Override
    protected final ProfileModel getProfileModel() {
        // HACK This seems wierd.  Maybe obtaining a model from the action abstraction isn't correct.
        return null;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#getSessionModel()
     */
    @Override
    protected final SessionModel getSessionModel() {
        // HACK This seems wierd.  Maybe obtaining a model from the action abstraction isn't correct.
        return null;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#getUserModel()
     */
    @Override
    protected final UserModel getUserModel() {
        // HACK This seems wierd.  Maybe obtaining a model from the action abstraction isn't correct.
        return null;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#getWorkspace()
     */
    @Override
    protected Workspace getWorkspace() {
        // HACK This seems wierd.  Maybe obtaining a model from the action abstraction isn't correct.
        return null;
    }

    /**
     * Invoke the action on the ui selection.
     * 
     * @param selection
     *            The currently selected ui element.
     */
    protected abstract void invoke(final Object selection);

    /** The selection's data key. */
    public enum DataKey { SELECTION }
}
