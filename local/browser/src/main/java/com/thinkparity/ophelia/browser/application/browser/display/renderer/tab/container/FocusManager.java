/**
 * Created On: 9-Nov-06 2:34:43 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container;

import java.awt.Component;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import com.thinkparity.ophelia.browser.application.browser.component.MenuFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.container.ContainerModel;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class FocusManager {
    
    private static FocusList focusList = FocusList.NONE;
    
    public FocusManager() {
        super();
    }
    
    /**
     * Get the list that has focus.
     * 
     * @return The list with focus.
     */
    public FocusList getFocusList() {
        return focusList;
    }
    
    /**
     * Add a focus listener for a container panel.
     * 
     * @param containerPanel
     *          A <code>ContainerPanel</code>.
     * @param model
     *          A <code>ContainerModel</code>.
     */
    public void addFocusListener(final ContainerPanel containerPanel, final ContainerModel model) {
        containerPanel.addFocusListener(new FocusAdapter() {
            public void focusGained(final FocusEvent e) {
                ContainerVersionsPanel versionsPanel = (ContainerVersionsPanel)model.getVersionsPanel(containerPanel.getContainer());
                changeFocus(containerPanel, versionsPanel, FocusList.CONTAINER);
            }
            public void focusLost(final FocusEvent e) {
                if (!MenuFactory.isPopupMenu()) {
                    ContainerVersionsPanel versionsPanel = (ContainerVersionsPanel)model.getVersionsPanel(containerPanel.getContainer());
                    changeFocus(containerPanel, versionsPanel, FocusList.NONE);
                }
            }
        });
    }
    
    /**
     * Add a focus listener for one of the lists of a container versions panel.
     * 
     * @param versionsPanel
     *          A <code>ContainerVersionsPanel</code>.
     * @param model
     *          A <code>ContainerModel</code>.
     * @param component
     *          The <code>Component</code>, either the content list or the versions list.
     * @param focusList
     *          The list.
     */
    public void addFocusListener(final ContainerVersionsPanel versionsPanel, final ContainerModel model,
            final Component component, final FocusList focusList) {
        component.addFocusListener(new FocusAdapter() {
            public void focusGained(final FocusEvent e) {
                ContainerPanel containerPanel = (ContainerPanel)model.getContainerPanel(versionsPanel.getContainer());
                changeFocus(containerPanel, versionsPanel, focusList);
            }
            public void focusLost(final FocusEvent e) {
                if (!MenuFactory.isPopupMenu()) {
                    ContainerPanel containerPanel = (ContainerPanel)model.getContainerPanel(versionsPanel.getContainer());
                    changeFocus(containerPanel, versionsPanel, FocusList.NONE);
                }
            }
        });
    }
    
    /**
     * Notify panels that the focus has changed.
     */
    private void changeFocus(final ContainerPanel containerPanel, final ContainerVersionsPanel versionsPanel, final FocusList focusList) {
        if (FocusManager.focusList != focusList) {
            FocusManager.focusList = focusList;
            if (null!=containerPanel) {
                containerPanel.focusChanged(focusList);
            }
            if (null!=versionsPanel) {
                versionsPanel.focusChanged(focusList);
            }
        }
    }
        
    public enum FocusList { CONTAINER, VERSION, CONTENT, NONE }
}
