/*
 * Created On:  19-Jun-07 3:40:09 PM
 */
package com.thinkparity.amazon.s3.service.client;

import com.thinkparity.amazon.s3.service.bucket.BucketService;
import com.thinkparity.amazon.s3.service.client.rest.RestProxyFactory;
import com.thinkparity.amazon.s3.service.client.rest.bucket.BucketServiceImpl;
import com.thinkparity.amazon.s3.service.client.rest.object.ObjectServiceImpl;
import com.thinkparity.amazon.s3.service.object.ObjectService;

/**
 * <b>Title:</b>thinkParity Amazon S3 Client Service Factory<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ServiceFactory {

    /** A singleton instance of the service factory. */
    private static final ServiceFactory SINGLETON;

    static {
        SINGLETON = new ServiceFactory();
    }

    /**
     * Obtain an instance of the service factory.
     * 
     * @return A <code>ServiceFactory</code>.
     */
    public static ServiceFactory getInstance() {
        return SINGLETON;
    }

    /** A service proxy factory. */
    private final ServiceProxyFactory proxyFactory;

    /**
     * Create ServiceFactory.
     *
     */
    private ServiceFactory() {
        super();
        this.proxyFactory = RestProxyFactory.getInstance();
    }

    /**
     * Obtain a bucket service.
     * 
     * @return An instance of <code>BucketService</code>.
     */
    public BucketService getBucketService() {
        return (BucketService) create(BucketService.class, BucketServiceImpl.class);
    }

    /**
     * Obtain an object service.
     * 
     * @return An instance of <code>ObjectService</code>.
     */
    public ObjectService getObjectService() {
        return (ObjectService) create(ObjectService.class, ObjectServiceImpl.class);
    }

    /**
     * Create the service proxy implementation.
     * 
     * @param serviceInterface
     *            A service interface <code>Class<?></code>.
     * @return An service proxy implementation <code>Class<?></code>.
     */
    private Object create(final Class<?> serviceInterface,
            final Class<?> serviceImplementation) {
        return proxyFactory.createProxy(serviceInterface, serviceImplementation);
    }
}
