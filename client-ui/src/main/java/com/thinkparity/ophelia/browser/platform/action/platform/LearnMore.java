/**
 * Created On: Apr 3, 2007 1:03:10 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.platform;

import com.thinkparity.codebase.Application;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.codebase.model.util.http.Link;
import com.thinkparity.codebase.model.util.http.LinkFactory;

import com.thinkparity.ophelia.browser.BrowserException;
import com.thinkparity.ophelia.browser.platform.BrowserPlatform;
import com.thinkparity.ophelia.browser.platform.Platform;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.util.jdic.DesktopUtil;

import org.jdesktop.jdic.desktop.DesktopException;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class LearnMore extends AbstractAction {

    /** The relative link for "signup" learn more. */
    private static String SIGNUP_LINK;

    /** The relative link for "beta" learn more. */
    private static String BETA_LINK;

    static {
        SIGNUP_LINK = "pricing";
        BETA_LINK = "freeTrial";
    }

    /** Create LearnMore. */
    public LearnMore(final Platform platform) {
        super(ActionId.PLATFORM_LEARN_MORE);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     */
    @Override
    public void invoke(final Data data) {
        final Topic topic = (Topic) data.get(DataKey.TOPIC);
        // TODO Finalize where links go.
        // TODO Beta topic will go away when beta is done.
        switch (topic) {
        case SIGNUP:
            browse(SIGNUP_LINK);
            break;
        case BETA:
            browse(BETA_LINK);
            break;
        default:
            Assert.assertUnreachable("Unknown learn more topic.");
        }
    }

    /**
     * Browser to the specified topic link.
     * 
     * @param topicLink
     *            A topic link <code>String</code>.
     * @throws BrowserException
     */
    private void browse(final String topicLink) {
        final Link learnMoreLink = LinkFactory.getInstance(Application.OPHELIA,
                BrowserPlatform.getInstance().getEnvironment()).create(topicLink);
        try {
            DesktopUtil.browse(learnMoreLink.toString());
        } catch (final DesktopException dx) {
            throw new BrowserException("Cannot open Learn More web page", dx);
        }
    }

    public enum DataKey { TOPIC }
    public enum Topic { SIGNUP, BETA }
}
