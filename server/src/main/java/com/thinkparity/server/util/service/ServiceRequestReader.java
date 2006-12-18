/*
 * Created On:  16-Dec-06 3:16:12 PM
 */
package com.thinkparity.desdemona.util.service;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.ArtifactType;
import com.thinkparity.codebase.model.profile.ProfileVCard;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface ServiceRequestReader {

    public ArtifactType readArtifactType(final String name);

    public Calendar readCalendar(final String name);

    public EMail readEMail(final String name);

    /**
     * Read an integer parameter from the internet query.
     * 
     * @param name
     *            The parameter name.
     * @return An integer value.
     */
    public Integer readInteger(final String name);

    /**
     * Read jabber id data.
     * 
     * @param name
     *            The element name.
     * @return The data; or null if the data does not exist.
     */
    public JabberId readJabberId(final String name);

    /**
     * Read a list of jabber ids.
     * 
     * @param parentName
     *            The parent parameter name.
     * @param name
     *            The parameter name.
     * @return A list of jabber ids.
     */
    public List<JabberId> readJabberIds(final String parentName,
            final String name);

    /**
     * Read long data.
     *
     * @param name
     *      The element name.
     * @return The data; or null if the data does not exist.
     */
    public Long readLong(final String name);

    /**
     * Read long data.
     *
     * @param parentName
     *      The parent element name.
     * @param name
     *      The element name.
     * @return The data; or null if the data does not exist.
     */
    public List<Long> readLongs(final String parentName, final String name);

    /**
     * Read the variable names within the service request.
     * 
     * @return A list of the variable names.
     */
    public List<String> readNames();

    public ProfileVCard readProfileVCard(final String name);

    /**
     * Read string data.
     *
     * @param name
     *      The element name.
     * @return The data; or null if the data does not exist.
     */
    public String readString(final String name);

    /**
     * Read a unique id.
     * 
     * @param name
     *            The element name.
     * @return The element value.
     */
    public UUID readUUID(final String name);
}
