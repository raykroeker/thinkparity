/*
 * Created On:  6-Jun-07 9:52:38 AM
 */
package com.thinkparity.desdemona.web;

import com.thinkparity.desdemona.web.service.Operation;
import com.thinkparity.desdemona.web.service.Parameter;
import com.thinkparity.desdemona.web.service.Result;
import com.thinkparity.desdemona.web.service.Service;


/**
 * <b>Title:</b>thinkParity Desdemona Web Service Request<br>
 * <b>Description:</b>An encapsulation of a web service request. Essentially a
 * remote procedure call wrapper.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ServiceRequest {

    /** The operation to invoke. */
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
     * @return A String.
     */
    public Operation getOperation() {
        return operation;
    }

    /**
     * Obtain parameters.
     *
     * @return A Object[].
     */
    public Parameter[] getParameters() {
        return parameters;
    }

    /**
     * Obtain service.
     *
     * @return A String.
     */
    public Service getService() {
        return service;
    }

    /**
     * Invoke the service request.
     * 
     * @return A <code>ServiceResponse</code>.
     * @throws Exception
     */
    public ServiceResponse invoke() throws Exception {
        return newServiceResponse(operation.invoke(service, parameters));
    }

    /**
     * Set operation.
     *
     * @param operation
     *		A String.
     */
    public void setOperation(final Operation operation) {
        this.operation = operation;
    }

    /**
     * Set parameters.
     *
     * @param parameters
     *		A Object[].
     */
    public void setParameters(final Parameter[] parameters) {
        this.parameters = parameters;
    }

    /**
     * Set service.
     *
     * @param service
     *		A String.
     */
    public void setService(final Service service) {
        this.service = service;
    }

    /**
     * Create a new service response.
     * 
     * @param result
     *            An operation invocation result.
     * @return A <code>ServiceResponse</code>.
     */
    private ServiceResponse newServiceResponse(final Result result) {
        final ServiceResponse serviceResponse = new ServiceResponse();
        serviceResponse.setResult(result);
        return serviceResponse;
    }
}
