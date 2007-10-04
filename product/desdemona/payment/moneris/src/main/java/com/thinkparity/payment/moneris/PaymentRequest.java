/*
 * Created On:  4-Sep-07 10:24:21 AM
 */
package com.thinkparity.payment.moneris;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class PaymentRequest {

    /** The api token. */
    private String apiToken;

    /** The host. */
    private String host;

    /** The store id. */
    private String storeId;

    /**
     * Create PaymentRequest.
     *
     */
    PaymentRequest() {
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
