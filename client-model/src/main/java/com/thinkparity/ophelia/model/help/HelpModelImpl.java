/*
 * Created On:  23-May-07 4:21:56 PM
 */
package com.thinkparity.ophelia.model.help;

import java.util.List;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.model.Model;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * <b>Title:</b>thinkParity OpheliaModel Help Model Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class HelpModelImpl extends Model implements HelpModel,
        InternalHelpModel {

    /**
     * Create HelpModelImpl.
     *
     */
    public HelpModelImpl() {
        super();
    }

    /**
     * @see com.thinkparity.ophelia.model.help.HelpModel#readHelpTopic(java.lang.Long)
     *
     */
    public HelpTopic readHelpTopic(Long id) {
        // NOCOMMIT HelpModelImpl.readHelpTopic NYI raymond@thinkparity.com 24-May-07
        throw Assert.createNotYetImplemented("");
    }

    /**
     * @see com.thinkparity.ophelia.model.help.HelpModel#readHelpTopics()
     *
     */
    public List<HelpTopic> readHelpTopics() {
        // NOCOMMIT HelpModelImpl.readHelpTopics NYI raymond@thinkparity.com 24-May-07
        throw Assert.createNotYetImplemented("");
    }

    /**
     * @see com.thinkparity.ophelia.model.help.HelpModel#search(java.lang.String)
     *
     */
    public List<Long> search(String expression) {
        // NOCOMMIT HelpModelImpl.search NYI raymond@thinkparity.com 24-May-07
        throw Assert.createNotYetImplemented("");
    }

    /**
     * @see com.thinkparity.ophelia.model.Model#initializeModel(com.thinkparity.codebase.model.session.Environment, com.thinkparity.ophelia.model.workspace.Workspace)
     *
     */
    @Override
    protected void initializeModel(final Environment environment,
            final Workspace workspace) {
    }
}
