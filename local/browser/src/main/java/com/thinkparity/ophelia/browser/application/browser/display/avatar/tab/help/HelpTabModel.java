/**
 * Created On: 24-May-07 10:13:56 AM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.help;

import java.awt.EventQueue;
import java.util.Collections;
import java.util.List;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.ophelia.model.help.HelpContent;
import com.thinkparity.ophelia.model.help.HelpTopic;

import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabAvatarFilterBy;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabAvatarFilterDelegate;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabPanelModel;
import com.thinkparity.ophelia.browser.application.browser.display.provider.tab.help.HelpProvider;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabButtonActionDelegate;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.help.HelpTabPanel;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public final class HelpTabModel extends TabPanelModel<Long> implements
        TabAvatarFilterDelegate {

    /** The <code>HelpTabActionDelegate</code>. */
    private final HelpTabActionDelegate actionDelegate;

    /** The <code>HelpTabPopupDelegate</code>. */
    private final HelpTabPopupDelegate popupDelegate;

    /**
     * Create HelpTabModel.
     * 
     */
    HelpTabModel() {
        super();
        this.actionDelegate = new HelpTabActionDelegate(this);
        this.popupDelegate= new HelpTabPopupDelegate(this);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabAvatarFilterDelegate#clearFilter()
     */
    public void clearFilter() {
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabAvatarFilterDelegate#getFilterBy()
     */
    public List<TabAvatarFilterBy> getFilterBy() {
        final List<TabAvatarFilterBy> filterBy = Collections.emptyList();
        return filterBy;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabPanelModel#getTabButtonActionDelegate()
     */
    @Override
    public TabButtonActionDelegate getTabButtonActionDelegate() {
        return actionDelegate;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabAvatarFilterDelegate#isFilterApplied()
     */
    public Boolean isFilterApplied() {
        return Boolean.FALSE;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabAvatarFilterDelegate#isFilterSelected(com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabAvatarFilterBy)
     */
    public Boolean isFilterSelected(final TabAvatarFilterBy filterBy) {
        return Boolean.FALSE;
    }

    /**
     * Apply the sort to the filtered list of panels.
     * 
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabModel#applySort()
     */
    @Override
    protected void applySort() {
        // No sort is done.
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabModel#debug()
     */
    @Override
    protected void debug() {
        checkThread();
        logger.logDebug("{0} help topic panels.", panels.size());
        logger.logDebug("{0} filtered panels.", filteredPanels.size());
        logger.logDebug("{0} visible panels.", visiblePanels.size());
        logger.logDebug("{0} model elements.", listModel.size());
        final TabPanel[] listModelPanels = new TabPanel[listModel.size()];
        listModel.copyInto(listModelPanels);
        for (final TabPanel listModelPanel : listModelPanels) {
            logger.logVariable("listModelPanel.getId()", listModelPanel.getId());
        }
        logger.logDebug("Search expression:  {0}", searchExpression);
        logger.logDebug("{0} search result hits.", searchResults.size());
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabPanelModel#deletePanel(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel)
     */
    @Override
    protected void deletePanel(final TabPanel tabPanel) {
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabModel#initialize()
     */
    @Override
    protected void initialize() {
        super.initialize();
        debug();
        clearPanels();
        final List<HelpTopic> helpTopics = readHelpTopics();
        for (final HelpTopic helpTopic : helpTopics) {
            addPanel(helpTopic);
        }
        synchronize();
        debug();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabPanelModel#lookupId(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel)
     */
    @Override
    protected Long lookupId(final TabPanel tabPanel) {
        return ((HelpTabPanel)tabPanel).getHelpTopic().getId();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabPanelModel#lookupPanel(java.lang.Object)
     */
    @Override
    protected TabPanel lookupPanel(final Long panelId) {
        final int panelIndex = lookupIndex(panelId);
        return -1 == panelIndex ? null : panels.get(panelIndex);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabPanelModel#readSearchResults()
     */
    @Override
    protected List<Long> readSearchResults() {
        checkThread();
        return ((HelpProvider) contentProvider).search(searchExpression);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabPanelModel#setExpandedPanelData(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel)
     */
    @Override
    protected void setExpandedPanelData(final TabPanel tabPanel) {
        final HelpContent helpContent = readHelpContent(lookupId(tabPanel));
        ((HelpTabPanel)tabPanel).setPanelData(helpContent);
    }

    /**
     * Add a help topic to the end of the panels list.
     * 
     * @param helpTopic
     *            A <code>HelpTopic</code>.
     */
    private void addPanel(final HelpTopic helpTopic) {
        panels.add(panels.size(), toDisplay(helpTopic));
    }

    /**
     * Check we are on the AWT event dispatching thread.
     */
    private void checkThread() {
        Assert.assertTrue(EventQueue.isDispatchThread(), "Help tab model not on the AWT event dispatch thread.");
    }

    /**
     * Lookup the index of the help topic's corresponding panel.
     * 
     * @param helpTopicId
     *            A help topic id <code>Long</code>.
     * @return An <code>int</code> index; or -1 if the help topic does not
     *         exist in the panel list.
     */
    private int lookupIndex(final Long helpTopicId) {
        for (int i = 0; i < panels.size(); i++)
            if (((HelpTabPanel) panels.get(i)).getHelpTopic()
                    .getId().equals(helpTopicId))
                return i;
        return -1;
    }

    /**
     * Read help content.
     * 
     * @param helpTopicId
     *            A help topic id <code>Long</code>.
     * @return A <code>HelpContent</code>.
     */
    private HelpContent readHelpContent(final Long helpTopicId) {
        return ((HelpProvider) contentProvider).readHelpContent(helpTopicId);
    }

    /**
     * Read the help topics from the provider.
     * 
     * @return A list of <code>HelpTopic</code>s.
     */
    private List<HelpTopic> readHelpTopics() {
        return ((HelpProvider) contentProvider).readHelpTopics();
    }

    /**
     * Obtain the help topic display cell for a help topic.
     * 
     * @param helpTopic
     *            A <code>HelpTopic</code>.
     * @return A <code>TabPanel</code>.
     */
    private TabPanel toDisplay(final HelpTopic helpTopic) {
        final HelpTabPanel panel = new HelpTabPanel(session);
        panel.setActionDelegate(actionDelegate);
        panel.setPanelData(helpTopic);
        panel.setPopupDelegate(popupDelegate);
        panel.setExpanded(isExpanded(panel));
        panel.setSelected(isSelected(panel));
        panel.setTabDelegate(this);
        return panel;
    }
}
