/*
 * Created On:  Nov 19, 2007 10:08:15 AM
 */
package com.thinkparity.ophelia.support.ui.avatar;

import com.thinkparity.ophelia.support.ui.action.ActionFactory;
import com.thinkparity.ophelia.support.ui.action.ActionRunner;

/**
 * <b>Title:</b>thinkParity Ophelia Support UI Avatar Context<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class AvatarContext {

    /** The action factory. */
    private final ActionFactory actionFactory;

    /**
     * Create AvatarContext.
     *
     */
    AvatarContext() {
        super();
        this.actionFactory = ActionFactory.getInstance();
    }

    /**
     * Instantiate an action runner.
     * 
     * @param id
     *            A <code>String</code>.
     * @return An <code>ActionRunner</code>.
     */
    public ActionRunner newActionRunner(final String id) {
        return actionFactory.newRunner(id);
    }
}
