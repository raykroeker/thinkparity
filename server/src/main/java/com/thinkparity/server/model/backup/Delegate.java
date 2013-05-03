/*
 * Created On:  27-Apr-07 9:43:14 AM
 */
package com.thinkparity.ophelia.model;

/**
 * <b>Title:</b>thinkParity OpheliaModel Delegate<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 * @param <T>
 *            A type of model implementation.
 */
public interface Delegate<T extends Model> {

    /**
     * Initialize the delegate.
     * 
     * @param modelImplementation
     *            An instance of the typed model implementation.
     */
    public void initialize(final T modelImplementation);
}
