/*
 * Created On:  27-Apr-07 9:43:14 AM
 */
package com.thinkparity.desdemona.model;

/**
 * <b>Title:</b>thinkParity Desdemona Model Delegate<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 * @param <T>
 *            A type of model implementation.
 */
public interface Delegate<T extends AbstractModelImpl> {

    /**
     * Initialize the delegate.
     * 
     * @param modelImplementation
     *            An instance of the typed model implementation.
     */
    public void initialize(final T modelImplementation);
}
