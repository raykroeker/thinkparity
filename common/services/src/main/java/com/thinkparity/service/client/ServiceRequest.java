/*
 * Created On:  7-Jun-07 9:41:59 AM
 */
package com.thinkparity.service.client;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ServiceRequest {

    /** An operation to invoke. */
    private Operation operation;

    /** The operation parameters. */
    private Parameter[] parameters;

    /** The service. */
    private Service service;

    /**
     * Create ServiceRequest.
     *
     */
    public ServiceRequest() {
        super();
    }

    /**
     * Obtain operation.
     *
     * @return A Operation.
     */
    public Operation getOperation() {
        return operation;
    }

    /**
     * Obtain parameters.
     *
     * @return A Parameter[].
     */
    public Parameter[] getParameters() {
        return parameters;
    }

    /**
     * Obtain service.
     *
     * @return A Service.
     */
    public Service getService() {
        return service;
    }

    /**
     * Set operation.
     *
     * @param operation
     *		A Operation.
     */
    public void setOperation(final Operation operation) {
        this.operation = operation;
    }

    /**
     * Set parameters.
     *
     * @param parameters
     *		A Parameter[].
     */
    public void setParameters(final Parameter[] parameters) {
        this.parameters = parameters;
    }

    /**
     * Set service.
     *
     * @param service
     *		A Service.
     */
    public void setService(final Service service) {
        this.service = service;
    }
}
