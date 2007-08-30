/**
 * Created On: 25-May-07 4:48:32 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.help;

import com.thinkparity.ophelia.model.help.HelpTopicMovie;

import com.thinkparity.ophelia.browser.BrowserException;
import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractBrowserAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.util.jdic.DesktopUtil;

import org.jdesktop.jdic.desktop.DesktopException;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class ShowMovie extends AbstractBrowserAction  {

    /**
     * Create a ShowMovie.
     * 
     * @param browser
     *            The browser application.
     */
    public ShowMovie(final Browser browser) {
        super(ActionId.HELP_SHOW_MOVIE);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     */
    @Override
    public void invoke(final Data data) {
        final HelpTopicMovie movie = (HelpTopicMovie) data.get(DataKey.MOVIE);
        try {
            DesktopUtil.browse(movie.getURL());
        } catch (final DesktopException dx) {
            throw new BrowserException("Cannot show movie.", dx);
        }
    }

    public enum DataKey { MOVIE }
}
