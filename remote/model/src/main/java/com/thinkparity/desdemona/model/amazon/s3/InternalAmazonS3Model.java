/*
 * Created On:  23-Jun-07 11:32:48 AM
 */
package com.thinkparity.desdemona.model.amazon.s3;

import java.util.Map;

import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.migrator.Release;

/**
 * <b>Title:</b>thinkParity Desdemona Internal Amazon S3 Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface InternalAmazonS3Model extends AmazonS3Model {

    /**
     * Create an instance of downstream headers used to download an artifact
     * version.
     * 
     * @param version
     *            A <code>DocumentVersion</code>.
     * @return A downstream header <code>Map<String, String></code>.
     */
    Map<String, String> newDownstreamHeaders(DocumentVersion version);

    /**
     * Create an instance of downstream headers used to download a product
     * release.
     * 
     * @param product
     *            A <code>Product</code>.
     * @param release
     *            A <code>Release</code>.
     * @return A downstream header <code>Map<String, String></code>.
     */
    Map<String, String> newDownstreamHeaders(Product product, Release release);

    /**
     * Create an instance of a downstream uri used to download an artifact
     * version.
     * 
     * @param version
     *            A <code>DocumentVersion</code>.
     * @return A download uri <code>String</code>.
     */
    String newDownstreamURI(final DocumentVersion version);

    /**
     * Create an instance of a downstream uri used to download a product
     * release.
     * 
     * @param product
     *            A <code>Product</code>.
     * @param release
     *            A <code>Release</code>.
     * @return A download uri <code>String</code>.
     */
    String newDownstreamURI(final Product product, final Release release);

    /**
     * Create an instance of upstream headers used to upload an artifact
     * version.
     * 
     * @param streamInfo
     *            An <code>AmazonS3StreamInfo<code>.
     * @param version
     *            A <code>DocumentVersion</code>.
     * @return An upstream header <code>Map<String, String></code>.
     */
    Map<String, String> newUpstreamHeaders(AmazonS3StreamInfo streamInfo,
            DocumentVersion version);

    /**
     * Create an instance of upstream headers used to upload a product release.
     * 
     * @param streamInfo
     *            An <code>AmazonS3StreamInfo</code>.
     * @param product
     *            A <code>Product</code>.
     * @param release
     *            A <code>Release</code>.
     * @return An upstream header <code>Map<String, String></code>.
     */
    Map<String, String> newUpstreamHeaders(AmazonS3StreamInfo streamInfo,
            Product product, Release release);

    /**
     * Create an instance of a upstream uri used to upload an artifact
     * version.
     * 
     * @param version
     *            A <code>DocumentVersion</code>.
     * @return An upload uri <code>String</code>.
     */
    String newUpstreamURI(DocumentVersion version);

    /**
     * Create an instance of a upstream uri used to upload a product release.
     * 
     * @param product
     *            A <code>Product</code>.
     * @param release
     *            A <code>Release</code>.
     * @return An upload uri <code>String</code>.
     */
    String newUpstreamURI(Product product, Release release);
}
