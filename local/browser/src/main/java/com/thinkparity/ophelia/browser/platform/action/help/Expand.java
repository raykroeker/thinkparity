/**
 * Created On: 25-May-07 4:25:44 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.help;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractBrowserAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class Expand extends AbstractBrowserAction {

    /** The browser application. */
    private final Browser browser;

    /**
     * Create a Expand.
     * 
     * @param browser
     *            The browser application.
     */
    public Expand(final Browser browser) {
        super(ActionId.HELP_EXPAND);
        this.browser = browser;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     */
    @Override
    public void invoke(final Data data) {
        final Long helpTopicId = (Long) data.get(DataKey.HELP_TOPIC_ID);
        browser.expandHelpTopic(helpTopicId);  
    }

    /** The data keys. */
    public enum DataKey { HELP_TOPIC_ID }
}
