/*
 * Created On:  October 7, 2006, 10:36 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab;

import java.awt.Container;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;

import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.application.browser.BrowserSession;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel;
import com.thinkparity.ophelia.browser.platform.Platform.Connection;
import com.thinkparity.ophelia.browser.platform.util.persistence.Persistence;
import com.thinkparity.ophelia.browser.platform.util.persistence.PersistenceFactory;
import com.thinkparity.ophelia.browser.util.localization.JPanelLocalization;

/**
 * <b>Title:</b>thinkParity Tab Panel Model<br>
 * <b>Description:</b>A model for all tab panels.<br>
 *
 * @author raymond@thinkparity.com
 * @version 1.1.2.2
 */
public abstract class TabPanelModel extends TabModel {
    
    /** A sort by persistence key */
    // TODO use appropriate variable naming for constants
    // TODO move the persistence key variables to the same scope as the persitence instance
    protected static final String sortByKey;
    
    /** A sort ascending persistence key */
    // TODO use appropriate variable naming for constants
    // TODO move the persistence key variables to the same scope as the persitence instance
    protected static final String sortAscendingKey;
    
    static {
        sortByKey = "sort.by";
        sortAscendingKey = "sort.ascending";
    }

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
    
    /** A parity persistence. */
    protected final Persistence persistence;
    
    /** A user search expression <code>String</code>. */
    protected String searchExpression;
    
    /** A search result list of unique id <code>Object</code>. */
    protected final List<Object> searchResults;

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
        this.persistence = PersistenceFactory.getPersistence(getClass()); 
        this.searchResults = new ArrayList<Object>();
        this.visiblePanels = new ArrayList<TabPanel>();
    }
       
    /**
     * Expand the panel.
     * 
     * @param uniqueId
     *            A unique id <code>Object</code>.
     * @param animate
     *            Animate flag <code>Boolean</code>.           
     */
    public void expandPanel(final Object uniqueId, final Boolean animate) {
        final TabPanel tabPanel = (TabPanel)lookupPanel(uniqueId);
        if (!isExpanded(tabPanel)) {
            toggleExpansion(tabPanel, animate);
        }
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
     * Scroll the panel so it is visible.
     * 
     * @param uniqueId
     *            A unique id <code>Object</code>.
     */
    public void scrollPanelToVisible(final Object uniqueId) {
        final JComponent panel = (JComponent)lookupPanel(uniqueId);
        final Rectangle rectangle = panel.getBounds();
        rectangle.x = rectangle.y = 0;
        panel.scrollRectToVisible(rectangle);
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
     * Sort and filter the ids according to the existing visible order.
     * Visible elements are returned in the order they
     * are currently displayed top to bottom.
     * Elements that are not visible are not included in the
     * returned list.
     * This method assumes visiblePanels are already filtered
     * and sorted correctly.
     * 
     * @param ids
     *          A list of unique id <code>Object</code>.
     * @return A sorted list of unique id <code>Object</code>.
     */
    public List<Object> getCurrentVisibleOrder(final List<? extends Object> ids) {
        final List<Object> sortedIds = new ArrayList<Object>();
        for (final TabPanel visiblePanel : visiblePanels) {
            final Object uniqueId = lookupId(visiblePanel);
            if (ids.contains(uniqueId)) {
                sortedIds.add(uniqueId);
            }            
        }
        return sortedIds;
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
     * Apply a series of filters on the panels.
     * 
     */
    protected void applyFilters() {
        filteredPanels.clear();
        if (isSearchApplied()) {
            TabPanel searchResultPanel;
            for (final Object searchResult : searchResults) {
                searchResultPanel = lookupPanel(searchResult);
                if (!filteredPanels.contains(searchResultPanel))
                    filteredPanels.add(searchResultPanel);
            }
        } else {
            // no filter is applied
            filteredPanels.addAll(panels);
        }
    }
    
    /**
     * Apply the search results.
     *
     */
    private void applySearch() {
        this.searchResults.clear();
        this.searchResults.addAll(readSearchResults());
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabModel#applySearch(java.lang.String)
     *
     */
    @Override
    protected void applySearch(final String searchExpression) {
        debug();
        if (searchExpression.equals(this.searchExpression)) {
            return;
        } else {
            this.searchExpression = searchExpression;
            applySearch();
            synchronize();
        }
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
     * Obtain the swing list model.
     * 
     * @return The swing list model.
     */
    @Override
    protected DefaultListModel getListModel() {
        return listModel;
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
        
    /**
     * Determine if search is applied.
     * 
     * @return True if a search expression is set.
     */
    protected boolean isSearchApplied() {
        return null != searchExpression;
    }
        
    /**
     * Lookup id of the object displayed in the tabPanel.
     * 
     * @param tabPanel
     *            A <code>TabPanel</code>.
     * @return A unique id <code>Object</code>.
     */
    protected abstract Object lookupId(final TabPanel tabPanel);
    
    /**
     * Lookup the panel for the corresponding id.
     * 
     * @param uniqueId
     *          The id.
     * @return A <code>TabPanel</code>.
     */
    protected abstract TabPanel lookupPanel(final Object uniqueId);
    
    /**
     * Search for a list of ids through the content provider.
     * 
     * @return A list of ids.
     */
    protected abstract List<? extends Object> readSearchResults();
    
    /**
     * Remove the search.
     * 
     * @see #searchExpression
     * @see #searchResults
     * @see #applySearch(String)
     */
    @Override
    protected void removeSearch() {
        debug();
        if (null == searchExpression) {
            return;
        } else {
            searchExpression = null;
            searchResults.clear();
            synchronize();
        }
    }
    
    /**
     * Create a final list of panels. Apply the
     * search results to the list.
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
}
