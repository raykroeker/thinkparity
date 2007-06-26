/*
 * Created On:  25-Jun-07 12:33:56 PM
 */
package com.thinkparity.ophelia.model.stream;

import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.stream.StreamSession;

import com.thinkparity.ophelia.model.Model;
import com.thinkparity.ophelia.model.workspace.Workspace;

import com.thinkparity.service.AuthToken;
import com.thinkparity.service.StreamService;
import com.thinkparity.service.client.ServiceFactory;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class StreamModelImpl extends Model implements StreamModel,
        InternalStreamModel {

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
     * @see com.thinkparity.ophelia.model.stream.InternalStreamModel#newUpstreamSession(com.thinkparity.codebase.model.document.DocumentVersion)
     *
     */
    public StreamSession newUpstreamSession(DocumentVersion version) {
        try {
            return streamService.newUpstreamSession(getAuthToken(), version);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.stream.InternalStreamModel#newUpstreamSession(com.thinkparity.codebase.model.migrator.Product,
     *      com.thinkparity.codebase.model.migrator.Release, java.lang.Long,
     *      java.lang.String, java.lang.String)
     * 
     */
    public StreamSession newUpstreamSession(final Product product,
            final Release release, final Long contentLength,
            final String contentMD5, final String contentType) {
        try {
            return streamService.newUpstreamSession(getAuthToken(), product,
                    release, contentLength, contentMD5, contentType);
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
        final ServiceFactory serviceFactory = ServiceFactory.getInstance();
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
