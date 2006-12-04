/*
 * Created On: May 10, 2006 2:38:59 PM
 */
package com.thinkparity.codebase.wildfire.handler;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.thinkparity.codebase.StringUtil;
import com.thinkparity.codebase.Constants.Xml;
import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.xmpp.IQReader;
import com.thinkparity.codebase.xmpp.IQWriter;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.stream.StreamSession;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.Token;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent;

import org.apache.log4j.Logger;
import org.jivesoftware.wildfire.IQHandlerInfo;
import org.jivesoftware.wildfire.auth.UnauthorizedException;
import org.xmpp.packet.IQ;
import org.xmpp.packet.PacketError;

/**
 * An abstraction of the use-case controller from the MVC paradigm. The IQ
 * controller is a stateless class that handles actions from the IQDispatcher.
 * All state is convened in the IQ as well as the Session.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.8
 */
public abstract class AbstractHandler extends
		org.jivesoftware.wildfire.handler.IQHandler {

	/** The info. */
	private final IQHandlerInfo info;

	/** An iq reader. */
    private IQReader iqReader;

    /** An iq writer. */
    private IQWriter iqWriter;

	/**
     * Create AbstractHandler.
     * 
     * @param action
     *            The action the controller will handle.
     */
	protected AbstractHandler(final String action) {
		super(action);
		this.info = new IQHandlerInfo(
                Xml.NAME,
                Xml.NAMESPACE + ":" + action);
	}

    /**
     * Create an iq reader.
     * 
     * @param iq
     *            The internet query.
     * @return The internet query reader.
     */
    public abstract IQReader createReader(final IQ iq);

    /**
     * Create an iq writer.
     * 
     * @param iq
     *            The internet query.
     * @return The internet query writer.
     */
    public abstract IQWriter createWriter(final IQ iq);

    /**
     * @see org.jivesoftware.wildfire.handler.IQHandler#getInfo()
     * 
     */
	public IQHandlerInfo getInfo() { return info; }

	/**
     * @see org.jivesoftware.wildfire.handler.IQHandler#handleIQ(org.xmpp.packet.IQ)
     * 
	 */
    public IQ handleIQ(final IQ iq) throws UnauthorizedException {
        iqReader = createReader(iq);

        final IQ response = createResponse(iq);
        iqWriter = createWriter(response);

        try {
            service();
        } catch (final Throwable t) {
            Logger.getLogger(getClass()).fatal(getErrorId(t), t);
            return createErrorResponse(iq, t);
        }

        return response;
    }

    /** Handle an internet query. */
    public abstract void service();

    /**
     * Create a response for the query.
     * @param iq The internet query.
     * @return The response.
     */
    protected IQ createResponse(final IQ iq) {
        final IQ response = IQ.createResultIQ(iq);
        response.setChildElement(info.getName(), Xml.RESPONSE_NAMESPACE);
        return response;
    }

    /**
     * Obtain an error id.
     * 
     * @return An error id.
     */
    protected abstract Object getErrorId(final Throwable t);

    /**
     * Read a calendar parameter.
     * 
     * @param name
     *            The parameter name.
     * @return The calendar.
     */
    protected final Calendar readCalendar(final String name) {
        return iqReader.readCalendar(name);
    }

    protected final EMail readEMail(final String name) {
        return iqReader.readEMail(name);
    }

    /**
     * Read an integer parameter.
     * 
     * @param name
     *            The parameter name.
     * @return An integer value.
     */
    protected final Integer readInteger(final String name) { 
        return iqReader.readInteger(name);
    }

    /**
     * Read a jabber id parameter.
     * 
     * @param name
     *            The parameter name.
     * @return The jabber id.
     */
    protected final JabberId readJabberId(final String name) {
        return iqReader.readJabberId(name);
    }

    /**
     * Read a list of jabber id parameters.
     * 
     * @param parentName
     *            The parent parameter name.
     * @param name
     *            The parameter name.
     * @return A list of jabber ids.
     */
    protected final List<JabberId> readJabberIds(final String parentName,
            final String name) {
        return iqReader.readJabberIds(parentName, name);
    }

    /**
     * Read a long parameter.
     * 
     * @param name
     *            The parameter name.
     * @return The long value.
     */
    protected final Long readLong(final String name) {
        return iqReader.readLong(name);
    }

    /**
     * Read a string parameter.
     * 
     * @param name
     *            The parameter name.
     * @return The string value.
     */
    protected final String readString(final String name) {
        return iqReader.readString(name);
    }

    /**
     * Write a calendar to the response query.
     *
     * @param name
     *      The element name.
     * @param value
     *      The element value.
     */
    protected final void writeCalendar(final String name, final Calendar value) {
        iqWriter.writeCalendar(name, value);
    }

    protected final void writeContainer(final String name, final Container value) {
        iqWriter.writeContainer(name, value);
    }

    protected final void writeContainers(final String parentName,
            final String name, final List<Container> values) {
        iqWriter.writeContainers(parentName, name, values);
    }

    protected final void writeContainerVersions(final String parentName,
            final String name, final List<ContainerVersion> values) {
        iqWriter.writeContainerVersions(parentName, name, values);
    }

    protected final void writeDocuments(final String parentName,
            final String name, final List<Document> values) {
        iqWriter.writeDocuments(parentName, name, values);
    }

    protected final void writeDocumentVersionDeltas(final String name,
            final Map<DocumentVersion, Delta> values) {
        iqWriter.writeDocumentVersionDeltas(name, values);
    }

    protected final void writeUserReceipts(final String name,
            final Map<User, ArtifactReceipt> values) {
        iqWriter.writeUserReceipts(name, values);
    }

    protected final void writeDocumentVersions(final String parentName,
            final String name, final List<DocumentVersion> values) {
        iqWriter.writeDocumentVersions(parentName, name, values);
    }
    /**
     * Write a list of e-mail values to the response query.
     * 
     * @param parentName
     *            The parent element name.
     * @param name
     *            The element name.
     * @param values
     *            The element values.
     */
    protected final void writeEMails(final String parentName,
            final String name, final List<EMail> values) {
        iqWriter.writeEMails(parentName, name, values);
    }

    protected final void writeEvents(final String name, final String childName,
            final List<XMPPEvent> values) {
        iqWriter.writeEvents(name, childName, values);
    }


    /**
     * Write a jabber id value to the response query.
     * 
     * @param name
     *            The element name.
     * @param value
     *            The element value.
     */
    protected final void writeJabberId(final String name, final JabberId value) {
        iqWriter.writeJabberId(name, value);
    }

    /**
     * Write a list of string values to the response query.
     * 
     * @param parentName
     *            The parent element name.
     * @param name
     *            The element name.
     * @param values
     *            The element values.
     */
    protected final void writeJabberIds(final String parentName,
            final String name, final List<JabberId> values) {
        iqWriter.writeJabberIds(parentName, name, values);
    }

    /**
     * Write a long value to the response query.
     * 
     * @param name
     *      The element name.
     * @param value
     *      The element value.
     */
    protected final void writeLong(final String name, final Long value) {
        iqWriter.writeLong(name, value);
    }

    protected final void writeStreamSession(final String name, final StreamSession value) {
        iqWriter.writeStreamSession(name, value);
    }

    /**
     * Write a string value to the response query.
     *
     * @param name
     *      The element name.
     * @param value
     *      The element value.
     */
    protected final void writeString(final String name, final String value) {
        iqWriter.writeString(name, value);
    }

    /**
     * Write a list of string values to the response query.
     * 
     * @param parentName
     *            The parent element name.
     * @param name
     *            The element name.
     * @param values
     *            The element values.
     */
    protected final void writeStrings(final String parentName,
            final String name, final List<String> values) {
        iqWriter.writeStrings(parentName, name, values);
    }

    protected final void writeTeam(final String name, final String childName, final List<TeamMember> values) {
        iqWriter.writeTeam(name, childName, values);
    }

    protected final void writeToken(final String name, final Token value) {
        iqWriter.writeToken(name, value);
    }

    protected final void writeUser(final String name, final User value) {
        iqWriter.writeUser(name, value);
    }

    /**
     * Create an error response for the query, for the error.
     * 
     * @param iq
     *            The internet query.
     * @param x
     *            The error.
     * @return The error response.
     */
    private IQ createErrorResponse(final IQ iq, final Throwable t) {
        final IQ errorResult = IQ.createResultIQ(iq);

        final PacketError packetError = new PacketError(
                PacketError.Condition.internal_server_error,
                PacketError.Type.cancel, StringUtil.printStackTrace(t));

		errorResult.setError(packetError);
        return errorResult;
    }
}
