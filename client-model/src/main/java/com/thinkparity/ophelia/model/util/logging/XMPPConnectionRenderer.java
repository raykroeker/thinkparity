/*
 * Created On: Sep 27, 2006 10:09:39 AM
 */
package com.thinkparity.ophelia.model.util.logging;

import org.apache.log4j.or.ObjectRenderer;

import org.jivesoftware.smack.XMPPConnection;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class XMPPConnectionRenderer implements ObjectRenderer {

    /** Create XMPPConnectionRenderer. */
    public XMPPConnectionRenderer() {
        super();
    }

    /**
     * @see org.apache.log4j.or.ObjectRenderer#doRender(java.lang.Object)
     */
    public String doRender(final Object o) {
        if(null == o) { 
            return new StringBuffer(XMPPConnection.class.getName()).append("//")
                    .append("null")
                    .toString();
        }
        else {
            final XMPPConnection c = (XMPPConnection) o;
            return new StringBuffer(XMPPConnection.class.getName())
                .append("//").append(c.hashCode())
                .append("?connectionID=").append(c.getConnectionID())
                .append("&host=").append(c.getHost())
                .append("&port=").append(c.getPort())
                .append("&serviceName=").append(c.getServiceName())
                .append("&isAnonymous=").append(c.isAnonymous())
                .append("&isAuthencitated=").append(c.isAuthenticated())
                .append("&isConnected=").append(c.isConnected())
                .append("&isSecureConnection=").append(c.isSecureConnection())
                .append("&isUsingCompression=").append(c.isUsingCompression())
                .append("&isUsingTLS=").append(c.isUsingTLS())
                .toString();
        }
    }

}
