/*
 * Created On:  7-Jun-07 10:09:45 AM
 */
package com.thinkparity.service.client;

/**
 * <b>Title:</b>thinkParity Service Client Proxy Factory<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface ServiceProxyFactory {

    /**
     * Create a proxy for a service.
     * 
     * @param serviceClass
     *            A service <code>Class<?></code>.
     * @param service
     *            A <code>Service</code>.
     * @return A proxy <code>Object</code>.
     */
    public Object createProxy(final Class<?> serviceClass, final Service service);
}
