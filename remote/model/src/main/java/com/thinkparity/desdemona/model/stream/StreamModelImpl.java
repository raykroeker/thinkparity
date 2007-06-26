/*
 * Generated On: Oct 22 06 10:33:07 AM
 */
package com.thinkparity.desdemona.model.stream;

import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.stream.StreamSession;

import com.thinkparity.desdemona.model.AbstractModelImpl;
import com.thinkparity.desdemona.model.amazon.s3.InternalAmazonS3Model;

/**
 * <b>Title:</b>thinkParity Stream Model Implementation</br>
 * <b>Description:</b>
 *
 * @author CreateModel.groovy
 * @version 1.1
 */
public final class StreamModelImpl extends AbstractModelImpl implements
        StreamModel, InternalStreamModel {

    /**
     * Create StreamModelImpl.
     *
     */
    public StreamModelImpl() {
        super();
    }

    /**
     * @see com.thinkparity.desdemona.model.stream.StreamModel#newDownstreamSession(com.thinkparity.codebase.model.document.DocumentVersion)
     * 
     */
    public StreamSession newDownstreamSession(final DocumentVersion version) {
        try {
            final StreamSession session = new StreamSession();
            session.setBufferSize(getBufferSize("stream-session"));
            final InternalAmazonS3Model amazonS3Model = getAmazonS3Model();
            session.setHeaders(amazonS3Model.newDownstreamHeaders(version));
            session.setURI(amazonS3Model.newDownstreamURI(version));
            return session;
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.stream.StreamModel#newDownstreamSession(com.thinkparity.codebase.model.migrator.Release)
     *
     */
    public StreamSession newDownstreamSession(final Product product,
            final Release release) {
        try {
            final StreamSession session = new StreamSession();
            session.setBufferSize(getBufferSize("stream-session"));
            final InternalAmazonS3Model amazonS3Model = getAmazonS3Model();
            session.setHeaders(amazonS3Model.newDownstreamHeaders(product, release));
            session.setURI(amazonS3Model.newDownstreamURI(product, release));
            return session;
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.stream.StreamModel#newUpstreamSession(com.thinkparity.codebase.model.document.DocumentVersion)
     * 
     */
    public StreamSession newUpstreamSession(final DocumentVersion version) {
        try {
            final StreamSession session = new StreamSession();
            session.setBufferSize(getBufferSize("stream-session"));
            final InternalAmazonS3Model amazonS3Model = getAmazonS3Model();
            session.setHeaders(amazonS3Model.newUpstreamHeaders(version));
            session.setURI(amazonS3Model.newUpstreamURI(version));
            return session;
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.stream.StreamModel#newUpstreamSession(com.thinkparity.codebase.model.migrator.Product,
     *      com.thinkparity.codebase.model.migrator.Release, java.lang.Long,
     *      java.lang.String, java.lang.String)
     * 
     */
    public StreamSession newUpstreamSession(final Product product,
            final Release release, final Long contentLength,
            final String contentMD5, final String contentType) {
        try {
            final StreamSession session = new StreamSession();
            session.setBufferSize(getBufferSize("stream-session"));
            final InternalAmazonS3Model amazonS3Model = getAmazonS3Model();
            session.setHeaders(amazonS3Model.newUpstreamHeaders(product,
                    release, contentLength, contentMD5, contentType));
            session.setURI(amazonS3Model.newUpstreamURI(product, release));
            return session;
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.AbstractModelImpl#initialize()
     *
     */
    @Override
    protected void initialize() {}
}
