/*
 * Created On:  3-Jun-07 2:57:37 PM
 */
package com.thinkparity.desdemona.web.service;

import javax.jws.WebService;

import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.stream.StreamSession;

import com.thinkparity.desdemona.model.stream.StreamModel;

import com.thinkparity.service.AuthToken;
import com.thinkparity.service.StreamService;

/**
 * <b>Title:</b>thinkParity Desdemona Stream Service Endpoint Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@WebService(endpointInterface = "com.thinkparity.service.StreamService")
public class StreamSEI extends ServiceSEI implements StreamService {

    /**
     * Create StreamSEI.
     *
     */
    public StreamSEI() {
        super();
    }

    /**
     * @see com.thinkparity.service.StreamService#newDownstreamSession(com.thinkparity.service.AuthToken,
     *      com.thinkparity.codebase.model.document.DocumentVersion)
     * 
     */
    public StreamSession newDownstreamSession(final AuthToken authToken,
            final DocumentVersion version) {
        return getModel(authToken).newDownstreamSession(version);
    }

    /**
     * @see com.thinkparity.service.StreamService#newDownstreamSession(com.thinkparity.service.AuthToken,
     *      com.thinkparity.codebase.model.migrator.Product,
     *      com.thinkparity.codebase.model.migrator.Release)
     * 
     */
    public StreamSession newDownstreamSession(final AuthToken authToken,
            final Product product, final Release release) {
        return getModel(authToken).newDownstreamSession(product, release);
    }

    /**
     * @see com.thinkparity.service.StreamService#newUpstreamSession(com.thinkparity.service.AuthToken,
     *      com.thinkparity.codebase.model.document.DocumentVersion)
     * 
     */
    public StreamSession newUpstreamSession(final AuthToken authToken,
            final DocumentVersion version) {
        return getModel(authToken).newUpstreamSession(version);
    }

    /**
     * @see com.thinkparity.service.StreamService#newUpstreamSession(com.thinkparity.service.AuthToken,
     *      com.thinkparity.codebase.model.migrator.Product,
     *      com.thinkparity.codebase.model.migrator.Release)
     * 
     */
    public StreamSession newUpstreamSession(final AuthToken authToken,
            final Product product, final Release release,
            final Long contentLength, final String contentMD5,
            final String contentType) {
        return getModel(authToken).newUpstreamSession(product, release,
                contentLength, contentMD5, contentType);
    }

    /**
     * Obtain a stream model for an authenticated session.
     * 
     * @param authToken
     *            A session <code>AuthToken</code>.
     * @return A <code>StreamModel</code>.
     */
    private StreamModel getModel(final AuthToken authToken) {
        return getModelFactory(authToken).getStreamModel();
    }
}
