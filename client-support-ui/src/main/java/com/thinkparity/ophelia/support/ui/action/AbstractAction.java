/*
 * Created On:  Nov 19, 2007 9:14:07 AM
 */
package com.thinkparity.ophelia.support.ui.action;

import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.ophelia.support.Support;

/**
 * <b>Title:</b>thinkParity Ophelia Support UI Abstract Action<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class AbstractAction implements Action {

    /** An action logger. */
    protected final Log4JWrapper logger;

    /** An action context. */
    private final ActionContext context;

    /** An action id. */
    private final String id;

    /**
     * Create AbstractAction.
     * 
     * @param id
     *            A <code>String</code>.
     */
    protected AbstractAction(final String id) {
        super();
        this.id = id;

        this.context = new ActionContext();
        this.context.setEnvironment(Support.getInstance().getEnvrionment());

        this.logger = new Log4JWrapper(getClass());
    }

    /**
     * @see com.thinkparity.ophelia.support.ui.action.Action#getId()
     *
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Obtain the action context.
     * 
     * @return An <code>ActionContext</code>.
     */
    protected ActionContext getContext() {
        return context;
    }
}
