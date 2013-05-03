/*
 * Oct 9, 2005
 */
package com.thinkparity.ophelia.model;

/**
 * <b>Title:</b>thinkParity OpheliaModel Helper<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class ModelHelper<T extends Model> {

    /** The typed <code>Model</code>. */
    private final T model;

    /**
     * Create ModelHelper.
     * 
     * @param model
     *            The <code>Model</code>.
     */
	protected ModelHelper(final T model) {
        super();
        this.model = model;
    }

    /**
     * Panic. Nothing can be done about the error that has been generated. An
     * appropriate error is constructed suitable for throwing beyond the model
     * interface.
     * 
     * @param t
     *            A <code>Throwable</code>.
     * @return A <code>RuntimeException</code>.
     */
    protected RuntimeException panic(final Throwable t) {
        return model.panic(t);
    }
}
