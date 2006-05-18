/*
 * Created On: May 10, 2006 2:38:59 PM
 * $Id$
 */
package com.thinkparity.migrator.controller;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jivesoftware.messenger.IQHandlerInfo;
import org.jivesoftware.messenger.auth.UnauthorizedException;
import org.xmpp.packet.IQ;
import org.xmpp.packet.PacketError;

import com.thinkparity.codebase.StringUtil;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.LoggerFactory;
import com.thinkparity.migrator.Constants.Xml;
import com.thinkparity.migrator.model.library.LibraryModel;
import com.thinkparity.migrator.model.release.ReleaseModel;
import com.thinkparity.migrator.util.IQReader;
import com.thinkparity.migrator.util.IQWriter;

/**
 * An abstraction of the use-case controller from the MVC paradigm. The IQ
 * controller is a stateless class that handles actions from the IQDispatcher.
 * All state is convened in the IQ as well as the Session.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
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
	 */
	protected AbstractController(final String action) {
		super(action);
		this.info = new IQHandlerInfo(
                Xml.NAME,
                Xml.NAMESPACE + ":" + action);
        this.logger = LoggerFactory.getLogger(getClass());
	}

    /** @see org.jivesoftware.messenger.handler.IQHandler#getInfo() */
	public IQHandlerInfo getInfo() { return info; }

    /** @see org.jivesoftware.messenger.handler.IQHandler#handleIQ(org.xmpp.packet.IQ) */
    public IQ handleIQ(final IQ iq) throws UnauthorizedException {
        logger.info("[RMIGRATOR] [CONTROLLER] [HANDLE IQ]");
        logger.debug(iq.toXML());
        iqReader = new IQReader(iq);

//        final IQ response = createResponse(iq);
//        iqWriter = new IQWriter(response);
        
        final IQ response = IQ.createResultIQ(iq);
        response.setChildElement(info.getName(), Xml.RESPONSE_NAMESPACE);
        iqWriter = new IQWriter(response);

        try { service(); }
        catch(final Throwable t) {
            logger.error("[RMIGRATOR] [CONTROLLER] [UNKNOWN ERROR]", t);
            return createErrorResponse(iq, t);
        }

        logger.debug(response.toXML());
        return response;
    }

    /** Handle an internet query. */
    public abstract void service();

    /**
     * Obtain the parity library interface.
     * 
     * @param clasz
     *            The caller.
     * @return The parity library interface.
     */
    protected LibraryModel getLibraryModel(final Class clasz) {
        return LibraryModel.getModel();
    }

    /**
     * Obtain the parity release interface.
     * 
     * @param clasz
     *            The caller.
     * @return The parity release interface.
     */
    protected ReleaseModel getReleaseModel(final Class clasz) {
        return ReleaseModel.getModel();
    }

    /**
     * Read a byte array parameter.
     * 
     * @param name
     *            The parameter name.
     * @return The byte array parameter value.
     */
    protected Byte[] readByteArray(final String name) {
        return iqReader.readByteArray(name);
    }

    /**
     * Read a library ids parameter.
     * 
     * @param name
     *            The parameter name.
     * @return The library list value.
     */
    protected List<Library> readLibraryIds(final String parentName,
            final String name) {
        final List<Library> libraries = new LinkedList<Library>();
        final List<Long> libraryIds = iqReader.readLongs(parentName, name);
        for(final Long libraryId : libraryIds) {
            libraries.add(getLibraryModel(getClass()).read(libraryId));
        }
        return libraries;
    }

    /**
     * Read a library type parameter.
     * 
     * @param name
     *            The parameter name.
     * @return The library type value.
     */
    protected Library.Type readLibraryType(final String name) {
        return iqReader.readLibraryType(name);
    }

    /**
     * Read a long parameter.
     * 
     * @param name
     *            The parameter name.
     * @return The long value.
     */
    protected Long readLong(final String name) {
        return iqReader.readLong(name);
    }

    /**
     * Read a string parameter.
     * 
     * @param name
     *            The parameter name.
     * @return The string value.
     */
    protected String readString(final String name) {
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
    protected void writeBytes(final String name, final Byte[] value) {
        iqWriter.writeBytes(name, value);
    }

    /**
     * Write libraries to the response query.
     * 
     * @param parentName
     *            The parent element name.
     * @param name
     *            The element name.
     * @param libraries
     *            The element value.
     */
    protected void writeLibraries(final String parentName, final String name,
            final List<Library> libraries) {
        iqWriter.writeLibraries(parentName, name, libraries);
    }

    /**
     * Write a library type value to the response query.
     * 
     * @param name
     *            The element name.
     * @param libraryType
     *            The element value.
     */
    protected void writeLibraryType(final String name,
            final Library.Type libraryType) {
        iqWriter.writeLibraryType(name, libraryType);
    }

    /**
     * Write a long value to the response query.
     * 
     * @param name
     *      The element name.
     * @param value
     *      The element value.
     */
    protected void writeLong(final String name, final Long value) {
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
    protected void writeString(final String name, final String value) {
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

    /**
     * Create an iq.
     * 
     * @param type
     *            The query type.
     * @param packetId
     *            The request query id.
     * @return A response query.
     */
    private IQ createIQ(final IQ iq) {
        final IQ resultIQ = IQ.createResultIQ(iq);
        resultIQ.setChildElement(info.getName(), info.getNamespace());
        return resultIQ;
    }

    /**
     * Create a response for the query.
     * @param iq The internet query.
     * @return The response.
     */
    private IQ createResponse(final IQ iq) {
        final IQ response = createIQ(iq);
        return response;
    }
}
