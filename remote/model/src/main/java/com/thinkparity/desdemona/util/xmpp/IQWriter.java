/*
 * Created On: Jun 22, 2006 2:55:32 PM
 * $Id$
 */
package com.thinkparity.desdemona.util.xmpp;

import java.util.List;

import org.dom4j.Branch;
import org.xmpp.packet.IQ;

import com.thinkparity.codebase.model.artifact.ArtifactType;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.profile.ProfileEMail;

import com.thoughtworks.xstream.XStream;

/**
 * <b>Title:</b>thinkParity Model IQ Writer <br>
 * <b>Description:</b>A custom xmpp internet query writer for the model.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class IQWriter extends com.thinkparity.codebase.xmpp.IQWriter {

    /** An xstream xml serializer. */
    private final XStream xstream;

    /** Create IQWriter. */
    public IQWriter(final IQ iq) {
        super(iq);
        this.xstream = new XStream();
    }

    /**
     * Marshal a list of containers to the internet query.
     * 
     * @param containers
     *            A list of containers.
     */
    public final void marshalContainers(final List<Container> containers) {
        marshal(containers);
    }

    /**
     * Marshal a list of containers to the internet query.
     * 
     * @param containers
     *            A list of containers.
     */
    public final void marshalContainerVersions(
            final List<ContainerVersion> versions) {
        marshal(versions);
    }

    /**
     * Marshal a list of documents to the xml response.
     * 
     * @param documents
     *            A <code>List&lt;Document&gt;</code>.
     */
    public final void marshalDocuments(final List<Document> documents) {
        marshal(documents);
    }


    /**
     * Marshal a list of document versions to the xml response.
     * 
     * @param versions
     *            A <code>List&lt;DocumentVersion&gt;</code>.
     */
    public final void marshalDocumentVersions(
            final List<DocumentVersion> versions) {
        marshal(versions);
    }

    /**
     * Write the artifact type to the internet query.
     * 
     * @param name
     *            The element name.
     * @param value
     *            The element value.
     */
    public final void writeArtifactType(final String name,
            final ArtifactType value) {
        ElementBuilder.addElement(iq.getChildElement(), name, value);
    }

    /**
     * Write the artifact type to the internet query.
     * 
     * @param name
     *            The element name.
     * @param value
     *            The element value.
     */
    public final void writeProfileEMails(final String name,
            final List<ProfileEMail> value) {
        ElementBuilder.addProfileEMailElements(iq.getChildElement(), name,
                name, value);
    }

    /**
     * Marshal an object to the internet query using the xstream object
     * serialization library.
     * 
     * @param object
     *            An object.
     */
    private final void marshal(final Object object) {
        final Dom4JWriter writer = new Dom4JWriter(iq.getChildElement());
        xstream.marshal(object, writer);
    }

    private class Dom4JWriter extends com.thoughtworks.xstream.io.xml.Dom4JWriter {

        private Boolean firstNode;

        /** Create Dom4JWriter. */
        public Dom4JWriter(final Branch branch) {
            super(branch);
            this.firstNode = Boolean.TRUE;
        }

        /**
         * @see com.thoughtworks.xstream.io.xml.Dom4JWriter#startNode(java.lang.String)
         */
        @Override
        public void startNode(final String name) {
            super.startNode(name);
            if (firstNode) {
                addAttribute("javaType", xstream.getClass().getName());
                firstNode = Boolean.FALSE;
            }
        }
    }
}
