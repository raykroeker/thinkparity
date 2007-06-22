/*
 * Created On: 2007-01-17 13:12:00
 */
package com.thinkparity.ophelia.browser.platform.action.container;

import com.thinkparity.codebase.model.container.Container;

import com.thinkparity.ophelia.model.container.ContainerModel;
import com.thinkparity.ophelia.model.document.CannotLockException;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractBrowserAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * <b>Title:</b>thinkParity Archive Container Action<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class Archive extends AbstractBrowserAction {

    /** A thinkParity <code>Browser</code> application. */
    private final Browser browser;

    /**
     * A <code>Container</code>. Used by invoke and retry invoke to maintain
     * the container.
     */
    private Container container;

    /**
     * A <code>Boolean</code>. Used by invoke and retry invoke to maintain
     * the draft exists flag.
     */
    private Boolean draftExists;

    /**
     * Create Archive.
     * 
     */
    public Archive(final Browser browser) {
        super(ActionId.CONTAINER_ARCHIVE);
        this.browser = browser;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     *
     */
    @Override
    protected void invoke(final Data data) {
        final Long containerId = (Long) data.get(DataKey.CONTAINER_ID);
        final ContainerModel containerModel = getContainerModel();
        final Container container = containerModel.read(containerId);
        final Boolean draftExists = containerModel.doesExistLocalDraft(containerId);
        if (draftExists) {
            if (browser.confirm("ContainerArchive.ConfirmArchiveDraftExistsMessage",
                    new Object[] {container.getName()})) {
                invoke(container, draftExists);
            }
        } else {
            if (browser.confirm("ContainerArchive.ConfirmArchiveMessage",
                    new Object[] { container.getName() })) {
                invoke(container, draftExists);
            }
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#retryInvokeAction()
     *
     */
    @Override
    public void retryInvokeAction() {
        invoke(container, draftExists);
    }

    /**
     * Invoke the archive api.
     * 
     * @param container
     *            A <code>Container</code>.
     * @param draftExists
     *            A draft exists <code>Boolean</code>.
     */
    private void invoke(final Container container, final Boolean draftExists) {
        this.container = container;
        this.draftExists = draftExists;
        try {
            if (draftExists) {
                getContainerModel().deleteDraft(container.getId());
            }
            getContainerModel().archive(container.getId());
        } catch (final CannotLockException clx) {
            browser.retry(this, container.getName());
        }
    }

    /** The action data keys. */
    public enum DataKey { CONTAINER_ID }
}
