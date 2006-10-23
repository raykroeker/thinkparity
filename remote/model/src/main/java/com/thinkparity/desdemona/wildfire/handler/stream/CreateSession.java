/*
 * Created On: Sun Oct 22 2006 10:23 PDT
 */
package com.thinkparity.desdemona.wildfire.handler.stream;

import com.thinkparity.codebase.model.stream.Session;

import com.thinkparity.desdemona.wildfire.handler.AbstractHandler;

/**
 *
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class CreateSession extends AbstractHandler {

    /** Create CreateSession. */
    public CreateSession() {
        super("stream:createsession");
    }

    /**
     * Create a new stream session.
     *
     */
    @Override
    public void service() {
        final Session session = create();
        com.thinkparity.codebase.assertion.Assert.assertNotYetImplemented("");
        //write("session", session);
    }

    /**
     * Create a new stream session.
     *
     * @return A <code>Session</code>.
     */
    private Session create() {
        //return StreamModel.getModel().createSession();
        throw com.thinkparity.codebase.assertion.Assert.createNotYetImplemented("");
    }
}