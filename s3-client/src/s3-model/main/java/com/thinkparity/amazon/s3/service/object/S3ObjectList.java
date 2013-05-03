/*
 * Created On:  22-Jun-07 8:50:53 AM
 */
package com.thinkparity.amazon.s3.service.object;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class S3ObjectList {

    /** The bucket name. */
    private String bucketName;

    /** The common prefixes. */
    private final List<String> commonPrefixes;

    /** A delimiter. */
    private String delimiter;

    /** The marker. */
    private String marker;

    /** The max number of keys. */
    private Integer maxKeys;

    /** The objects. */
    private final List<S3Object> objects;

    /** The prefix. */
    private String prefix;

    /** A flag indicating if the list was truncated. */
    private Boolean truncated;

    /**
     * Create S3ObjectList.
     *
     */
    public S3ObjectList() {
        super();
        this.commonPrefixes = new ArrayList<String>();
        this.objects = new ArrayList<S3Object>();
    }

    /**
     * Add a common prefix.
     * 
     * @param commonPrefix
     *            A common prefix <code>String</code>.
     */
    public void addCommonPrefix(final String commonPrefix) {
        this.commonPrefixes.add(commonPrefix);
    }

    /**
     * Add an object.
     * 
     * @param object
     *            A <code>S3Object</code>.
     */
    public void addObject(final S3Object object) {
        this.objects.add(object);
    }

    /**
     * Clear the common prefixes.
     *
     */
    public void clearCommonPrefixes() {
        this.commonPrefixes.clear();
    }

    /**
     * Clear all objects.
     * 
     */
    public void clearObjects() {
        this.objects.clear();
    }

    /**
     * Obtain bucket.
     *
     * @return A S3Bucket.
     */
    public String getBucketName() {
        return bucketName;
    }

    /**
     * Obtain commonPrefixes.
     *
     * @return A List<String>.
     */
    public List<String> getCommonPrefixes() {
        return commonPrefixes;
    }

    /**
     * Obtain delimiter.
     *
     * @return A String.
     */
    public String getDelimiter() {
        return delimiter;
    }

    /**
     * Obtain marker.
     *
     * @return A String.
     */
    public String getMarker() {
        return marker;
    }

    /**
     * Obtain maxKeys.
     *
     * @return A Integer.
     */
    public Integer getMaxKeys() {
        return maxKeys;
    }

    /**
     * Obtain objects.
     *
     * @return A List<S3Object>.
     */
    public List<S3Object> getObjects() {
        return objects;
    }

    /**
     * Obtain prefix.
     *
     * @return A String.
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Obtain truncated.
     *
     * @return A Boolean.
     */
    public Boolean isTruncated() {
        return truncated;
    }

    /**
     * Remove a common prefix.
     * 
     * @param commonPrefix
     *            A common prefix <code>String</code>.
     */
    public void removeCommonPrefix(final String commonPrefix) {
        this.commonPrefixes.remove(commonPrefix);
    }

    /**
     * Remove an object.
     * 
     * @param object
     *            A <code>S3Object</code>.
     */
    public void removeObject(final S3Object object) {
        this.objects.remove(object);
    }

    /**
     * Set bucket.
     *
     * @param bucket
     *		A S3Bucket.
     */
    public void setBucketName(final String bucketName) {
        this.bucketName = bucketName;
    }

    /**
     * Set commonPrefixes.
     *
     * @param commonPrefixes
     *		A List<String>.
     */
    public void setCommonPrefixes(final List<String> commonPrefixes) {
        this.commonPrefixes.clear();
        this.commonPrefixes.addAll(commonPrefixes);
    }

    /**
     * Set delimiter.
     *
     * @param delimiter
     *		A String.
     */
    public void setDelimiter(final String delimiter) {
        this.delimiter = delimiter;
    }

    /**
     * Set marker.
     *
     * @param marker
     *		A String.
     */
    public void setMarker(final String marker) {
        this.marker = marker;
    }

    /**
     * Set maxKeys.
     *
     * @param maxKeys
     *		A Integer.
     */
    public void setMaxKeys(final Integer maxKeys) {
        this.maxKeys = maxKeys;
    }

    /**
     * Set objects.
     *
     * @param objects
     *		A List<S3Object>.
     */
    public void setObjects(final List<S3Object> objects) {
        this.objects.clear();
        this.objects.addAll(objects);
    }

    /**
     * Set prefix.
     *
     * @param prefix
     *		A String.
     */
    public void setPrefix(final String prefix) {
        this.prefix = prefix;
    }

    /**
     * Set truncated.
     *
     * @param truncated
     *		A Boolean.
     */
    public void setTruncated(final Boolean truncated) {
        this.truncated = truncated;
    }
}
