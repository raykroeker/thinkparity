/*
 * Created On:  31-Aug-07 10:46:12 AM
 */
package com.thinkparity.service.client;

/**
 * <b>Title:</b>thinkParity Service Constants<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Constants {

    /** <b>Title:</b>Http Client Constants<br> */
    public static final class HttpClient {

        /** <b>Title:</b>Http Client Constants:  Connection Manager<br> */
        public static final class ConnectionManager {
            public static final Integer MAX_TOTAL_CONNECTIONS = 3;
            public static final Integer SO_TIMEOUT = 90 * 1000;
            public static final Boolean TCP_NODELAY = Boolean.TRUE;
        }
    }

    /**
     * Create Constants.
     *
     */
    private Constants() {
        super();
    }
}
