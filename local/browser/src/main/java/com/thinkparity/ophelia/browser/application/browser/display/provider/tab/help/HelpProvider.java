/**
 * Created On: 24-May-07 12:19:34 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.provider.tab.help;

import java.util.List;

import com.thinkparity.ophelia.model.help.HelpModel;
import com.thinkparity.ophelia.model.help.HelpTopic;
import com.thinkparity.ophelia.model.profile.ProfileModel;

import com.thinkparity.ophelia.browser.application.browser.display.provider.ContentProvider;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class HelpProvider extends ContentProvider {

    /** A thinkParity <code>HelpModel</code>. */
    private final HelpModel helpModel;

    /**
     * Create HelpProvider.
     * 
     * @param profileModel
     *            An instance of <code>ProfileModel</code>.
     * @param helpModel
     *            An instance of <code>HelpModel</code>.
     */
    public HelpProvider(final ProfileModel profileModel,
            final HelpModel helpModel) {
        super(profileModel);
        this.helpModel = helpModel;
    }

    /**
     * Read a help topic.
     * 
     * @param id
     *            A <code>Long</code> help id.
     * @return A <code>HelpTopic</code>.
     */
    public HelpTopic readHelpTopic(final Long id) {
        return helpModel.readTopic(id);
    }

    /**
     * Read help topics.
     * 
     * @param readContent
     *            A <code>Boolean</code> indicating to read content or not.
     * @return A <code>List</code> of <code>HelpTopic</code>s.
     */
    public List<HelpTopic> readHelpTopics(final Boolean readContent) {
        // NOCOMMIT
        return helpModel.readTopics();
    }

    /**
     * Search help topics.
     * 
     * @param expression
     *            A search expression <code>String</code>.
     * @return A <code>List&lt;Long&gt;</code>.
     */
    public List<Long> search(final String expression) {
        return helpModel.searchTopics(expression);
    }
}
