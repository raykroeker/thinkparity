/*
 * Created On:  25-Jun-07 12:33:25 PM
 */
package com.thinkparity.ophelia.model.stream;

import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.stream.StreamInfo;
import com.thinkparity.codebase.model.stream.StreamSession;
import com.thinkparity.codebase.model.util.jta.TransactionType;

/**
 * <b>Title:</b>thinkParity Ophelia Model Internal Stream Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@ThinkParityTransaction(TransactionType.SUPPORTED)
public interface InternalStreamModel extends StreamModel {

    /**
     * Create a downstream instance of a stream session for a document version.
     * 
     * @param version
     *            A <code>DocumentVersion</code>.
     * @return A <code>StreamSession</code>.
     */
    public StreamSession newDownstreamSession(final DocumentVersion version);

    /**
     * Create a downstream release instance of a stream session for a release.
     * 
     * @param product
     *            A <code>Product</code>.
     * @param release
     *            A <code>Release</code>.
     * @return A <code>StreamSession</code>.
     */
    public StreamSession newDownstreamSession(final Product product,
            final Release release);

    /**
     * Create an upstream instance of a stream session for a document version.
     * 
     * @param streamInfo
     *            A <code>StreamInfo</code>.
     * @param version
     *            A <code>DocumentVersion</code>.
     * @return A <code>StreamSession</code>.
     */
    public StreamSession newUpstreamSession(final StreamInfo streamInfo,
            final DocumentVersion version);

    /**
     * Create a upstream release instance of a stream session for a release.
     * 
     * @param streamInfo
     *            A <code>StreamInfo</code>.
     * @param product
     *            A <code>Product</code>.
     * @param release
     *            A <code>Release</code>.
     * @return A <code>StreamSession</code>.
     */
    public StreamSession newUpstreamSession(final StreamInfo streamInfo,
            final Product product, final Release release);
}
