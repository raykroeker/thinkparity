/**
 * Created On: 15-Oct-07 11:18:38 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.platform;

import java.io.IOException;
import java.net.URISyntaxException;

import com.thinkparity.ophelia.browser.BrowserException;
import com.thinkparity.ophelia.browser.platform.Platform;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.util.swing.DesktopUtil;

/**
 * @author robert@thinkparity.com
 * @version $Revision$
 */
public class ShowGettingStartedMovie extends AbstractAction {

    /** The url for the getting started movie. */
    private static String GETTING_STARTED_MOVIE_URL;

    static {
        GETTING_STARTED_MOVIE_URL = "http://movie.thinkParity.com/en/start";
    }

    /**
     * Create a ShowGettingStartedMovie.
     * 
     * @param platform
     *            The <code>Platform</code>.
     */
    public ShowGettingStartedMovie(final Platform platform) {
        super(ActionId.PLATFORM_SHOW_GETTING_STARTED_MOVIE);
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
