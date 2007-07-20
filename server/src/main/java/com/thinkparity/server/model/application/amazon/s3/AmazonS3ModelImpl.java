/*
 * Created On:  23-Jun-07 11:33:06 AM
 */
package com.thinkparity.desdemona.model.amazon.s3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.thinkparity.codebase.StringUtil;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.codebase.model.ThinkParityException;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.util.http.HttpUtils;

import com.thinkparity.desdemona.model.AbstractModelImpl;
import com.thinkparity.desdemona.util.DateTimeProvider;
import com.thinkparity.desdemona.util.DesdemonaProperties;

import org.apache.commons.codec.binary.Base64;

/**
 * <b>Title:</b>thinkParity Desdemona Amazon S3 Model Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class AmazonS3ModelImpl extends AbstractModelImpl implements
        AmazonS3Model, InternalAmazonS3Model {

    /** The amazon web-services access key id. */
    private static String AMZ_ACCESS_KEY_ID;

    /** The amazon web-services secret access key. */
    private static byte[] AMZ_SECRET_ACCESS_KEY;

    /** The character set used for the amazon/thinkparity communications. */
    private static final Charset CHARSET;

    /** The hmac sha1 encryption algorithm name. */
    private static final String HMAC_SHA1_ALGORITHM_NAME;

    static {
        CHARSET = StringUtil.Charset.UTF_8.getCharset();

        HMAC_SHA1_ALGORITHM_NAME = "HmacSHA1";
    }

    /**
     * Encode the encrypted bytes with a base 64 encoding.
     * 
     * @param encrypted
     *            The encrypted <code>byte[]</code>.
     * @return A <code>byte[]</code>.
     */
    private static byte[] encode(final byte[] encrypted) {
        return Base64.encodeBase64(encrypted);
    }

    /**
     * Encrypt the canonical string.
     * 
     * @param canonical
     *            A canonical <code>String</code>.
     * @return An encrypted <code>byte[]</code>.
     */
    private static byte[] encrypt(final String canonical) {
        final SecretKeySpec secretKeySpec = newSecretKeySpec();
        final Mac mac;
        try {
            mac = Mac.getInstance(HMAC_SHA1_ALGORITHM_NAME);
            mac.init(secretKeySpec);
            return mac.doFinal(canonical.getBytes(CHARSET));
        } catch (final NoSuchAlgorithmException nsax) {
            throw new ThinkParityException(nsax);
        } catch (final InvalidKeyException ikx) {
            throw new ThinkParityException(ikx);
        }
    }

    /**
     * Obtain the amazon access key id.
     * 
     * @return An access key id <code>String</code>.
     */
    private static String getAmazonAccessKeyId() throws IOException {
        if (null == AMZ_ACCESS_KEY_ID) {
            final DesdemonaProperties properties = DesdemonaProperties.getInstance();
            final File file = new File(properties.getProperty("thinkparity.amazon.accesskeyid-file"));
            AMZ_ACCESS_KEY_ID = new BufferedReader(new FileReader(file)).readLine();
        }
        return AMZ_ACCESS_KEY_ID;
    }

    /**
     * Obtain the amazon secret access key.
     * 
     * @return A <code>byte[]</code>.
     */
    private static byte[] getAmazonSecretAccessKey() throws IOException {
        if (null == AMZ_SECRET_ACCESS_KEY) {
            final DesdemonaProperties properties = DesdemonaProperties.getInstance();
            final File file = new File(properties.getProperty("thinkparity.amazon.secretaccesskey-file"));
            final InputStream stream = new FileInputStream(file);
            try {
                AMZ_SECRET_ACCESS_KEY = new byte[(int) file.length()];
                stream.read(AMZ_SECRET_ACCESS_KEY);
            } finally {
                stream.close();
            }
        }
        return AMZ_SECRET_ACCESS_KEY;
    }

    /**
     * Obtain the bucket name for the environment.
     * 
     * @param environment
     *            An <code>Environment</code>.
     * @return A bucket name <code>String</code>.
     */
    private static String getBucketName(final Environment environment) {
        switch (environment) {
        case DEVELOPMENT:
        case DEVELOPMENT_LOCALHOST:
            return "development.thinkparity";
        case DEVELOPMENT_TESTING:
        case DEVELOPMENT_TESTING_LOCALHOST:
            return "development_testing.thinkparity";
        case PRODUCTION:
            return "thinkparity";
        default:
            throw Assert.createUnreachable("Unsupported environment.");
        }
    }

    /**
     * Create a authorization header value for s3 access.
     * 
     * @return An authorization header value <code>String</code>.
     */
    private static String newAuthorization(final byte[] signature) {
        try {
            return new StringBuilder(64).append("AWS ")
                .append(getAmazonAccessKeyId()).append(':')
                .append(newString(signature)).toString();
        } catch (final IOException iox) {
            throw new ThinkParityException("Cannot create authorization.", iox);
        }
    }

    /**
     * Create an http formatted string of the current date/time.
     * 
     * @return A <code>String</code>.
     */
    private static String newDateString() {
        return HttpUtils.format(DateTimeProvider.getCurrentDateTime().getTime());
    }

    /**
     * Create an amazon s3 canonical string for a downstream get request.
     * 
     * @param version
     *            A <code>DocumentVersion</code>.
     * @param date
     *            The date <code>String</code>.
     * @return The amazon s3 canonical <code>String</code>.
     */
    private static String newDownstreamCanonical(final Environment environment,
            final DocumentVersion version, final String date) {
        return new StringBuilder(64)
            .append(HttpUtils.MethodNames.GET).append('\n')
            .append('\n')   // CONTENT_MD5
            .append('\n')   // CONTENT_TYPE
            .append(date).append('\n')
            .append('/')
            .append(getBucketName(environment))
            .append(newKey(version))
            .toString();
    }

    /**
     * Create an amazon s3 canonical string for a downstream get request.
     * 
     * @param version
     *            A <code>DocumentVersion</code>.
     * @param date
     *            The date <code>String</code>.
     * @return The amazon s3 canonical <code>String</code>.
     */
    private static String newDownstreamCanonical(final Environment environment,
            final Product product, final Release release, final String date) {
        return new StringBuilder(64)
            .append(HttpUtils.MethodNames.GET).append('\n')
            .append('\n')   // CONTENT_MD5
            .append('\n')   // CONTENT_TYPE
            .append(date).append('\n')
            .append('/')
            .append(getBucketName(environment))
            .append(newKey(product, release))
            .toString();
    }

    /**
     * Create a new instance of a header map.
     * 
     * @return A <code>Map<String, String></code>.
     */
    private static Map<String, String> newHeaderMap() {
        return new HashMap<String, String>();
    }

    /**
     * Create a new instance of an s3 key for a document version. The key is
     * <code>/artifacts/documents/UUID/VID</code> where AT is the artifact type
     * lowercased and pluralized, UUID is the artifact unique id and VID is the
     * artifact version.
     * 
     * @param version
     *            A <code>DocumentVersion</code>.
     * @return An s3 key <code>String</code>.
     */
    private static String newKey(final DocumentVersion version) {
        return new StringBuilder(32)
            .append("/artifacts/documents")
            .append('/').append(version.getArtifactUniqueId())
            .append('/').append(version.getVersionId())
            .toString();
    }

    /**
     * Create a new instance of an s3 key for a product release. The key is
     * <code>/products/PN/releases/RN/ROS</code> where PN is the product name
     * lowercased, RN is the release name and ROS is the release operating
     * system.
     * 
     * @param product
     *            A <code>Product</code>.
     * @param release
     *            A <code>Release</code>.
     * @return An s3 key <code>String</code>.
     */
    private static String newKey(final Product product, final Release release) {
        return new StringBuilder(32)
            .append("/products/")
            .append(product.getName().toLowerCase())
            .append("/releases/")
            .append(release.getName().toLowerCase())
            .append('/').append(release.getOs().name().toLowerCase())
            .toString();
    }

    /**
     * Create an instance of a secret key spec for encryption.
     * 
     * @return A <code>SecretKeySpec</code>.
     */
    private static SecretKeySpec newSecretKeySpec() {
        try {
            return new SecretKeySpec(getAmazonSecretAccessKey(),
                    HMAC_SHA1_ALGORITHM_NAME);
        } catch (final IOException iox) {
            throw new ThinkParityException("Cannot create secret key spec.", iox);
        }
    }

    /**
     * Create a new string from an encoded byte array.
     * 
     * @param encoded
     *            A <code>byte[]</code>.
     * @return A <code>String</code>.
     */
    private static String newString(final byte[] encoded) {
        return new String(encoded, CHARSET);
    }

    /**
     * Create an amazon s3 canonical string for an upstream put request.
     * 
     * @param version
     *            A <code>DocumentVersion</code>.
     * @param date
     *            The date <code>String</code>.
     * @return The amazon s3 canonical <code>String</code>.
     */
    private static String newUpstreamCanonical(final Environment environment,
            final DocumentVersion version, final String date) {
        return new StringBuilder(64)
            .append(HttpUtils.MethodNames.PUT).append('\n')
            .append(version.getChecksum()).append('\n')
            .append(HttpUtils.ContentTypeNames.BINARY_OCTET_STREAM).append('\n')
            .append(date).append('\n')
            .append('/')
            .append(getBucketName(environment))
            .append(newKey(version))
            .toString();
    }

    /**
     * Create an amazon s3 canonical string for an upstream put request.
     * 
     * @param product
     *            A <code>Product</code>.
     * @param release
     *            A <code>Release</code>.
     * @param date
     *            The date <code>String</code>.
     * @return The amazon s3 canonical <code>String</code>.
     */
    private static String newUpstreamCanonical(final Environment environment,
            final Product product, final Release release, final String date,
            final String contentMD5, final String contentType) {
        return new StringBuilder(64)
            .append(HttpUtils.MethodNames.PUT).append('\n')
            .append(contentMD5).append('\n')
            .append(contentType).append('\n')
            .append(date).append('\n')
            .append('/')
            .append(getBucketName(environment))
            .append(newKey(product, release))
            .toString();
    }

    /**
     * Create a new http uri for the environment for the document version.
     * 
     * @param environment
     *            The <code>Environment</code>.
     * @param version
     *            A <code>DocumentVersion</code>.
     * @return A uri <code>String</code>.
     */
    private static String newURI(final Environment environment,
            final DocumentVersion version) {
        return newURIBuilder(environment).append(newKey(version)).toString();
    }

    /**
     * Create a new http uri for the environment for the product release.
     * 
     * @param environment
     *            The <code>Environment</code>.
     * @param product
     *            A <code>Product</code>.
     * @param release
     *            A <code>Release</code>.
     * @return A uri <code>String</code>.
     */
    private static String newURI(final Environment environment,
            final Product product, final Release release) {
        return newURIBuilder(environment)
            .append(newKey(product, release)).toString();
    }

    /**
     * Create a new instance of a uri builder.
     * 
     * @return A <code>StringBuilder</code>.
     */
    private static StringBuilder newURIBuilder(final Environment environment) {
        return new StringBuilder("https://s3.amazonaws.com/")
            .append(getBucketName(environment));
    }

    /** The local <code>Environment</code>. */
    private Environment environment;

    /**
     * Create StorageModelImpl.
     *
     */
    public AmazonS3ModelImpl() {
        super();
    }

    /**
     * @see com.thinkparity.desdemona.model.amazon.s3.InternalAmazonS3Model#newDownstreamHeaders(com.thinkparity.codebase.model.document.DocumentVersion)
     * 
     */
    public Map<String, String> newDownstreamHeaders(final DocumentVersion version) {
        final String date = newDateString();
        final String canonical = newDownstreamCanonical(environment, version, date);
        logger.logVariable("canonical", canonical);
        final byte[] encrypted = encrypt(canonical);
        final byte[] signature = encode(encrypted);
        final String authorization = newAuthorization(signature);
        final Map<String, String> headers = newHeaderMap();
        headers.put(HttpUtils.HeaderNames.AUTHORIZATION, authorization);
        headers.put(HttpUtils.HeaderNames.DATE, date);
        return headers;
    }

    /**
     * @see com.thinkparity.desdemona.model.amazon.s3.InternalAmazonS3Model#newDownstreamHeaders(com.thinkparity.codebase.model.migrator.Product, com.thinkparity.codebase.model.migrator.Release)
     *
     */
    public Map<String, String> newDownstreamHeaders(final Product product,
            final Release release) {
        final String date = newDateString();
        final String canonical = newDownstreamCanonical(environment, product, release, date);
        logger.logVariable("canonical", canonical);
        final byte[] encrypted = encrypt(canonical);
        final byte[] signature = encode(encrypted);
        final String authorization = newAuthorization(signature);
        final Map<String, String> headers = newHeaderMap();
        headers.put(HttpUtils.HeaderNames.AUTHORIZATION, authorization);
        headers.put(HttpUtils.HeaderNames.DATE, date);
        return headers;
    }

    /**
     * @see com.thinkparity.desdemona.model.amazon.s3.InternalAmazonS3Model#newDownstreamURI(com.thinkparity.codebase.model.document.DocumentVersion)
     * 
     */
    public String newDownstreamURI(final DocumentVersion version) {
        return newURI(environment, version);
    }

    /**
     * @see com.thinkparity.desdemona.model.amazon.s3.InternalAmazonS3Model#newDownstreamURI(com.thinkparity.codebase.model.migrator.Product, com.thinkparity.codebase.model.migrator.Release)
     *
     */
    public String newDownstreamURI(final Product product, final Release release) {
        return newURI(environment, product, release);
    }

    /**
     * @see com.thinkparity.desdemona.model.amazon.s3.InternalAmazonS3Model#newUpstreamHeaders(com.thinkparity.codebase.model.document.DocumentVersion)
     * 
     */
    public Map<String, String> newUpstreamHeaders(final DocumentVersion version) {
        final String date = newDateString();
        final String canonical = newUpstreamCanonical(environment, version, date);
        logger.logVariable("canonical", canonical);
        final byte[] encrypted = encrypt(canonical);
        final byte[] signature = encode(encrypted);
        final String authorization = newAuthorization(signature);
        final Map<String, String> headers = newHeaderMap();
        headers.put(HttpUtils.HeaderNames.AUTHORIZATION, authorization);
        headers.put(HttpUtils.HeaderNames.CONTENT_LENGTH, String.valueOf(version.getSize()));
        headers.put(HttpUtils.HeaderNames.CONTENT_MD5, version.getChecksum());
        headers.put(HttpUtils.HeaderNames.CONTENT_TYPE, HttpUtils.ContentTypeNames.BINARY_OCTET_STREAM);
        headers.put(HttpUtils.HeaderNames.DATE, date);
        return headers;
    }

    /**
     * @see com.thinkparity.desdemona.model.amazon.s3.InternalAmazonS3Model#newUpstreamHeaders(com.thinkparity.codebase.model.migrator.Product, com.thinkparity.codebase.model.migrator.Release)
     *
     */
    public Map<String, String> newUpstreamHeaders(final Product product,
            final Release release, final Long contentLength,
            final String contentMD5, final String contentType) {
        final String date = newDateString();
        final String canonical = newUpstreamCanonical(environment, product,
                release, date, contentMD5, contentType);
        logger.logVariable("canonical", canonical);
        final byte[] encrypted = encrypt(canonical);
        final byte[] signature = encode(encrypted);
        final String authorization = newAuthorization(signature);
        final Map<String, String> headers = newHeaderMap();
        headers.put(HttpUtils.HeaderNames.AUTHORIZATION, authorization);
        headers.put(HttpUtils.HeaderNames.CONTENT_LENGTH, String.valueOf(contentLength));
        headers.put(HttpUtils.HeaderNames.CONTENT_MD5, contentMD5);
        headers.put(HttpUtils.HeaderNames.CONTENT_TYPE, contentType);
        headers.put(HttpUtils.HeaderNames.DATE, date);
        return headers;
    }

    /**
     * @see com.thinkparity.desdemona.model.amazon.s3.InternalAmazonS3Model#newUpstreamURI(com.thinkparity.codebase.model.document.DocumentVersion)
     * 
     */
    public String newUpstreamURI(final DocumentVersion version) {
        return newURI(environment, version);
    }

    /**
     * @see com.thinkparity.desdemona.model.amazon.s3.InternalAmazonS3Model#newUpstreamURI(com.thinkparity.codebase.model.migrator.Product, com.thinkparity.codebase.model.migrator.Release)
     *
     */
    public String newUpstreamURI(final Product product, final Release release) {
        return newURI(environment, product, release);
    }

    /**
     * @see com.thinkparity.desdemona.model.AbstractModelImpl#initialize()
     *
     */
    @Override
    protected void initialize() {
        final DesdemonaProperties properties = DesdemonaProperties.getInstance();

        this.environment = Environment.valueOf(properties.getProperty("thinkparity.environment"));
    }
}
