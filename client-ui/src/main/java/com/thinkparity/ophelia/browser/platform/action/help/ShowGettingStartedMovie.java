/**
 * Created On: 1-Oct-07 3:52:05 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.help;

import java.io.IOException;
import java.net.URISyntaxException;

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
public class ShowGettingStartedMovie extends AbstractBrowserAction {

    /** The url for the getting started movie. */
    private static String GETTING_STARTED_MOVIE_URL;

    static {
        // NOCOMMIT should be "start"
        GETTING_STARTED_MOVIE_URL = "http://movie.thinkParity.com/en/intro";
    }

    /**
     * Create a ShowGettingStartedMovie.
     * 
     * @param browser
     *            The browser application.
     */
    public ShowGettingStartedMovie(final Browser browser) {
        super(ActionId.HELP_SHOW_GETTING_STARTED_MOVIE);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     */
    @Override
    public void invoke(final Data data) {
        try {
            // HACK - ShowMovie - Not using action.
            DesktopUtil.browse(GETTING_STARTED_MOVIE_URL);
        } catch (final URISyntaxException urisx) {
            throw new BrowserException("Cannot show movie.", urisx);
        } catch (final IOException iox) {
            throw new BrowserException("Cannot show movie.", iox);
        }
    }
}
