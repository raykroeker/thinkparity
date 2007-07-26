/*
 * Generated On: Oct 22 06 10:33:07 AM
 */
package com.thinkparity.desdemona.model.stream;

import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.stream.StreamInfo;
import com.thinkparity.codebase.model.stream.StreamSession;

/**
 * <b>Title:</b>thinkParity Stream Model<br>
 * <b>Description:</b>
 *
 * @author CreateModel.groovy
 * @version 1.1
 */
public interface StreamModel {

    /**
     * Create a stream session for downloading the artifact version.
     * 
     * @param streamInfo
     *            A <code>StreamInfo</code>.
     * @param version
     *            A <code>DocumentVersion</code>.
     * @return A <code>StreamSession</code>.
     */
    StreamSession newDownstreamSession(DocumentVersion version);

    /**
     * Create a stream session for downloading the release.
     * 
     * @param streamInfo
     *            A <code>StreamInfo</code>.
     * @param product
     *            A <code>Product</code>.
     * @param release
     *            A <code>Release</code>.
     * @return A <code>StreamSession</code>.
     */
    StreamSession newDownstreamSession(Product product, Release release);

    /**
     * Create a stream session for uploading the document version.
     * 
     * @param streamInfo
     *            A <code>StreamInfo</code>.
     * @param version
     *            A <code>DocumentVersion</code>.
     * @return A <code>StreamSession</code>.
     */
    StreamSession newUpstreamSession(StreamInfo streamInfo,
            DocumentVersion version);

    /**
     * Create a stream session for uploading the release.
     * 
     * @param streamInfo
     *            A <code>StreamInfo</code>.
     * @param product
     *            A <code>Product</code>.
     * @param release
     *            A <code>Release</code>.
     * @return A <code>StreamSession</code>.
     */
    StreamSession newUpstreamSession(StreamInfo streamInfo, Product product,
            Release release);
}
