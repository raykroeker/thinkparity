/*
 * Created On:  3-Sep-07 6:22:32 PM
 */
package com.thinkparity.desdemona.model.profile;

/**
 * <b>Title:</b>thinkParity Desdemona Model Profile Payment Callback<br>
 * <b>Description:</b>Instances of the profile model instantiate and pass the
 * payment callback to the core profile creation api such that the call can be
 * made transactional.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
interface PaymentCallback {

    /**
     * Invoke the payment callback.
     * 
     */
    void call();
}
