/*
 * Created On:  7-Jun-07 10:09:45 AM
 */
package com.thinkparity.amazon.s3.service.client;

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
     * @param implClass
     *            A service implementation <code>Class<?></code>.
     * @return A proxy <code>Object</code>.
     */
    public Object createProxy(final Class<?> serviceClass,
            final Class<?> implClass);
}
