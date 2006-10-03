/*
 * Created On: Oct 2, 2006 11:56:42 AM
 */
package com.thinkparity.desdemona.wildfire.handler;

import org.jivesoftware.wildfire.auth.UnauthorizedException;

import org.xmpp.packet.IQ;

import com.thinkparity.desdemona.model.user.UserModel;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class AnonymousHandler extends AbstractHandler {

    /** An anonymous thinkParity user interface. */
    private UserModel userModel;

    /** Create AnonymousHandler. */
    protected AnonymousHandler(final String action) {
        super(action);
    }

    /**
     * @see com.thinkparity.desdemona.wildfire.handler.AbstractHandler#handleIQ(org.xmpp.packet.IQ)
     */
    @Override
    public IQ handleIQ(final IQ iq) throws UnauthorizedException {
        synchronized (AbstractHandler.SERIALIZER) {
            this.userModel = UserModel.getModel();
            return logVariable("response",
                    super.handleIQ(logVariable("iq", iq)));
        }
    }

    /**
     * Obtain the userModel
     *
     * @return The UserModel.
     */
    protected final UserModel getUserModel() {
        return userModel;
    }
}
