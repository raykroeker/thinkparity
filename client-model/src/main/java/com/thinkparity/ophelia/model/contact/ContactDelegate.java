/*
 * Created On:  27-Nov-07 5:58:33 PM
 */
package com.thinkparity.ophelia.model.contact;

import com.thinkparity.ophelia.model.DefaultDelegate;
import com.thinkparity.ophelia.model.io.handler.ContactIOHandler;

/**
 * <b>Title:</b>thinkParity Ophelia Model Contact Delegate Abstraction<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class ContactDelegate extends DefaultDelegate<ContactModelImpl> {

    /** A contact persistence interface. */
    protected ContactIOHandler contactIO;

    /**
     * Create ContactDelegate.
     *
     */
    protected ContactDelegate() {
        super();
    }

    /**
     * @see com.thinkparity.ophelia.model.DefaultDelegate#initialize(com.thinkparity.ophelia.model.Model)
     *
     */
    @Override
    public void initialize(final ContactModelImpl modelImplementation) {
        super.initialize(modelImplementation);

        contactIO = modelImplementation.getContactIO();
    }
}
