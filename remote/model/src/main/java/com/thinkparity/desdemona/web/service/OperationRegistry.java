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
public class OperationRegistry {

    /** A singleton instance of the operation registry. */
    private static final OperationRegistry SINGLETON;

    static {
        SINGLETON = new OperationRegistry();
    }

    /**
     * Obtain an instance of the operation registry.
     * 
     * @return An instance of <code>OperationRegistry</code>.
     */
    public static OperationRegistry getInstance() {
        return SINGLETON;
    }

    /** The service/operation backing registry. */
    private final Map<Service, Map<String, Operation>> registry;

    /**
     * Create OperationRegistry.
     *
     */
    private OperationRegistry() {
        super();
        this.registry = new Hashtable<Service, Map<String, Operation>>(20, 0.75F);
    }

    /**
     * Lookup an operation.
     * 
     * @param service
     *            A <code>Service</code>.
     * @param id
     *            A service id <code>String</code>.
     * @return An <code>Operation</code> or null if no operation for the given
     *         service and id exists.
     */
    public Operation getOperation(final Service service, final String id) {
        final Map<String, Operation> operations = registry.get(service);
        return null == operations ? null : operations.get(id);
    }

    /**
     * Obtain a list of all operations for a service.
     * 
     * @return A <code>List</code> of operations.
     */
    List<Operation> operations(final Service service) {
        final List<Operation> operations = new ArrayList<Operation>();
        operations.addAll(registry.get(service).values());
        Collections.sort(operations, new Comparator<Operation>() {
            public int compare(final Operation o1, final Operation o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });
        return Collections.unmodifiableList(operations);
    }

    /**
     * Store an operation in the registry.
     * 
     * @param service
     *            A <code>Service</code>.
     * @param operation
     *            An <code>Operation</code>.
     * @return The previous <code>Operation</code>.
     */
    Operation putOperation(final Service service, final Operation operation) {
        Map<String, Operation> operations = registry.get(service);
        if (null == operations) {
            operations = new Hashtable<String, Operation>(20, 0.75F);
        }
        final Operation previous = operations.put(operation.getId(), operation);
        registry.put(service, operations);
        return previous;
    }

    /**
     * Remove an operation for a service from the registry.
     * 
     * @param service
     *            A <code>Service</code>.
     * @param operation
     *            An <code>Operation</code>.
     * @return The <code>Operation</code>.
     */
    Operation removeOperation(final Service service, final Operation operation) {
        final Map<String, Operation> operations = registry.get(service);
        if (null == operations) {
            return null;
        } else {
            return operations.remove(operation.getId());
        }
    }
}
