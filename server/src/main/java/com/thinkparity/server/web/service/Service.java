/*
 * Created On:  6-Jun-07 9:58:17 AM
 */
package com.thinkparity.desdemona.web.service;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Service {

    /** The service id. */
    private String id;

    /** The service endpoint implementation class. */
    private Class<?> seiClass;

    /**
     * Create Service.
     *
     */
    public Service() {
        super();
    }

    /**
     * @see com.thinkparity.desdemona.web.service.Service#getId()
     *
     */
    public String getId() {
        return id;
    }

    /**
     * Create an instance of a service endpoint implementation.
     * 
     * @return An instance of <code>ServiceSEI</code>.
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public ServiceSEI newSEIInstance() throws InstantiationException,
            IllegalAccessException {
        return (ServiceSEI) seiClass.newInstance();
    }

    /**
     * Set id.
     *
     * @param id
     *      A String.
     */
    public void setId(final String id) {
        this.id = id;
    }

    /**
     * Set the service endpoint implementation class.
     * 
     * @param implClass
     *            A <code>Class<?></code>.
     */
    public void setSEIClass(final Class<?> seiClass) {
        this.seiClass = seiClass;
    }
}
