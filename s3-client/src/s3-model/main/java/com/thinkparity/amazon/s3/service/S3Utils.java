/*
 * Created On:  20-Jun-07 12:46:38 PM
 */
package com.thinkparity.amazon.s3.service;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.thinkparity.codebase.Constants;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpMethod;

import com.thinkparity.amazon.s3.service.bucket.S3Bucket;
import com.thinkparity.amazon.s3.service.object.S3Key;


/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class S3Utils {

    /** A singleton instance of s3 utils. */
    private static final S3Utils SINGLETON;

    static {
        SINGLETON = new S3Utils();
    }

    /**
     * Obtain an instance of the s3 utils.
     * 
     * @return An instance of <code>S3Utils</code>.
     */
    public static S3Utils getInstance() {
        return SINGLETON;
    }

    /** A header comparator. */
    private Comparator<Header> headerComparator;

    /** The encryption algorithm name. */
    private final String hmacSha1Algorithm;

    /** An iso 8601 date format pattern. */
    private final String iso8601Pattern;

    /**
     * Create S3Utils.
     *
     */
    private S3Utils() {
        super();
        this.headerComparator = new Comparator<Header>() {
            public int compare(final Header o1, final Header o2) {
                return o1.getName().toLowerCase().compareTo(
                        o2.getName().toLowerCase());
            }
        };
        this.hmacSha1Algorithm = "HmacSHA1";
        this.iso8601Pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    }

    /**
     * Canonicalize the http method/bucket/uri for signing.
     * 
     * @param httpMethod
     *            An <code>HttpMethod</code>.
     * @param bucket
     *            An optional <code>S3Bucket</code>.
     * @param key
     *            An optional <code>S3Key</code>.
     * @return A canonical <code>String</code> representation of the request.
     */
    public String canoncialize(final HttpMethod httpMethod,
            final S3Bucket bucket, final S3Key key, final Boolean isACLRequest) {
        // see s3-dg.pdf page 43
        final StringBuilder buffer = new StringBuilder(64);
        buffer.append(httpMethod.getName()).append('\n');
        if (isSetHeader(httpMethod, "Content-MD5"))
            buffer.append(getHeader(httpMethod, "Content-MD5").getValue());
        buffer.append('\n');
        if (isSetHeader(httpMethod, "Content-Type"))
            buffer.append(getHeader(httpMethod, "Content-Type").getValue());
        buffer.append('\n');
        buffer.append(getHeader(httpMethod, "Date").getValue()).append('\n');
        buffer.append(canonicalizeAmazonHeaders(httpMethod));
        buffer.append(canonicalizeResource(bucket, key, isACLRequest));
        return buffer.toString();
    }
    
    /**
     * Perform a base 64 ecoding on the bytes.
     * 
     * @param bytes
     *            A <code>byte[]</code>.
     * @return An encoded <code>byte[]</code>.
     */
    public byte[] encode(final byte[] bytes) {
        return Base64.encodeBase64(bytes);
    }

    /**
     * Encrypt the string using the HmacSHA1 algorithm.
     * 
     * @param auth
     *            The <code>S3Authentication</code>.
     * @param context
     *            The <code>S3Context</code>.
     * @param string
     *            The <code>String</code> to encrypt.
     * @return The encrypted <code>byte[]</code>.
     */
    public byte[] encrypt(final S3Authentication auth, final S3Context context,
            final String string) {
        // see s3-dg.pdf page 44
        final SecretKeySpec secretKeySpec = newSecretKeySpec(auth);
        Mac mac = null;
        try {
            mac = Mac.getInstance(hmacSha1Algorithm);
        } catch (final NoSuchAlgorithmException nsax) {
            throw new RuntimeException(nsax);
        }
        try {
            mac.init(secretKeySpec);
            return mac.doFinal(string.getBytes(context.getCharset()));
        } catch (final InvalidKeyException ikx) {
            throw new RuntimeException(ikx);
        }
    }

    /**
     * Format the current date time.
     * 
     * @return A formatted date/time <code>String</code>.
     */
    public String formatCurrentDateTime() {
        return newSimpleDateFormat().format(new Date());
    }

    /**
     * Create a simple date format.
     * 
     * @return A <code>SimpleDateFormat</code>.
     */
    public SimpleDateFormat newSimpleDateFormat() {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(iso8601Pattern);
        simpleDateFormat.setTimeZone(Constants.UNIVERSAL_TIME_ZONE);
        return simpleDateFormat;
    }

    /**
     * Canonicalize the amazon headers according to the s3 developer's guide.
     * Header names are all lower-cased; like-named headers are collapsed;
     * multi-line headers are unfolded; and the headers are sorted by name.
     * 
     * @param httpMethod
     *            An <code>HttpMethod</code>.
     * @return A canonicalized <code>String</code>.
     */
    private String canonicalizeAmazonHeaders(final HttpMethod httpMethod) {
        // s3-dg.pdf page 45 - lower case the header names
        final Header[] headers = httpMethod.getRequestHeaders();
        final List<Header> amazonHeaders = new ArrayList<Header>();
        Header clone;
        for (final Header header : headers) {
            if (header.getName().toLowerCase().startsWith("x-amz-")) {
                clone = new Header(header.getName().toLowerCase(),
                        header.getValue(), header.isAutogenerated());
                amazonHeaders.add(clone);
            }
        }
        // s3-dg.pdf page 45 - collapse all like-named headers
        final Map<String, Header> amazonHeaderMap = new HashMap<String, Header>(amazonHeaders.size(), 1.0F);
        Header existingHeader;
        for (final Header amazonHeader : amazonHeaders) {
            if (amazonHeaderMap.containsKey(amazonHeader.getName())) {
                existingHeader = amazonHeaderMap.get(amazonHeader.getName());
                existingHeader.setValue(new StringBuilder(existingHeader.getValue())
                    .append(',').append(amazonHeader.getValue())
                    .toString());
            }
        }
        amazonHeaders.clear();
        amazonHeaders.addAll(amazonHeaderMap.values());
        // s3-dg.pdf page 45 - unfold all headers
        char[] value;
        for (final Header amazonHeader : amazonHeaders) {
            value = amazonHeader.getValue().toCharArray();
            for (int i = 0; i < value.length; i++) {
                switch (value[i]) {
                case '\r':
                case '\n':
                    value[i] = ' ';
                    break;
                }
            }
            amazonHeader.setValue(String.valueOf(value));
        }
        // s3-dg.pdf page 45 - sort all headers
        Collections.sort(amazonHeaders, headerComparator);
        final StringBuilder buffer = new StringBuilder(128);
        for (final Header amazonHeader : amazonHeaders) {
            buffer.append(amazonHeader.getName()).append(':')
                .append(amazonHeader.getValue())
                .append('\n');
        }
        return buffer.toString();
    }

    /**
     * Canonicalize the resource.
     * 
     * @param bucket
     *            The bucket name <code>String</code>.
     * @param uri
     *            The uri <code>String</code>.
     * @param isACLRequest
     *            Whether or not the request is for acl retreival.
     * @return A canonicalized resource <code>String</code>.
     */
    private String canonicalizeResource(final S3Bucket bucket, final S3Key key,
            final Boolean isACLRequest) {
        final StringBuilder buffer = new StringBuilder(32)
            .append('/');
        // add the bucket
        if (null != bucket) {
            buffer.append(bucket.getName());
        }
        if (null != key) {
            if (null != bucket) {
                buffer.append('/');
            }
            buffer.append(key.getResource());
        }
        if (isACLRequest) {
            buffer.append("?acl");
        }
        return buffer.toString();
    }

    /**
     * Obtain a named request header from the http method.
     * 
     * @param httpMethod
     *            An <code>HttpMethod</code>.
     * @param name
     *            A header name <code>String</code>.
     * @return A <code>Header</code>.
     */
    private Header getHeader(final HttpMethod httpMethod, final String name) {
        return httpMethod.getRequestHeader(name);
    }

    /**
     * Determine if a named header is set within the http method.
     * 
     * @param httpMethod
     *            A <code>HttpMethod</code>.
     * @param name
     *            A header name <code>String</code>.
     * @return True if the header is set.
     */
    private boolean isSetHeader(final HttpMethod httpMethod, final String name) {
        final Header header = httpMethod.getRequestHeader(name);
        if (null == header) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Create an instance of a secret key spec for an authentication.
     * 
     * @param auth
     *            A <code>S3Authentication</code>.
     * @return A <code>SecretKeySpec</code>.
     */
    private SecretKeySpec newSecretKeySpec(final S3Authentication auth) {
        return new SecretKeySpec(auth.getSecretAccessKey(), hmacSha1Algorithm);
    }
}
