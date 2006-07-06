/*
 * Created On: May 10, 2006 2:38:59 PM
 * $Id$
 */
package com.thinkparity.codebase.controller;

import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

import org.jivesoftware.messenger.IQHandlerInfo;
import org.jivesoftware.messenger.auth.UnauthorizedException;

import org.xmpp.packet.IQ;
import org.xmpp.packet.PacketError;

import com.thinkparity.codebase.StringUtil;
import com.thinkparity.codebase.Constants.Xml;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.xmpp.IQReader;
import com.thinkparity.codebase.xmpp.IQWriter;

/**
 * An abstraction of the use-case controller from the MVC paradigm. The IQ
 * controller is a stateless class that handles actions from the IQDispatcher.
 * All state is convened in the IQ as well as the Session.
 * 
 * @author raykroeker@gmail.com
 * @version $Revision$
 */
public abstract class AbstractController extends
		org.jivesoftware.messenger.handler.IQHandler {

    /** An apache logger. */
	protected final Logger logger;

	/** The info. */
	private final IQHandlerInfo info;

	/** An iq reader. */
    private IQReader iqReader;

    /** An iq writer. */
    private IQWriter iqWriter;

	/**
     * Create a AbstractController.
     * 
     * @param action
     *            The action the controller will handle.
     */
	protected AbstractController(final String action) {
		super(action);
		this.info = new IQHandlerInfo(
                Xml.NAME,
                Xml.NAMESPACE + ":" + action);
        this.logger = Logger.getLogger(getClass());
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

    /** @see org.jivesoftware.messenger.handler.IQHandler#getInfo() */
	public IQHandlerInfo getInfo() { return info; }

    /** @see org.jivesoftware.messenger.handler.IQHandler#handleIQ(org.xmpp.packet.IQ) */
    public IQ handleIQ(final IQ iq) throws UnauthorizedException {
        logger.info("[RMIGRATOR] [CONTROLLER] [HANDLE IQ]");
        logger.debug(iq);
        iqReader = createReader(iq);

        final IQ response = createResponse(iq);
        iqWriter = createWriter(response);

        try { service(); }
        catch(final Throwable t) {
            logger.error("[RMIGRATOR] [CONTROLLER] [UNKNOWN ERROR]", t);
            return createErrorResponse(iq, t);
        }

        logger.debug(response);
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
     * Read a byte array parameter.
     * 
     * @param name
     *            The parameter name.
     * @return The byte array parameter value.
     */
    protected final byte[] readByteArray(final String name) {
        return iqReader.readByteArray(name);
    }

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
     * Write a byte array to the response query.
     * 
     * @param name
     *            The element name.
     * @param value
     *            The element value.
     */
    protected final void writeBytes(final String name, final byte[] value) {
        iqWriter.writeBytes(name, value);
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
