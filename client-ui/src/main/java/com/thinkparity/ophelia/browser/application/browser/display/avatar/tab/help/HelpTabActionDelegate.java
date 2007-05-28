/**
 * Created On: 24-May-07 10:26:42 AM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.help;

import com.thinkparity.ophelia.model.help.HelpTopic;

import com.thinkparity.ophelia.browser.application.browser.DefaultBrowserActionDelegate;
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
        implements ActionDelegate {

    /** The browse <code>AbstractAction</code>. */
    private final ActionInvocation browse;

    /**
     * Create HelpTabActionDelegate.
     * 
     * @param model
     *            The <code>HelpTabModel</code>.
     */
    HelpTabActionDelegate(final HelpTabModel model) {
        super();
        this.browse = getInstance(ActionId.HELP_SHOW_MOVIE);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.help.ActionDelegate#invokeForHelpTopic(com.thinkparity.ophelia.model.help.HelpTopic)
     */
    public void invokeForHelpTopic(final HelpTopic helpTopic) {
        final Data showMovieData = new Data(1);
        showMovieData.set(ShowMovie.DataKey.URL, helpTopic.getMovie());
        browse.invokeAction(getApplication(), showMovieData);
    }
}
