/*
 * Created On:  25-Jun-07 12:33:56 PM
 */
package com.thinkparity.ophelia.model.stream;

import com.thinkparity.codebase.event.EventListener;

import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.stream.StreamInfo;
import com.thinkparity.codebase.model.stream.StreamSession;

import com.thinkparity.ophelia.model.Model;
import com.thinkparity.ophelia.model.workspace.Workspace;

import com.thinkparity.service.AuthToken;
import com.thinkparity.service.ServiceFactory;
import com.thinkparity.service.StreamService;

/**
 * <b>Title:</b>thinkParity Ophelia Model Stream Model Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class StreamModelImpl extends Model<EventListener> implements
        StreamModel, InternalStreamModel {

    /** A stream web-service. */
    private StreamService streamService;

    /**
     * Create StreamModelImpl.
     *
     */
    public StreamModelImpl() {
        super();
    }

    /**
     * @see com.thinkparity.ophelia.model.stream.InternalStreamModel#newDownstreamSession(com.thinkparity.codebase.model.document.DocumentVersion)
     *
     */
    public StreamSession newDownstreamSession(final DocumentVersion version) {
        try {
            return streamService.newDownstreamSession(getAuthToken(), version);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.stream.InternalStreamModel#newDownstreamSession(com.thinkparity.codebase.model.migrator.Release)
     *
     */
    public StreamSession newDownstreamSession(final Product product,
            final Release release) {
        try {
            return streamService.newDownstreamSession(getAuthToken(), product,
                    release);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.stream.InternalStreamModel#newUpstreamSession(com.thinkparity.codebase.model.stream.StreamInfo,
     *      com.thinkparity.codebase.model.document.DocumentVersion)
     * 
     */
    public StreamSession newUpstreamSession(final StreamInfo streamInfo,
            final DocumentVersion version) {
        try {
            return streamService.newUpstreamSession(getAuthToken(), streamInfo,
                    version);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.stream.InternalStreamModel#newUpstreamSession(com.thinkparity.codebase.model.stream.StreamInfo,
     *      com.thinkparity.codebase.model.migrator.Product,
     *      com.thinkparity.codebase.model.migrator.Release)
     * 
     */
    public StreamSession newUpstreamSession(final StreamInfo streamInfo,
            final Product product, final Release release) {
        try {
            return streamService.newUpstreamSession(getAuthToken(), streamInfo,
                    product, release);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.Model#initializeModel(com.thinkparity.codebase.model.session.Environment, com.thinkparity.ophelia.model.workspace.Workspace)
     *
     */
    @Override
    protected void initializeModel(final Environment environment,
            final Workspace workspace) {
        final ServiceFactory serviceFactory = workspace.getServiceFactory(environment);
        this.streamService = serviceFactory.getStreamService();
    }

    /**
     * Obtain the session authentication token.
     * 
     * @return A session <code>AuthToken</code>.
     */
    private AuthToken getAuthToken() {
        return getSessionModel().getAuthToken();
    }
}
