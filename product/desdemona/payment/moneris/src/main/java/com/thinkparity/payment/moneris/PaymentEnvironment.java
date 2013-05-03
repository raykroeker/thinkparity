/*
 * Created On:  4-Sep-07 8:25:12 PM
 */
package com.thinkparity.payment.moneris;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class PaymentEnvironment {

    /** An api token. */
    private String apiToken;

    /** An encryption. */
    private String encryption;

    /** An environment host. */
    private String host;

    /** A store id. */
    private String storeId;

    /**
     * Create PaymentEnvironment.
     *
     */
    PaymentEnvironment() {
        super();
    }

    /**
     * Obtain the apiToken.
     *
     * @return A <code>String</code>.
     */
    public String getAPIToken() {
        return apiToken;
    }

    /**
     * Obtain the encryption.
     *
     * @return A <code>String</code>.
     */
    public String getEncryption() {
        return encryption;
    }

    /**
     * Obtain the host.
     *
     * @return A <code>String</code>.
     */
    public String getHost() {
        return host;
    }

    /**
     * Obtain the storeId.
     *
     * @return A <code>String</code>.
     */
    public String getStoreId() {
        return storeId;
    }

    /**
     * Set the apiToken.
     *
     * @param apiToken
     *		A <code>String</code>.
     */
    public void setAPIToken(final String apiToken) {
        this.apiToken = apiToken;
    }

    /**
     * Set the encryption.
     *
     * @param encryption
     *		A <code>String</code>.
     */
    public void setEncryption(final String encryption) {
        this.encryption = encryption;
    }

    /**
     * Set the host.
     *
     * @param host
     *		A <code>String</code>.
     */
    public void setHost(final String host) {
        this.host = host;
    }

    /**
     * Set the storeId.
     *
     * @param storeId
     *		A <code>String</code>.
     */
    public void setStoreId(final String storeId) {
        this.storeId = storeId;
    }
}
