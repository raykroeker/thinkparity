/*
 * Created On: Thu May 11 2006 11:41 PDT
 * $Id$
 */
package com.thinkparity.codebase.xmpp;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.stream.StreamSession;
import com.thinkparity.codebase.model.user.Token;

import org.xmpp.packet.IQ;


/**
 * <b>Title:</b>thinkParity IQ Writer<br>
 * <b>Description:</b>A jive server iq writer.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public abstract class IQWriter {

    /** The xmpp internet query this data writer is backing. */
    protected final IQ iq;

    /** Create IQDataWriter. */
    public IQWriter(final IQ iq) {
        super();
        this.iq = iq;
    }

    /**
     * Obtain the internet query.
     *
     * @return The internet query.
     */
    public final IQ getIQ() { return iq; }

    /**
     * Write a calendar value.
     *
     * @param name
     *      The element name.
     * @param value
     *      The element value.
     */
    public final void writeCalendar(final String name, final Calendar value) {
        ElementBuilder.addElement(iq.getChildElement(), name, value);
    }

    public final void writeContainer(final String name, final Container value) {
        ElementBuilder.addElement(iq.getChildElement(), name, value);
    }
    public final void writeContainers(final String parentName,
            final String name, final List<Container> values) {
        ElementBuilder.addContainerElements(iq.getChildElement(), parentName, name, values);
    }
    public final void writeContainerVersions(final String parentName,
            final String name, final List<ContainerVersion> values) {
        ElementBuilder.addContainerVersionElements(iq.getChildElement(), parentName, name, values);
    }

    public final void writeDocuments(final String parentName,
            final String name, final List<Document> values) {
        ElementBuilder.addDocumentElements(iq.getChildElement(), parentName, name, values);
    }

    public final void writeDocumentVersions(final String parentName,
            final String name, final List<DocumentVersion> values) {
        ElementBuilder.addDocumentVersionElements(iq.getChildElement(), parentName, name, values);
    }

    public final void writeEMail(final String name, final EMail value) {
        ElementBuilder.addElement(iq.getChildElement(), name, value);
    }

    /**
     * Write email values.
     * 
     * @param parentName
     *            The parent element name.
     * @param name
     *            The element name.
     * @param values
     *            The element values.
     */
    public final void writeEMails(final String parentName, final String name,
            final List<EMail> values) {
        ElementBuilder.addEMailElements(iq.getChildElement(), parentName, name, values);
    }

    /**
     * Write an integer value.
     * 
     * @param name
     *            The element name.
     * @param value
     *            The element value.
     */
    public final void writeInteger(final String name, final Integer value) {
        ElementBuilder.addElement(iq.getChildElement(), name, value);
    }

    /**
     * Write a jabber id value.
     * 
     * @param name
     *            The element name.
     * @param value
     *            The element value.
     */
    public final void writeJabberId(final String name, final JabberId value) {
        ElementBuilder.addElement(iq.getChildElement(), name, value);
    }

    /**
     * Write a list of jabber id values.
     * 
     * @param parentName
     *            The parent element name.
     * @param name
     *            The element name.
     * @param value
     *            The element value.
     */
    public final void writeJabberIds(final String parentName,
            final String name, final List<JabberId> values) {
        ElementBuilder.addJabberIdElements(iq.getChildElement(), parentName, name, values);
    }

    /**
     * Write a long value.
     * 
     * @param name
     *      The element name.
     * @param value
     *      The element value.
     */
    public final void writeLong(final String name, final Long value) {
        ElementBuilder.addElement(iq.getChildElement(), name, value);
    }

    /**
     * Write a list of long values.
     * 
     * @param name
     *            The element name.
     * @param value
     *            The element values.
     */
    public final void writeLongs(final String parentName, final String name,
            final List<Long> values) {
        ElementBuilder.addLongElements(iq.getChildElement(), parentName, name, values);
    }

    public final void writeStreamSession(final String name, final StreamSession value) {
        ElementBuilder.addElement(iq.getChildElement(), name, value);
    }

    /**
     * Write a string value.
     *
     * @param name
     *      The element name.
     * @param value
     *      The element value.
     */
    public final void writeString(final String name, final String value) {
        ElementBuilder.addElement(iq.getChildElement(), name, value);
    }

    /**
     * Write string values.
     * 
     * @param parentName
     *            The parent element name.
     * @param name
     *            The element name.
     * @param values
     *            The element values.
     */
    public final void writeStrings(final String parentName, final String name,
            final List<String> values) {
        ElementBuilder.addStringElements(iq.getChildElement(), parentName, name, values);
    }

    public final void writeToken(final String name, final Token value) {
        ElementBuilder.addElement(iq.getChildElement(), name, value);
    }

    /**
     * Write unique id value.
     * 
     * @param name
     *            The element name.
     * @param value
     *            The element value.
     */
    public final void writeUniqueId(final String name, final UUID value) {
        ElementBuilder.addElement(iq.getChildElement(), name, value);
    }
}
