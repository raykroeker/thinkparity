/*
 * Created On:  30-Mar-07 4:16:56 PM
 */
package com.thinkparity.ophelia.model.session;

/**
 * <b>Title:</b>thinkParity OpheliaModel Offline Code<br>
 * <b>Description:</b>Describes the reason for being offline.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public enum OfflineCode {

    /** The client is in a maintenance cycle. */
    CLIENT_MAINTENANCE,

    /** The client network is unavailable. */ 
    CLIENT_NETWORK_UNAVAILABLE,

    /** The client is offline. */
    CLIENT_OFFLINE
}
