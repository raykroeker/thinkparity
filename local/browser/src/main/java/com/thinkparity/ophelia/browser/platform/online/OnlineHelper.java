/*
 * Created On: Jun 14, 2006 4:10:49 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.online;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.thinkparity.ophelia.browser.platform.Platform;

/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class OnlineHelper {

    /** The url to check if we are online. */
    private final String urlString;

    /** Create OnlineHelper. */
    public OnlineHelper(final Platform platform) {
        super();
        this.urlString = new StringBuffer("http://")
                .append(platform.getPreferences().getServerHost())
                .toString();
    }

    /**
     * Determine whether or not the platform has online capability.
     * 
     * @return True if the platform is online.
     */
    public Boolean isOnline() {
        try {
            final URL url = new URL(urlString);
            final HttpURLConnection con = (HttpURLConnection) url.openConnection();
            final int responseCode = con.getResponseCode();
            if(200 != responseCode) { return Boolean.FALSE; }
            else { return Boolean.TRUE; }
        }
        catch(final MalformedURLException murlx) { return Boolean.FALSE; }
        catch(final IOException iox) { return Boolean.FALSE; }
    }
}
