/*
 * Created On: Thu Sep 28 09:24:29 PDT 2006
 */
package com.thinkparity.ophelia.model.util.xmpp;

import com.thinkparity.ophelia.model.io.xmpp.XMPPMethod;

final class XMPPSystem extends AbstractXMPP {

    /**
     * Create XMPPSystem.
     *
     * @param core
     *      The <code>XMPPCore</code> interface.
     */
    XMPPSystem(final XMPPCore core) {
        super(core);
    }

    /**
     * Read the thinkParity remote version.
     *
     * @return The version.
     */
    String readVersion() {
        logger.logApiId();
        final XMPPMethod readVersion = new XMPPMethod("system:readversion");
        return execute(readVersion, Boolean.TRUE).readResultString("version");
    }
}
