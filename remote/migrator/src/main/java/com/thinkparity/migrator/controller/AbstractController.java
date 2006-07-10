/*
 * Created On: May 10, 2006 2:38:59 PM
 * $Id$
 */
package com.thinkparity.migrator.controller;

import java.util.LinkedList;
import java.util.List;

import org.xmpp.packet.IQ;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.Release;
import com.thinkparity.migrator.model.library.LibraryModel;
import com.thinkparity.migrator.model.release.ReleaseModel;
import com.thinkparity.migrator.xmpp.IQReader;
import com.thinkparity.migrator.xmpp.IQWriter;

/**
 * An abstraction of the use-case controller from the MVC paradigm. The IQ
 * controller is a stateless class that handles actions from the IQDispatcher.
 * All state is convened in the IQ as well as the Session.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class AbstractController extends
        com.thinkparity.codebase.controller.AbstractController {

    /** The migrator's custom iq reader. */
    private IQReader iqReader;

	/** The migrator's custom iq writer. */
    private IQWriter iqWriter;

    /**
     * Create AbstractController.
     * 
     * @param action
     *            The action the controller will handle.
     */
	protected AbstractController(final String action) { super(action); }

    /**
     * @see com.thinkparity.codebase.controller.AbstractController#createReader(org.xmpp.packet.IQ)
     * 
     */
    public com.thinkparity.codebase.xmpp.IQReader createReader(final IQ iq) {
        iqReader = new IQReader(iq);
        return iqReader;
    }

    /**
     * @see com.thinkparity.codebase.controller.AbstractController#createWriter(org.xmpp.packet.IQ)
     * 
     */
    public com.thinkparity.codebase.xmpp.IQWriter createWriter(final IQ iq) {
        iqWriter = new IQWriter(iq);
        return iqWriter;
    }

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
     * Write releases to the response query.
     * 
     * @param parentName
     *            The parent element name.
     * @param name
     *            The element name.
     * @param releases
     *            The element value.
     */
    protected void writeReleases(final String parentName,
            final String name, final List<Release> releases) {
        iqWriter.writeReleases(parentName, name, releases);
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
}
