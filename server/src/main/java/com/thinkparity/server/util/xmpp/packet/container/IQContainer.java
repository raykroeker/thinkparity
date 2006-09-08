/*
 * Created On: Jul 6, 2006 10:08:20 AM
 */
package com.thinkparity.desdemona.util.xmpp.packet.container;

import com.thinkparity.desdemona.util.dom4j.ElementBuilder;
import com.thinkparity.desdemona.util.dom4j.ElementName;
import com.thinkparity.desdemona.util.dom4j.NamespaceName;
import com.thinkparity.desdemona.util.xmpp.packet.IQParity2;
import com.thinkparity.desdemona.util.xmpp.packet.IQParity.Action;

/**
 * @author raymond@thinkparity.com
 * @version
 */
abstract class IQContainer extends IQParity2 {

    /**
     * Create IQContainer.
     * 
     * @param action
     *            An enumerable action.
     * @param namespaceName
     *            A namespace name.
     */
    public IQContainer(final Action action, final NamespaceName namespaceName) {
        super(action, namespaceName);
    }

    /**
     * Set the document name.
     * 
     * @param name
     *            The document name.
     */
    public void setName(final String name) {
        ElementBuilder.addElement(rootElement, ElementName.NAME, name);
    }

    /**
     * Set the document version id.
     * 
     * @param versionId
     *            The document version id.
     */
    public void setVersionId(final Long versionId) {
        ElementBuilder.addElement(rootElement, ElementName.VERSIONID, versionId);
    }
}
