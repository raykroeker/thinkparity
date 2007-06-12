/*
 * Created On:  6-Jun-07 10:05:54 AM
 */
package com.thinkparity.desdemona.web.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;


/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ServiceRegistry {

    /** A singleton instance of <code>ServiceRegistry</code>. */
    private static final ServiceRegistry SINGLETON;

    static {
        SINGLETON = new ServiceRegistry();
    }

    /**
     * Obtain an instance of the service registry.
     * 
     * @return An instance of <code>ServiceRegistry</code>.
     */
    public static ServiceRegistry getInstance() {
        return SINGLETON;
    }

    /** The backing service registry. */
    private final Map<String, Service> registry;

    /**
     * Create ServiceRegistry.
     *
     */
    private ServiceRegistry() {
        super();
        this.registry = new Hashtable<String, Service>(20, 0.75F);
    }

    /**
     * Lookup a service.
     * 
     * @param id
     *            A service id <code>String</code>.
     * @return A <code>Service</code> or null if no service for the given id
     *         exists.
     */
    public Service getService(final String id) {
        return registry.get(id);
    }

    /**
     * Determine whether or not a service with the given id is registered.
     * 
     * @param id
     *            A service id.
     * @return True if the service is registered.
     */
    public Boolean containsService(final String id) {
        return Boolean.valueOf(registry.containsKey(id));
    }

    /**
     * Obtain a list of all services.
     * 
     * @return A <code>List<Service></code>.
     */
    public List<Service> services() {
        final List<Service> copy = new ArrayList<Service>(registry.size());
        copy.addAll(registry.values());
        Collections.sort(copy, new Comparator<Service>() {
            public int compare(final Service o1, final Service o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });
        return Collections.unmodifiableList(copy);
    }

    /**
     * Register a service.
     * 
     * @param service
     *            A <code>Service</code>.
     */
    void putService(final Service service) {
        registry.put(service.getId(), service);
    }

    /**
     * Unregister a service.
     * 
     * @param service
     *            A <code>Service</code>.
     */
    void removeService(final Service service) {
        registry.remove(service.getId());
    }
}
