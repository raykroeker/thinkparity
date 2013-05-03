/**
 * Created On: 24-May-07 10:16:55 AM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.help;

import com.thinkparity.ophelia.model.help.HelpTopic;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public interface ActionDelegate {

    /**
     * Invoke the action for a help topic.
     * 
     * @param helpTopic
     *            A <code>HelpTopic</code>.
     */
    public void invokeForHelpTopic(final HelpTopic helpTopic);
}
