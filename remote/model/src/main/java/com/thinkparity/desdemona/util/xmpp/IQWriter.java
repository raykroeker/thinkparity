/*
 * Created On: Jun 22, 2006 2:55:32 PM
 * $Id$
 */
package com.thinkparity.desdemona.util.xmpp;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.migrator.Resource;
import com.thinkparity.codebase.model.stream.StreamSession;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.Token;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.user.UserVCard;
import com.thinkparity.codebase.model.util.dom4j.ElementBuilder;
import com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent;
import com.thinkparity.codebase.model.util.xstream.XStreamUtil;

import com.thinkparity.desdemona.util.service.ServiceResponseWriter;

import org.dom4j.Element;
import org.xmpp.packet.IQ;

import com.thoughtworks.xstream.io.xml.Dom4JWriter;

/**
 * <b>Title:</b>thinkParity Model IQ Writer <br>
 * <b>Description:</b>A custom xmpp internet query writer for the model.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public final class IQWriter implements ServiceResponseWriter {

    /** An apache iq IQ_LOGGER. */
    private static final Log4JWrapper IQ_LOGGER;

    /** XStream serialization. */
    private static final XStreamUtil XSTREAM_UTIL;

    static {
        IQ_LOGGER = new Log4JWrapper("DESDEMONA_XMPP_DEBUGGER");
        XSTREAM_UTIL = XStreamUtil.getInstance();
    }

    /** The xmpp internet query this data writer is backing. */
    protected final IQ iq;

    /**
     * An apache logger protected final Log4JWrapper logger;
     * 
     * /** Create IQDataWriter.
     * 
     * @param iq
     *            An xmpp internet query <code>IQ</code>.
     */
    public IQWriter(final IQ iq) {
        super();
        this.iq = iq;
    }

    /**
     * Obtain the xmpp interenet query <code>IQ</code>.
     * 
     * @return An xmpp <code>IQ</code>.
     */
    public final IQ getIQ() { return iq; }

    /**
     * @see com.thinkparity.desdemona.util.service.ServiceResponseWriter#write(java.lang.String,
     *      com.thinkparity.codebase.model.migrator.Product)
     * 
     */
    public final void write(final String name, final Product value) {
        ElementBuilder.addElement(XSTREAM_UTIL, iq.getChildElement(), name, value);
    }

    /**
     * @see com.thinkparity.desdemona.util.service.ServiceResponseWriter#write(java.lang.String,
     *      com.thinkparity.codebase.model.migrator.Release)
     * 
     */
    public final void write(final String name, final Release value) {
        ElementBuilder.addElement(XSTREAM_UTIL, iq.getChildElement(), name, value);
    }

    /**
     * @see com.thinkparity.desdemona.util.service.ServiceResponseWriter#writeBoolean(java.lang.String,
     *      java.lang.Boolean)
     * 
     */
    public final void writeBoolean(final String name, final Boolean value) {
        ElementBuilder.addElement(iq.getChildElement(), name, value);
    }

    /**
     * @see com.thinkparity.desdemona.util.service.ServiceResponseWriter#writeCalendar(java.lang.String,
     *      java.util.Calendar)
     * 
     */
    public final void writeCalendar(final String name, final Calendar value) {
        ElementBuilder.addElement(iq.getChildElement(), name, value);
    }

    /**
     * @see com.thinkparity.desdemona.util.service.ServiceResponseWriter#writeContainer(java.lang.String,
     *      com.thinkparity.codebase.model.container.Container)
     * 
     */
    public final void writeContainer(final String name, final Container value) {
        ElementBuilder.addElement(XSTREAM_UTIL, iq.getChildElement(), name, value);
    }

    /**
     * @see com.thinkparity.desdemona.util.service.ServiceResponseWriter#writeContainers(java.lang.String,
     *      java.lang.String, java.util.List)
     * 
     */
    public final void writeContainers(final String parentName,
            final String name, final List<Container> values) {
        ElementBuilder.addContainerElements(XSTREAM_UTIL, iq.getChildElement(), parentName, name, values);
    }

    /**
     * @see com.thinkparity.desdemona.util.service.ServiceResponseWriter#writeContainerVersions(java.lang.String,
     *      java.lang.String, java.util.List)
     * 
     */
    public final void writeContainerVersions(final String parentName,
            final String name, final List<ContainerVersion> values) {
        ElementBuilder.addContainerVersionElements(iq.getChildElement(), parentName, name, values);
    }

    /**
     * @see com.thinkparity.desdemona.util.service.ServiceResponseWriter#writeDocuments(java.lang.String,
     *      java.lang.String, java.util.List)
     * 
     */
    public final void writeDocuments(final String parentName,
            final String name, final List<Document> values) {
        ElementBuilder.addDocumentElements(iq.getChildElement(), parentName, name, values);
    }

    /**
     * @see com.thinkparity.desdemona.util.service.ServiceResponseWriter#writeDocumentVersion(java.lang.String,
     *      com.thinkparity.codebase.model.document.DocumentVersion)
     * 
     */
    public final void writeDocumentVersion(final String name,
            final DocumentVersion value) {
        ElementBuilder.addElement(iq.getChildElement(), name, value);
    }

    /**
     * @see com.thinkparity.desdemona.util.service.ServiceResponseWriter#writeDocumentVersionDeltas(java.lang.String,
     *      java.util.Map)
     * 
     */
    public final void writeDocumentVersionDeltas(final String name, final Map<DocumentVersion, Delta> values) {
        ElementBuilder.addDocumentVersionDeltaElements(iq.getChildElement(),
                name, values);
    }

    /**
     * @see com.thinkparity.desdemona.util.service.ServiceResponseWriter#writeDocumentVersions(java.lang.String,
     *      java.lang.String, java.util.List)
     * 
     */
    public final void writeDocumentVersions(final String parentName,
            final String name, final List<DocumentVersion> values) {
        ElementBuilder.addDocumentVersionElements(iq.getChildElement(), parentName, name, values);
    }

    /**
     * @see com.thinkparity.desdemona.util.service.ServiceResponseWriter#writeEMail(java.lang.String,
     *      com.thinkparity.codebase.email.EMail)
     * 
     */
    public final void writeEMail(final String name, final EMail value) {
        ElementBuilder.addElement(iq.getChildElement(), name, value);
    }

    /**
     * @see com.thinkparity.desdemona.util.service.ServiceResponseWriter#writeEMails(java.lang.String,
     *      java.lang.String, java.util.List)
     * 
     */
    public final void writeEMails(final String parentName, final String name,
            final List<EMail> values) {
        ElementBuilder.addEMailElements(iq.getChildElement(), parentName, name, values);
    }

    /**
     * @see com.thinkparity.desdemona.util.service.ServiceResponseWriter#writeEvents(java.lang.String,
     *      java.lang.String, java.util.List)
     * 
     */
    public final void writeEvents(final String name, final String childName,
            final List<XMPPEvent> values) {
        final Element parent = iq.getChildElement();
        if (values.size() < 1) {
            ElementBuilder.addNullElement(parent, name, List.class);
        } else {
            final Element element = ElementBuilder.addElement(parent, name, List.class);
            for (final XMPPEvent value : values) {
                final Element childElement = ElementBuilder.addElement(element, childName, value.getClass());
                final Dom4JWriter writer = new Dom4JWriter(childElement);
                XSTREAM_UTIL.marshal(value, writer);
            }
        }
    }

    /**
     * @see com.thinkparity.desdemona.util.service.ServiceResponseWriter#writeJabberId(java.lang.String,
     *      com.thinkparity.codebase.jabber.JabberId)
     * 
     */
    public final void writeJabberId(final String name, final JabberId value) {
        ElementBuilder.addElement(iq.getChildElement(), name, value);
    }

    /**
     * @see com.thinkparity.desdemona.util.service.ServiceResponseWriter#writeJabberIds(java.lang.String,
     *      java.lang.String, java.util.List)
     * 
     */
    public final void writeJabberIds(final String parentName,
            final String name, final List<JabberId> values) {
        ElementBuilder.addJabberIdElements(iq.getChildElement(), parentName, name, values);
    }

    /**
     * @see com.thinkparity.desdemona.util.service.ServiceResponseWriter#writeLongs(java.lang.String,
     *      java.lang.String, java.util.List)
     * 
     */
    public final void writeLongs(final String parentName, final String name,
            final List<Long> values) {
        ElementBuilder.addLongElements(iq.getChildElement(), parentName, name, values);
    }

    /**
     * @see com.thinkparity.desdemona.util.service.ServiceResponseWriter#writeResources(java.lang.String,
     *      java.util.List)
     * 
     */
    public void writeResources(final String name, final List<Resource> values) {
        ElementBuilder.addResourceElements(XSTREAM_UTIL, iq.getChildElement(), name, values);
    }

    /**
     * @see com.thinkparity.desdemona.util.service.ServiceResponseWriter#writeStreamSession(java.lang.String,
     *      com.thinkparity.codebase.model.stream.StreamSession)
     * 
     */
    public final void writeStreamSession(final String name, final StreamSession value) {
        ElementBuilder.addElement(iq.getChildElement(), name, value);
    }

    /**
     * @see com.thinkparity.desdemona.util.service.ServiceResponseWriter#writeString(java.lang.String,
     *      java.lang.String)
     * 
     */
    public final void writeString(final String name, final String value) {
        ElementBuilder.addElement(iq.getChildElement(), name, value);
    }

    /**
     * @see com.thinkparity.desdemona.util.service.ServiceResponseWriter#writeTeam(java.lang.String,
     *      java.lang.String, java.util.List)
     * 
     */
    public final void writeTeam(final String name, final String childName, final List<TeamMember> values) {
        final Element parent = iq.getChildElement();
        IQ_LOGGER.logVariable("parent", parent.asXML());
        if (values.size() < 1) {
            ElementBuilder.addNullElement(parent, name, List.class);
        } else {
            final Element element = ElementBuilder.addElement(parent, name, List.class);
            for (final TeamMember value : values) {
                final Element childElement = ElementBuilder.addElement(element, childName, value.getClass());
                final Dom4JWriter writer = new Dom4JWriter(childElement);
                XSTREAM_UTIL.marshal(value, writer);
            }
        }
        IQ_LOGGER.logVariable("parent", parent.asXML());
    }

    /**
     * @see com.thinkparity.desdemona.util.service.ServiceResponseWriter#writeToken(java.lang.String,
     *      com.thinkparity.codebase.model.user.Token)
     * 
     */
    public final void writeToken(final String name, final Token value) {
        ElementBuilder.addElement(iq.getChildElement(), name, value);
    }

    /**
     * @see com.thinkparity.desdemona.util.service.ServiceResponseWriter#writeUniqueId(java.lang.String,
     *      java.util.UUID)
     * 
     */
    public final void writeUniqueId(final String name, final UUID value) {
        ElementBuilder.addElement(iq.getChildElement(), name, value);
    }

    /**
     * @see com.thinkparity.desdemona.util.service.ServiceResponseWriter#writeUserReceipts(java.lang.String,
     *      java.util.Map)
     * 
     */
    public final void writeUserReceipts(final String name,
            final Map<User, ArtifactReceipt> values) {
        ElementBuilder.addUserReceiptElements(XSTREAM_UTIL, iq.getChildElement(), name, values);
    }

    /**
     * @see com.thinkparity.desdemona.util.service.ServiceResponseWriter#writeVCard(java.lang.String,
     *      com.thinkparity.codebase.model.user.UserVCard)
     * 
     */
    public void writeVCard(final String name, final UserVCard value) {
        ElementBuilder.addVCardElement(XSTREAM_UTIL, iq.getChildElement(),
                name, value);
    }
}
