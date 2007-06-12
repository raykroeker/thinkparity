/*
 * Created On:  6-Jun-07 10:05:46 AM
 */
package com.thinkparity.desdemona.web.service;

/**
 * <b>Title:</b>thinkParity Desdemona Web Service Factory<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class ServiceFactory {

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
    static ServiceFactory getInstance() {
        return SINGLETON;
    }

    /** The service registry. */
    private final ServiceRegistry registry;

    /**
     * Create ServiceFactory.
     *
     */
    private ServiceFactory() {
        super();
        this.registry = ServiceRegistry.getInstance();
    }

    /**
     * Create a new instance of a service.
     * 
     * @param <T>
     *            A type of service.
     * @param serviceClass
     *            A service <code>Class<T></code>.
     * @return A <code>Service<T></code>.
     */
    Service newService(final Class<Service> serviceClass) {
        try {
            final Service instance = (Service) serviceClass.newInstance();
            register(instance);
            return instance;
        } catch (final IllegalAccessException ix) {
            throw new ServiceException(ix);
        } catch (final InstantiationException ix) {
            throw new ServiceException(ix);
        }
    }

    /**
     * Register a service.
     * 
     * @param service
     *            A <code>Service</code>.
     */
    private void register(final Service service) {
        registry.putService(service);
    }
}
