/*
 * Created On: Sep 16, 2006 1:54:18 PM
 */
package com.thinkparity.codebase.model;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class AbstractModelImpl {

    private Context context;

    protected AbstractModelImpl() {
        super();
    }

    /**
     * Obtain the context
     *
     * @return The Context.
     */
    protected final Context getContext() {
        return context;
    }

    /**
     * Set context.
     *
     * @param context The Context.
     */
    public final void setContext(final Context context) {
        this.context = context;
    }
}
