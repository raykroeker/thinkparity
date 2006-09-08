/*
 * Apr 8, 2006
 */
package com.thinkparity.desdemona.util.xmpp.packet.artifact;

import com.thinkparity.desdemona.util.dom4j.NamespaceName;
import com.thinkparity.desdemona.util.xmpp.packet.IQParity2;
import com.thinkparity.desdemona.util.xmpp.packet.IQParity.Action;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class IQArtifact extends IQParity2 {

    /**
     * Create a IQArtifact.
     * 
     * @param action
     *            The internet query action.
     * @param namespaceName
     *            The internet query namespace.
     */
    public IQArtifact(final Action action, final NamespaceName namespaceName) {
        super(action, namespaceName);
    }
}
