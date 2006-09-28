/*
 * Aug 6, 2005
 */
package com.thinkparity.ophelia.model;

import com.thinkparity.codebase.StackUtil;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.ophelia.model.workspace.WorkspaceModel;

/**
 * AbstractModel
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class AbstractModel<T extends AbstractModelImpl> extends
        com.thinkparity.codebase.model.AbstractModel<T> {

    /** A stack filter for the logger. */
    private static final StackUtil.Filter STACK_FILTER;

    static {
        STACK_FILTER = new StackUtil.Filter() {
            public Boolean accept(final StackTraceElement stackElement) {
                return !"getImpl".equals(stackElement.getMethodName()) &&
                    !"getImplLock".equals(stackElement.getMethodName());
            }
        };
    }

    /** An apache logger. */
    private final Log4JWrapper logger;

    /**
	 * Obtain the workspace model.
	 * 
	 * @return The parity workspace model.
	 */
	protected static WorkspaceModel getWorkspaceModel() {
		final WorkspaceModel workspaceModel = WorkspaceModel.getModel();
		return workspaceModel;
	}

    /**
     * Create a AbstractModel.
     */
    protected AbstractModel(final T impl) {
        super(impl);
        this.logger = new Log4JWrapper();
    }

    /**
     * @see com.thinkparity.codebase.model.AbstractModel#getImpl()
     */
    @Override
    protected final T getImpl() {
        logger.logApiId(STACK_FILTER);
        return super.getImpl();
    }

    /**
     * @see com.thinkparity.codebase.model.AbstractModel#getImplLock()
     */
    @Override
    protected Object getImplLock() {
        return super.getImpl().workspace;
    }
}
