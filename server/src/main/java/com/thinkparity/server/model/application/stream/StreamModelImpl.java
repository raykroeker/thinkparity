/*
 * Generated On: Oct 22 06 10:33:07 AM
 */
package com.thinkparity.desdemona.model.stream;

import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.stream.StreamInfo;
import com.thinkparity.codebase.model.stream.StreamSession;
import com.thinkparity.codebase.model.util.http.HttpUtils;

import com.thinkparity.desdemona.model.AbstractModelImpl;
import com.thinkparity.desdemona.model.Constants;
import com.thinkparity.desdemona.model.amazon.s3.AmazonS3StreamInfo;
import com.thinkparity.desdemona.model.amazon.s3.InternalAmazonS3Model;
import com.thinkparity.desdemona.model.migrator.Archive;

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
     * @see com.thinkparity.desdemona.model.stream.StreamModel#newDownstreamSession(com.thinkparity.codebase.model.stream.StreamInfo,
     *      com.thinkparity.codebase.model.document.DocumentVersion)
     * 
     */
    public StreamSession newDownstreamSession(final DocumentVersion version) {
        try {
            final StreamSession session = new StreamSession();
            session.setBufferSize(getBufferSize("stream-session"));
            session.setRetryAttempts(Constants.Stream.DOWNSTREAM_RETRY_ATTEMPTS);
            final InternalAmazonS3Model amazonS3Model = getAmazonS3Model();
            session.setHeaders(amazonS3Model.newDownstreamHeaders(version));
            session.setURI(amazonS3Model.newDownstreamURI(version));
            return session;
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.stream.StreamModel#newDownstreamSession(com.thinkparity.codebase.model.stream.StreamInfo,
     *      com.thinkparity.codebase.model.migrator.Product,
     *      com.thinkparity.codebase.model.migrator.Release)
     * 
     */
    public StreamSession newDownstreamSession(final Product product,
            final Release release) {
        try {
            final StreamSession session = new StreamSession();
            session.setBufferSize(getBufferSize("stream-session"));
            session.setRetryAttempts(Constants.Stream.DOWNSTREAM_RETRY_ATTEMPTS);
            final InternalAmazonS3Model amazonS3Model = getAmazonS3Model();
            session.setHeaders(amazonS3Model.newDownstreamHeaders(product, release));
            session.setURI(amazonS3Model.newDownstreamURI(product, release));
            return session;
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.stream.StreamModel#newUpstreamSession(com.thinkparity.codebase.model.stream.StreamInfo,
     *      com.thinkparity.codebase.model.document.DocumentVersion)
     * 
     */
    public StreamSession newUpstreamSession(final StreamInfo streamInfo,
            final DocumentVersion version) {
        try {
            final StreamSession session = new StreamSession();
            session.setBufferSize(getBufferSize("stream-session"));
            session.setRetryAttempts(Constants.Stream.UPSTREAM_RETRY_ATTEMPTS);
            final InternalAmazonS3Model amazonS3Model = getAmazonS3Model();
            final AmazonS3StreamInfo amzS3StreamInfo = new AmazonS3StreamInfo();
            amzS3StreamInfo.setMD5(streamInfo.getMD5());
            amzS3StreamInfo.setLength(String.valueOf(streamInfo.getSize()));
            amzS3StreamInfo.setType(HttpUtils.ContentTypeNames.BINARY_OCTET_STREAM);
            session.setHeaders(amazonS3Model.newUpstreamHeaders(amzS3StreamInfo, version));
            session.setURI(amazonS3Model.newUpstreamURI(version));
            return session;
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.stream.StreamModel#newUpstreamSession(com.thinkparity.codebase.model.stream.StreamInfo,
     *      com.thinkparity.codebase.model.migrator.Product,
     *      com.thinkparity.codebase.model.migrator.Release)
     * 
     */
    public StreamSession newUpstreamSession(final StreamInfo streamInfo,
            final Product product, final Release release) {
        try {
            final StreamSession session = new StreamSession();
            session.setBufferSize(getBufferSize("stream-session"));
            session.setRetryAttempts(Constants.Stream.UPSTREAM_RETRY_ATTEMPTS);
            final InternalAmazonS3Model amazonS3Model = getAmazonS3Model();
            final AmazonS3StreamInfo amzS3StreamInfo = new AmazonS3StreamInfo();
            amzS3StreamInfo.setMD5(streamInfo.getMD5());
            amzS3StreamInfo.setLength(String.valueOf(streamInfo.getSize()));
            amzS3StreamInfo.setType(HttpUtils.ContentTypeNames.APPLICATION_ZIP);
            session.setHeaders(amazonS3Model.newUpstreamHeaders(amzS3StreamInfo,
                    product, release));
            session.setURI(amazonS3Model.newUpstreamURI(product, release));
            return session;
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.stream.InternalStreamModel#newUpstreamSession(StreamInfo, Product, Release, Archive)
     *
     */
    @Override
    public StreamSession newUpstreamSession(final StreamInfo streamInfo,
            final Product product, final Release release, final Archive archive) {
        try {
            final StreamSession session = new StreamSession();
            session.setBufferSize(getBufferSize("stream-session"));
            session.setRetryAttempts(Constants.Stream.UPSTREAM_RETRY_ATTEMPTS);
            final InternalAmazonS3Model amazonS3Model = getAmazonS3Model();
            final AmazonS3StreamInfo amzS3StreamInfo = new AmazonS3StreamInfo();
            amzS3StreamInfo.setMD5(streamInfo.getMD5());
            amzS3StreamInfo.setLength(String.valueOf(streamInfo.getSize()));
            amzS3StreamInfo.setType(HttpUtils.ContentTypeNames.BINARY_OCTET_STREAM);
            session.setHeaders(amazonS3Model.newUpstreamHeaders(amzS3StreamInfo,
                    product, release, archive));
            session.setURI(amazonS3Model.newUpstreamURI(product, release,
                    archive));
            return session;
        } catch (final Exception x) {
            throw panic(x);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.AbstractModelImpl#initialize()
     *
     */
    @Override
    protected void initialize() {}
}
