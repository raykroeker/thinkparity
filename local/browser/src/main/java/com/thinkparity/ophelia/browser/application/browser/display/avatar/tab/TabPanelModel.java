/*
 * Created On:  October 7, 2006, 10:36 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab;

import java.awt.Container;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;

import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.application.browser.BrowserSession;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel;
import com.thinkparity.ophelia.browser.platform.Platform.Connection;
import com.thinkparity.ophelia.browser.util.localization.JPanelLocalization;

/**
 * <b>Title:</b>thinkParity Tab Panel Model<br>
 * <b>Description:</b>A model for all tab panels.<br>
 *
 * @author raymond@thinkparity.com
 * @version 1.1.2.2
 */
public abstract class TabPanelModel extends TabModel {

    /** An apache logger wrapper. */
    protected final Log4JWrapper logger;

    /** The application. */
    protected final Browser browser;

    /** The model's expanded state map. */
    protected final Map<TabPanel, Boolean> expandedState;

    /** A list of filtered panels. */
    protected final List<TabPanel> filteredPanels;

    /** The swing list model. */
    protected final DefaultListModel listModel;

    /** A <code>JPanelLocalization</code>. */
    protected JPanelLocalization localization;

    /** A list of all panels. */
    protected final List<TabPanel> panels;

    /** A <code>BrowserSession</code>. */
    protected BrowserSession session;

    /** A list of all visible cells. */
    protected final List<TabPanel> visiblePanels;

    /**
     * Create TabPanelModel.
     *
     */
    public TabPanelModel() {
        super();
        this.logger = new Log4JWrapper(getClass());
        this.browser = getBrowser();
        this.expandedState = new HashMap<TabPanel, Boolean>();
        this.filteredPanels = new ArrayList<TabPanel>();
        this.listModel = new DefaultListModel();
        this.panels = new ArrayList<TabPanel>();
        this.visiblePanels = new ArrayList<TabPanel>();
    }
    
    /**
     * Determine whether or not thinkParity is running in development mode.
     * 
     * @return True if thinkParity is running in development mode.
     */
    public boolean isDevelopmentMode() {
        return browser.isDevelopmentMode();
    }

    /**
     * Determine whether or not the user is online.
     * 
     * @return True if the user is online.
     */
    public Boolean isOnline() {
        return browser.getConnection() == Connection.ONLINE;
    }
    
    /**
     * Set localization.
     *
     * @param localization
     *      A JPanelLocalization.
     */
    public void setLocalization(final JPanelLocalization localization) {
        this.localization = localization;
    }
    
    /**
     * Set the session.
     * 
     * @param session
     *            A <code>BrowserSession</code>.
     */
    public void setSession(final BrowserSession session) {
        this.session = session;
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabModel#toggleExpansion(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel)
     * 
     */
    public void toggleExpansion(final TabPanel tabPanel) {
        toggleExpansion(tabPanel, Boolean.TRUE);
    }
    
    /**
     * Toggle expansion of a panel.
     * 
     * @param tabPanel
     *            A <code>TabPanel</code>.
     * @param animate
     *            Animate flag <code>Boolean</code>.           
     */
    public void toggleExpansion(final TabPanel tabPanel, final Boolean animate) {
        doToggleExpansion(tabPanel, animate);
        synchronize();
    }
    
    /**
     * Obtain the swing list model.
     * 
     * @return The swing list model.
     */
    @Override
    protected DefaultListModel getListModel() {
        return listModel;
    }
    
    /**
     * Create a final list of contact cells and invitation cells. Apply the
     * search results to the list.
     * 
     */
    @Override
    protected void synchronize() {
        debug();
        applyFilters();
        applySort();
        /* add the filtered panels the visibility list */
        visiblePanels.clear();
        for (final TabPanel filteredPanel : filteredPanels) {
            visiblePanels.add(filteredPanel);
        }
        // add newly visible panels to the model; and set other panels
        int listModelIndex;
        for (int i = 0; i < visiblePanels.size(); i++) {
            if (listModel.contains(visiblePanels.get(i))) {
                listModelIndex = listModel.indexOf(visiblePanels.get(i));
                /* the position of the panel in the model is identical to that
                 * of the panel the list */
                if (i == listModelIndex) {
                    listModel.set(i, visiblePanels.get(i));
                } else {
                    listModel.remove(listModelIndex);
                    listModel.add(i, visiblePanels.get(i));
                }
            } else {
                listModel.add(i, visiblePanels.get(i));
            }
        }
        // prune newly invisible panels from the model
        final TabPanel[] invisiblePanels = new TabPanel[listModel.size()];
        listModel.copyInto(invisiblePanels);
        for (int i = 0; i < invisiblePanels.length; i++) {
            if (!visiblePanels.contains(invisiblePanels[i])) {
                listModel.removeElement(invisiblePanels[i]);
            }
        }
        debug();
    }
        
    /**
     * Clear all panels.
     *
     */
    protected void clearPanels() {
        panels.clear();
    }
    
    /**
     * Toggle the expansion of a single panel.
     * 
     * @param tabPanel
     *            A <code>TabPanel</code>.
     * @param animate
     *            Animate flag <code>Boolean</code>.             
     */
    protected void doToggleExpansion(final TabPanel tabPanel, final Boolean animate) {
        if (isExpanded(tabPanel)) {
            // if the panel is already expanded; just collapse it
            tabPanel.collapse(animate);
            expandedState.put(tabPanel, Boolean.FALSE);
        } else {
            // find the first expanded panel and collapse it
            boolean didHit = false;
            if (animate) {
                for (final TabPanel otherTabPanel : visiblePanels) {
                    if (isExpanded(otherTabPanel)) {
                        ((Container)otherTabPanel).addPropertyChangeListener("expanded",
                                new PropertyChangeListener() {
                                    public void propertyChange(
                                            final PropertyChangeEvent evt) {
                                        ((Container)otherTabPanel).removePropertyChangeListener("expanded", this);
    
                                        tabPanel.expand(animate);
                                        expandedState.put(tabPanel, Boolean.TRUE);
                                    }
                                });
                        otherTabPanel.collapse(animate);
                        expandedState.put(otherTabPanel, Boolean.FALSE);
                        didHit = true;
                        break;
                    }
                }
            }
            // Collapse any expanded panels even if they are not visible, without animation.
            // Panels may be non-visible due to search, and when the search is cleared
            // we don't want multiple expanded panels.
            for (final TabPanel panel : panels) {
                if (!panel.equals(tabPanel) && isExpanded(panel)) {
                    panel.setExpanded(Boolean.FALSE);
                    expandedState.put(panel, Boolean.FALSE);
                }
            }
            if (!didHit) {
                tabPanel.expand(animate);
                expandedState.put(tabPanel, Boolean.TRUE);
            }
        }
    }
    
    /**
     * Determine if a panel is expanded.
     * 
     * @param tabPanel
     *            A <code>TabPanel</code>.
     * @return True if the panel is expanded; false otherwise.
     */
    protected boolean isExpanded(final TabPanel tabPanel) {
        if (expandedState.containsKey(tabPanel)) {
            return expandedState.get(tabPanel).booleanValue();
        } else {
            // NOTE the default panel expanded state can be changed here
            expandedState.put(tabPanel, Boolean.FALSE);
            return isExpanded(tabPanel);
        }
    } 
}
