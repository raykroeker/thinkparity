/*
 * Created On:  5-Sep-07 1:26:50 PM
 */
package com.thinkparity.payment.moneris.delegate;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class Constants {

    /**
     * Create Constants.
     *
     */
    Constants() {
        super();
    }

    static final class ResponseCode {
        /** The highest value a response code can be for approval. */
        static int APPROVED_CEILING = 50;
        static int EXPIRED = 482;
    }
}
