/*
 * Created On:  24-Jul-07 11:16:46 AM
 */
package com.thinkparity.codebase.model.crypto;


/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Secret {

    private String algorithm;

    private String format;

    private byte[] key;

    /**
     * Create VersionSecretKey.
     *
     */
    public Secret() {
        super();
    }

    /**
     * Obtain algorithm.
     *
     * @return A String.
     */
    public String getAlgorithm() {
        return algorithm;
    }

    /**
     * Obtain format.
     *
     * @return A String.
     */
    public String getFormat() {
        return format;
    }

    /**
     * Obtain key.
     *
     * @return A byte[].
     */
    public byte[] getKey() {
        return key;
    }

    /**
     * Set algorithm.
     *
     * @param algorithm
     *		A String.
     */
    public void setAlgorithm(final String algorithm) {
        this.algorithm = algorithm;
    }

    /**
     * Set format.
     *
     * @param format
     *		A String.
     */
    public void setFormat(final String format) {
        this.format = format;
    }

    /**
     * Set key.
     *
     * @param key
     *		A byte[].
     */
    public void setKey(final byte[] key) {
        this.key = key;
    }
}
