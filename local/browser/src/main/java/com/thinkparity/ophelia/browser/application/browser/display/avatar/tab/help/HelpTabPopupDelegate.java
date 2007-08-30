/**
 * Created On: 24-May-07 10:15:27 AM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.help;

import java.util.ArrayList;
import java.util.List;

import com.thinkparity.ophelia.model.help.HelpTopic;
import com.thinkparity.ophelia.model.help.HelpTopicMovie;

import com.thinkparity.ophelia.browser.application.browser.DefaultBrowserPopupDelegate;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.help.HelpTabPanel;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.help.PopupDelegate;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.help.Expand;
import com.thinkparity.ophelia.browser.platform.action.help.ShowMovie;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class HelpTabPopupDelegate extends DefaultBrowserPopupDelegate implements
        PopupDelegate {

    /** A list of action ids, used for the help popup. */
    private final List<ActionId> actionIds;

    /** A list of data, used for the help popup. */
    private final List<Data> dataList;

    /**
     * Create HelpTabPopupDelegate.
     * 
     * @param model
     *            The <code>HelpTabModel</code>.
     */
    HelpTabPopupDelegate(final HelpTabModel model) {
        super();
        this.actionIds = new ArrayList<ActionId>();
        this.dataList = new ArrayList<Data>();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.help.PopupDelegate#showForHelpTopic(com.thinkparity.ophelia.model.help.HelpTopic)
     */
    public void showForHelpTopic(final HelpTopic helpTopic) {
        final List<HelpTopicMovie> movies = helpTopic.getMovies();
        if (0 < movies.size()) {
            for (final HelpTopicMovie movie : movies) {
                final Data showMovieData = new Data(1);
                showMovieData.set(ShowMovie.DataKey.MOVIE, movie);
                addWithExpand(ActionId.HELP_SHOW_MOVIE, showMovieData, helpTopic.getId());
            }
            show();
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanelPopupDelegate#showForPanel(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel)
     */
    public void showForPanel(final TabPanel tabPanel) {
        showForHelpTopic(((HelpTabPanel) tabPanel).getHelpTopic());
    }

    /**
     * Add an action to a popup menu.
     * A second action to expand the help topic is inserted.
     * 
     * @param actionId
     *            An <code>ActionId</code>.
     * @param data
     *            The <code>Data</code>.       
     * @param helpTopicId
     *            A help topic id <code>Long</code>.          
     */
    private void addWithExpand(final ActionId actionId, final Data data,
            final Long helpTopicId) {
        actionIds.clear();
        dataList.clear();

        final Data expandData = new Data(1);
        expandData.set(Expand.DataKey.HELP_TOPIC_ID, helpTopicId);
        actionIds.add(ActionId.HELP_EXPAND);
        dataList.add(expandData);

        actionIds.add(actionId);
        dataList.add(data);
        add(actionIds, dataList, 1);
    }
}
