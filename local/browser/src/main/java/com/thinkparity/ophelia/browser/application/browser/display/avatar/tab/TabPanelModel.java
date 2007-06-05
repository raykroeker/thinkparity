/*
 * Created On:  October 7, 2006, 10:36 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab;

import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.application.browser.BrowserSession;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel;
import com.thinkparity.ophelia.browser.platform.util.persistence.Persistence;
import com.thinkparity.ophelia.browser.platform.util.persistence.PersistenceFactory;
import com.thinkparity.ophelia.browser.util.localization.Localization;

/**
 * <b>Title:</b>thinkParity Tab Panel Model<br>
 * <b>Description:</b>A model for all tab panels.<br>
 *
 * @author raymond@thinkparity.com
 * @version 1.1.2.2
 */
public abstract class TabPanelModel<T extends Object> extends TabModel {

    /** The application. */
    protected final Browser browser;

    /** The model's expanded state map. */
    protected final Map<TabPanel, Boolean> expandedState;

    /** A list of filtered panels. */
    protected final List<TabPanel> filteredPanels;

    /** The swing list model. */
    protected final DefaultListModel listModel;

    /** A <code>Localization</code>. */
    protected Localization localization;

    /** An apache logger wrapper. */
    protected final Log4JWrapper logger;

    /** A list of all panels. */
    protected final List<TabPanel> panels;
    
    /** A parity persistence. */
    protected final Persistence persistence;
    
    /** A user search expression <code>String</code>. */
    protected String searchExpression;
    
    /** The selected panel id. */
    private T selectedPanelId;
    
