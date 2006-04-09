/*
 * Apr 5, 2006
 */
package com.thinkparity.server.org.xmpp.packet.document;


import com.thinkparity.server.org.dom4j.ElementBuilder;
import com.thinkparity.server.org.dom4j.ElementName;
import com.thinkparity.server.org.dom4j.NamespaceName;
import com.thinkparity.server.org.xmpp.packet.IQParity2;
import com.thinkparity.server.org.xmpp.packet.IQParity.Action;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
abstract class IQDocument extends IQParity2 {

    /**
     * Create an IQDocument.
     * 
     */
    protected IQDocument(final Action action, final NamespaceName namespaceName) {
        super(action, namespaceName);
    }

    /**
     * Set the document byte content.
     * 
     * @param content
     *            The document byte content.
     */
    public void setContent(final byte[] content) {
        ElementBuilder.addElement(rootElement, ElementName.BYTES, content);
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
