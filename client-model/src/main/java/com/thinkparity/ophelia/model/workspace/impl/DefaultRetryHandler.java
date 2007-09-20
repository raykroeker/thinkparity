/*
 * Created On:  16-Aug-07 1:59:53 PM
 */
package com.thinkparity.ophelia.model.workspace.impl;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.stream.StreamRetryHandler;

import com.thinkparity.ophelia.model.ModelFactory;
import com.thinkparity.ophelia.model.session.OfflineCode;
import com.thinkparity.ophelia.model.session.SessionModel;
import com.thinkparity.ophelia.model.util.service.ServiceRetryHandler;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * <b>Title:</b>thinkParity Ophelia Model Workspace Default Retry Handler<br>
 * <b>Description:</b>Retry the service if the session reports online.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class DefaultRetryHandler implements ServiceRetryHandler,
        StreamRetryHandler {

    /** A the retry wait period. */
    private static final Long PERIOD;

    static {
        // PERIOD - DefaultRetryHandler#<cinit> - Retry - 1s
        PERIOD = Long.valueOf(1L * 1000L);
    }

    /** A model factory. */
    private final ModelFactory modelFactory;

    /**
     * Create DefaultRetryHandler.
     * 
     * @param environment
     *            An <code>Environment</code>.
     * @param workspace
     *            A <code>Workspace</code>.
     */
    public DefaultRetryHandler(final Environment environment,
            final Workspace workspace) {
        super();
        this.modelFactory = ModelFactory.getInstance(environment, workspace);
    }

    /**
     * @see com.thinkparity.codebase.model.stream.StreamRetryHandler#retry()
     * @see com.thinkparity.ophelia.model.util.service.ServiceRetryHandler#retry()
     *
     */
    @Override
    public Boolean retry() {
        /* if we are online; or if the model says we are offline because of a
         * pending restart; retry the invocation */
        final OfflineCode offlineCode = getSessionModel().getOfflineCode();
        if (null == offlineCode) {  /* we are online */
            return Boolean.TRUE;
        } else {
            switch (offlineCode) {
            case CLIENT_MAINTENANCE:
            case CLIENT_OFFLINE:
                return Boolean.TRUE;
            case CLIENT_NETWORK_UNAVAILABLE:
                return Boolean.FALSE;
            default:
                throw Assert.createUnreachable("Unknown offline code.");
            }
        }
    }

    /**
     * @see com.thinkparity.codebase.model.stream.StreamRetryHandler#period()
     *
     */
    @Override
    public Long waitPeriod() {
        return PERIOD;
    }

    /**
     * Obtain a session model.
     * 
     * @return An instance of <code>SessionModel</code>.
     */
    private SessionModel getSessionModel() {
        return modelFactory.getSessionModel();
    }
}
