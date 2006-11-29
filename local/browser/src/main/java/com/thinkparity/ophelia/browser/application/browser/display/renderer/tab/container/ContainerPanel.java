/*
 * Created On: October 6, 2006, 5:03 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;

import com.thinkparity.ophelia.model.container.ContainerDraft;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.container.ContainerModel;
import com.thinkparity.ophelia.browser.application.browser.dnd.ImportTxHandler;
import com.thinkparity.ophelia.browser.platform.application.ApplicationId;
import com.thinkparity.ophelia.browser.platform.application.ApplicationRegistry;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ContainerPanel extends AbstractContainerPanel {

    /** The browser application. */
    private final Browser browser;

    /** The focus manager. */
    private final FocusManager focusManager;
    
    /** The container panel's model. */
    private final ContainerModel model;

    /**
     * Create ContainerPanel.
     * 
     * @param model
     *            A <code>ContainerModel</code>.
     */
    public ContainerPanel(final ContainerModel model) {
        super();
        this.model = model;
        this.browser = ((Browser) new ApplicationRegistry().get(ApplicationId.BROWSER));
        this.focusManager = new FocusManager();
        focusManager.addFocusListener(this, model);
        initBookmarks();
    }
    
    /**
     * Called if the focus list changes.
     * 
     * @param focusList
     *      The list with focus.
     */
    public void focusChanged(final FocusManager.FocusList focusList) {
        repaint();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.AbstractContainerPanel#getContainer()
     *
     */
    @Override
    public Container getContainer() {
        return super.getContainer();
    }

    /**
     * Obtain the container id.
     * 
     * @return A container id <code>Long</code>.
     */
    public Long getContainerId() {
        return getContainer().getId();
    }

    /**
     * Get the draft associated with this container panel.
     */
    public ContainerDraft getDraft() {
        return super.getDraft();
    }

    /**
     * Set the panel data.
     * 
     * @param container
     *            A <code>Container</code>.
     * @param draft
     *            A <code>ContainerDraft</code>.
     * @param latestVersion
     *            The latest <code>ContainerVersion</code>.
     */
    public void setPanelData(final Container container,
            final ContainerDraft draft, final ContainerVersion latestVersion) {
        super.setPanelData(container, draft, latestVersion);
        setTransferHandler(new ImportTxHandler(browser, model, container));
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel#formMouseReleased(java.awt.event.MouseEvent)
     * 
     */
    @Override
    protected void formMouseReleased(MouseEvent e) {
        // Perform selection first since this affects drawing. Drawing
        // must be completed before the popup opens because of the
        // shadow border on the popup.
        if (e.isPopupTrigger()) {
            model.selectContainer(getContainer());
            if (!isFocusOwner()) {
                requestFocusInWindow();
            } 
        }   
        super.formMouseReleased(e);
    }

    /**
     * Show the popup menu for the container.
     * 
     * @param e
     *            A <code>MouseEvent</code>.
     */
    @Override
    protected void triggerPopup(final Component invoker, final MouseEvent e) {
        new ContainerPopup(model, getContainer()).show(invoker, e);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel#triggerSingleClick(java.awt.event.MouseEvent)
     */
    @Override
    protected void triggerSingleClick(final MouseEvent e) {
        model.triggerExpand(this);
        if (!isFocusOwner()) {
            requestFocusInWindow();
        }
    }

    /**
     * Initialize the bookmark label's mouse listeners.
     * 
     */
    private void initBookmarks() {
        addTextIconMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                if (getContainer().isBookmarked()) {
                    browser.runRemoveContainerBookmark(getContainer().getId());
                } else {
                    browser.runAddContainerBookmark(getContainer().getId());
                }
            }
            @Override
            public void mouseEntered(final MouseEvent e) {
                ((Component) e.getSource()).setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(final MouseEvent e) {
                ((Component) e.getSource()).setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }           
        });
    }
}
