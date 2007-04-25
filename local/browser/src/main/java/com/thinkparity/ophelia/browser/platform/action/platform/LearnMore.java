/**
 * Created On: Apr 3, 2007 1:03:10 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.platform;

import com.thinkparity.codebase.Application;
import com.thinkparity.codebase.StringUtil.Separator;

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
        // TODO This needs to go somewhere sensible.
        final StringBuffer topicLink = new StringBuffer("about").append(
                Separator.ForwardSlash.toString()).append(topic.toString());
        final Link learnMoreLink = LinkFactory.getInstance(Application.OPHELIA,
                BrowserPlatform.getInstance().getEnvironment()).create(
                topicLink.toString());    
        try {
            DesktopUtil.browse(learnMoreLink.toString());
        } catch (final DesktopException dx) {
            throw new BrowserException("Cannot open Learn More web page", dx);
        }
    }

    public enum DataKey { TOPIC }
    public enum Topic { SIGNUP, SYNCHRONIZE, BETA }
}
