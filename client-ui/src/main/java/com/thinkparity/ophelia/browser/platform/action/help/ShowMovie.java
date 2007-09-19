/**
 * Created On: 25-May-07 4:48:32 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.help;

import java.io.IOException;
import java.net.URISyntaxException;

import com.thinkparity.ophelia.model.help.HelpTopicMovie;

import com.thinkparity.ophelia.browser.BrowserException;
import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractBrowserAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.util.swing.DesktopUtil;

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
            // HACK - ShowMovie - Not using action.
            DesktopUtil.browse(movie.getURL());
        } catch (final URISyntaxException urisx) {
            throw new BrowserException("Cannot show movie.", urisx);
        } catch (final IOException iox) {
            throw new BrowserException("Cannot show movie.", iox);
        }
    }

    public enum DataKey { MOVIE }
}