    /** A search result list of unique id <code>T</code>s. */
    protected final List<T> searchResults;

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
        this.searchResults = new ArrayList<T>();
        this.visiblePanels = new ArrayList<TabPanel>();
    }

    /**
     * Collapse the panel.
     * 
     * @param panelId
     *            A panel id <code>T</code>.
     * @param animate
     *            Animate flag <code>Boolean</code>.           
     */
    public void collapsePanel(final T panelId, final Boolean animate) {
        checkThread();
        final TabPanel tabPanel = (TabPanel)lookupPanel(panelId);
        if (isExpanded(tabPanel)) {
            toggleExpansion(tabPanel, animate);
        }
    }

    /**
     * Delete the selected panel (run appropriate action).
     */
    public void deleteSelectedPanel() {
        checkThread();
        final TabPanel selectedPanel = getSelectedPanel();
        if (null != selectedPanel) {
            deletePanel(selectedPanel);
        }
    }

    /**
     * Expand the panel.
     * 
     * @param panelId
     *            A panel id <code>T</code>.
     * @param animate
     *            Animate flag <code>Boolean</code>.           
     */
    public void expandPanel(final T panelId, final Boolean animate) {
        checkThread();
        final TabPanel tabPanel = (TabPanel)lookupPanel(panelId);
        if (!isExpanded(tabPanel)) {
            toggleExpansion(tabPanel, animate);
        }
    }
    
    /**
     * Sort and filter the ids according to the existing visible order. Visible
     * elements are returned in the order they are currently displayed top to
     * bottom. Elements that are not visible are not included in the returned
     * list. This method assumes visiblePanels are already filtered and sorted
     * correctly.
     * 
     * @param ids
     *            A list of panel id <code>T</code>.
     * @return A sorted list of panel id <code>T</code>s.
     */
    public List<T> getCurrentVisibleOrder(final List<T> panelIds) {
        checkThread();
        final List<T> sortedIds = new ArrayList<T>();
        for (final TabPanel visiblePanel : visiblePanels) {
            final T panelId = lookupId(visiblePanel);
            if (panelIds.contains(panelId)) {
                sortedIds.add(panelId);
            }            
        }
        return sortedIds;
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
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabDelegate#isNextPanelExpanded(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel)
     */
    public Boolean isNextPanelExpanded(final TabPanel tabPanel) {
        final int visibleIndex = visiblePanels.indexOf(tabPanel);
        if (visibleIndex<0 || visibleIndex>=visiblePanels.size()-1) {
            return Boolean.FALSE;
        } else {
            return isExpanded(visiblePanels.get(visibleIndex+1));
        }
    }

    /**
     * Determine whether or not the user is online.
     * 
     * @return True if the user is online.
     */
    public Boolean isOnline() {
        return browser.isOnline();
    }

    /**
     * Scroll the panel so it is visible.
     * 
     * @param panelId
     *            A panel id <code>T</code>.
     */
    public void scrollPanelToVisible(final T panelId) {
        checkThread();
        final JComponent panel = (JComponent) lookupPanel(panelId);
        final Rectangle rectangle = panel.getBounds();
        rectangle.x = rectangle.y = 0;
        panel.scrollRectToVisible(rectangle);
    }

    /**
     * Select the first panel.
     */
    public void selectFirstPanel() {
        checkThread();
        if (visiblePanels.size() > 0) {
            selectPanel(visiblePanels.get(0), Boolean.TRUE);
        }
    }

    /**
     * Select the last panel.
     */
    public void selectLastPanel() {
        checkThread();
        if (visiblePanels.size() > 0) {
            selectPanel(visiblePanels.get(visiblePanels.size()-1), Boolean.TRUE);
        }
    }

    /**
     * Select the next panel, or the first panel if none are selected.
     */
    public void selectNextPanel() {
        checkThread();
        if (visiblePanels.size() > 0) {
            final TabPanel oldSelectedPanel = getSelectedPanel();
            if (null == oldSelectedPanel) {
                selectPanel(visiblePanels.get(0), Boolean.TRUE);
            } else {
                final int visibleIndex = visiblePanels.indexOf(oldSelectedPanel);
                if (visibleIndex < 0) {
                    selectPanel(visiblePanels.get(0), Boolean.TRUE);
                } else if (visibleIndex < visiblePanels.size()-1) {
                    selectPanel(visiblePanels.get(visibleIndex+1), Boolean.TRUE);
                }
            }
        }
    }

    /**
     * Select the previous panel, or the first panel if none are selected.
     */
    public void selectPreviousPanel() {
        checkThread();
        if (visiblePanels.size() > 0) {
            final TabPanel oldSelectedPanel = getSelectedPanel();
            if (null == oldSelectedPanel) {
                selectPanel(visiblePanels.get(0), Boolean.TRUE);
            } else {
                final int visibleIndex = visiblePanels.indexOf(oldSelectedPanel);
                if (visibleIndex < 0) {
                    selectPanel(visiblePanels.get(0), Boolean.TRUE);
                } else if (visibleIndex > 0) {
                    selectPanel(visiblePanels.get(visibleIndex-1), Boolean.TRUE);
                }
            }
        }
    }

    /**
     * Select the panel.
     * 
     * @param panelId
     *            A panel id <code>T</code>.    
     */
    public void selectPanel(final T panelId) {
        checkThread();
        final TabPanel panel = lookupPanel(panelId);
        // Panel can be null in some cases, for example if a notification refers
        // to an invitation which has already been accepted.
        if (null != panel) {
            selectPanel(panel);
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabDelegate#selectPanel(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel)
     */
    public void selectPanel(final TabPanel tabPanel) {
        selectPanel(tabPanel, Boolean.FALSE);
    }

    /**
     * Select the panel.
     * 
     * @param tabPanel
     *            A <code>TabPanel</code>. 
     * @param scrollPanelToVisible
     *            A <code>Boolean</code>, scroll the panel so it is visible or not. 
     */
    public void selectPanel(final TabPanel tabPanel, final Boolean scrollPanelToVisible) {
        checkThread();
        requestFocusInWindow(tabPanel);
        if (isSelected(tabPanel)) {
            return;
        }
        final TabPanel oldSelectedPanel = getSelectedPanel();
        if (null != oldSelectedPanel) {
            oldSelectedPanel.setSelected(Boolean.FALSE);
        }
        selectedPanelId = lookupId(tabPanel);
        tabPanel.setSelected(Boolean.TRUE);
        if (scrollPanelToVisible) {
            scrollPanelToVisible(selectedPanelId);
        }
    }

    /**
     * Set localization.
     *
     * @param localization
     *      A Localization.
     */
    public void setLocalization(final Localization localization) {
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
        checkThread();
        if (!tabPanel.isSetExpandedData()) {
            readExpandedPanelData(tabPanel);
        }
        doToggleExpansion(tabPanel, animate);
        synchronize();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabModel#applySearchFilter()
     */
    @Override
    protected void applySearchFilter() {
        checkThread();
        filteredPanels.clear();
        if (isSearchApplied()) {
            TabPanel searchResultPanel;
            for (final T searchResult : searchResults) {
                searchResultPanel = lookupPanel(searchResult);
                /* NOTE if the search result panel is null something has
                 * happened to bump the panel out of this tab */ 
                if (null != searchResultPanel)
                    if (!filteredPanels.contains(searchResultPanel))
                        filteredPanels.add(searchResultPanel);
            }
        } else {
            // no filter is applied
            filteredPanels.addAll(panels);
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabModel#applySearch(java.lang.String)
     *
     */
    @Override
    protected void applySearch(final String searchExpression) {
        checkThread();
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
        checkThread();
        panels.clear();
    }

    /**
     * Delete the panel (run appropriate action).
     * 
     * @param tabPanel
     *            A <code>TabPanel</code>.
     */
    protected abstract void deletePanel(final TabPanel tabPanel);

    /**
     * Toggle the expansion of a single panel.
     * 
     * @param tabPanel
     *            A <code>TabPanel</code>.
     * @param animate
     *            Animate flag <code>Boolean</code>.             
     */
    protected void doToggleExpansion(final TabPanel tabPanel, final Boolean animate) {
        checkThread();
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
                                        synchronize();
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
        checkThread();
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
     * Determine if a panel is selected.
     * 
     * @param tabPanel
     *            A <code>TabPanel</code>.
     * @return True if the panel is selected; false otherwise.
     */
    protected boolean isSelected(final TabPanel tabPanel) {
        return (null != selectedPanelId && lookupId(tabPanel).equals(selectedPanelId));
    }

    /**
     * Lookup the panel id for a panel.
     * 
     * @param tabPanel
     *            A <code>TabPanel</code>.
     * @return A panel id <code>T</code>.
     */
    protected abstract T lookupId(final TabPanel tabPanel);

    /**
     * Lookup the panel for the corresponding id.
     * 
     * @param uniqueId
     *          The id.
     * @return A <code>TabPanel</code>.
     */
    protected abstract TabPanel lookupPanel(final T panelId);

    /**
     * Read additional data that a panel needs when expanded.
     * 
     * @param tabPanel
     *            A <code>TabPanel</code>.
     */
    protected abstract void readExpandedPanelData(final TabPanel tabPanel);

    /**
     * Search for a list of ids through the content provider.
     * 
     * @return A list of ids.
     */
    protected abstract List<T> readSearchResults();

    /**
     * Request focus on the selected panel, if there is one.
     */
    protected void requestFocusInSelectedPanel() {
        final TabPanel selectedPanel = getSelectedPanel();
        if (null != selectedPanel) {
            requestFocusInWindow(selectedPanel);
        }
    }

    /**
     * Request focus.
     * 
     * @param tabPanel
     *            A <code>TabPanel</code>.
     */
    protected void requestFocusInWindow(final TabPanel tabPanel) {
        ((JComponent)tabPanel).requestFocusInWindow();
    }

    /**
     * Remove the search.
     * 
     * @see #searchExpression
     * @see #searchResults
     * @see #applySearch(String)
     */
    @Override
    protected void removeSearch() {
        checkThread();
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
    protected void synchronizeImpl() {
        checkThread();
        debug();
        applySearchFilter();
        applyFilter();
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
        // if there is no selection, or if the selection is not visible,
        // select the first panel.
        if (!isSelectedPanel() ||
                (isSelectedPanel() && !visiblePanels.contains(getSelectedPanel()))) {
            selectFirstPanel();
        }
        debug();
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
     * Check we are on the AWT event dispatching thread.
     */
    private void checkThread() {
        Assert.assertTrue(EventQueue.isDispatchThread(), "Tab panel model not on the AWT event dispatch thread.");
    }

    /**
     * Get the selected panel.
     * Returns null if no panel is selected.
     * Also returns null if a panel is selected and later deleted.
     * 
     * @return The selected <code>TabPanel</code>, or null if there is none.
     */
    private TabPanel getSelectedPanel() {
        if (null == selectedPanelId) {
            return null;
        } else {
            return lookupPanel(selectedPanelId);
        }
    }

    /**
     * Determine if there is a selected panel.
     * 
     * @return true if there is a selected panel.
     */
    private Boolean isSelectedPanel() {
        return (null != getSelectedPanel());
    }
}
