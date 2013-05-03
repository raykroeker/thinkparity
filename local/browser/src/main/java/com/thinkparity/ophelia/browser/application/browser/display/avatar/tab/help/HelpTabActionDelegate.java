/**
 * Created On: 24-May-07 10:26:42 AM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.help;

import java.util.List;

import com.thinkparity.ophelia.model.help.HelpTopic;
import com.thinkparity.ophelia.model.help.HelpTopicMovie;

import com.thinkparity.ophelia.browser.application.browser.DefaultBrowserActionDelegate;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabButtonActionDelegate;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.help.ActionDelegate;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.ActionInvocation;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.help.ShowMovie;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class HelpTabActionDelegate extends DefaultBrowserActionDelegate
        implements ActionDelegate, TabButtonActionDelegate {

    /** The browse <code>AbstractAction</code>. */
    private final ActionInvocation showMovie;

    /**
     * Create HelpTabActionDelegate.
     * 
     * @param model
     *            The <code>HelpTabModel</code>.
     */
    HelpTabActionDelegate(final HelpTabModel model) {
        super();
        this.showMovie = getInstance(ActionId.HELP_SHOW_MOVIE);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.help.ActionDelegate#invokeForHelpTopic(com.thinkparity.ophelia.model.help.HelpTopic)
     */
    public void invokeForHelpTopic(final HelpTopic helpTopic) {
        final List<HelpTopicMovie> movies = helpTopic.getMovies();
        if (0 < movies.size()) {
            // NOTE Only one movie per help topic is supported in the UI for now.
            final HelpTopicMovie movie = movies.get(0);
            final Data showMovieData = new Data(1);
            showMovieData.set(ShowMovie.DataKey.MOVIE, movie);
            showMovie.invokeAction(getApplication(), showMovieData);
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabButtonActionDelegate#invokeForTabButton()
     */
    public void invokeForTabButton() {}

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabButtonActionDelegate#isTabButtonActionAvailable()
     */
    public Boolean isTabButtonActionAvailable() {
        return Boolean.FALSE;
    }
}
