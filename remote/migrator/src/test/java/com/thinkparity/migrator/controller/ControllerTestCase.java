/*
 * Created On: Thu May 11 2006 08:28 PDT
 * $Id$
 */
package com.thinkparity.migrator.controller;

import org.xmpp.packet.IQ;
import org.xmpp.packet.PacketError;

import com.thinkparity.codebase.StringUtil.Separator;

import com.thinkparity.migrator.MigratorTestCase;

/**
 * A controller test case abstraction.
 *
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public abstract class ControllerTestCase extends MigratorTestCase {

    protected static String createFailMessage(final IQ iq) {
        final PacketError packetError = iq.getError();
        return new StringBuffer("[RMIGRATOR] [CONTROLLER TEST] [FAIL] [")
            .append(packetError.getCondition().toString())
            .append("]")
            .append(Separator.SystemNewLine)
            .append(Separator.Tab)
            .append("[")
            .append(packetError.getType())
            .append("]")
            .append(Separator.SystemNewLine)
            .append(Separator.Tab)
            .append("[")
            .append(packetError.getText())
            .append("]")
            .toString();
    }

    protected static Boolean didFail(final IQ iq) { return iq.getType() == IQ.Type.error; }

    /**
     * Create ControllerTestCase.
     *
     * @param name
     *      The test name.
     */
    protected ControllerTestCase(final String name) { super(name); }
}
