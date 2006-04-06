/*
 * Apr 5, 2006
 */
package com.thinkparity.server.org.xmpp.packet.document;

import java.util.UUID;

import org.dom4j.Element;

import com.thinkparity.server.org.dom4j.ElementBuilder;
import com.thinkparity.server.org.dom4j.ElementName;
import com.thinkparity.server.org.dom4j.NamespaceName;
import com.thinkparity.server.org.xmpp.packet.IQParity;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class IQDocument extends IQParity {

    /**
     * The root xml element.
     * 
     */
    protected final Element rootElement;

    /**
     * Create an IQDocument.
     * 
     */
    protected IQDocument(final Action action, final NamespaceName namespaceName) {
        super(action);

        this.rootElement = setChildElement(
                ElementName.QUERY.getName(),
                namespaceName.getName());
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
     * Set the document unique id.
     * 
     * @param uniqueId
     *            The document unique id.
     */
    public void setUniqueId(final UUID uniqueId) {
        ElementBuilder.addElement(rootElement, ElementName.UUID, uniqueId);
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
