/*
 * Created On:  16-Jul-07 9:28:03 AM
 */
package com.thinkparity.ophelia.model;

import java.util.List;

import com.thinkparity.codebase.model.artifact.ArtifactVersion;

/**
 * <b>Title:</b>thinkParity Ophelia Model Local Content Event<br>
 * <b>Description:</b>Defines an event-wrapper interface for an event that
 * contains content which can be pre-processed (ie downloaded) before being
 * passed on to the event handler.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface LocalContentEvent<T extends ArtifactVersion, U extends Object> {

    /**
     * Obtain all of the local content.
     * 
     * @return A <code>List<U></code>.
     */
    public List<U> getAllLocalContent();

    /**
     * Obtain the local content for the version.
     * 
     * @param version
     *            A <code>T</code>.
     * @return The local content <code>U</code>.
     */
    public U getLocalContent(final T version);

    /**
     * Obtain the versions.
     * 
     * @return A <code>List<T></code>.
     */
    public List<T> getVersions();

    /**
     * Set the local content.
     * 
     * @param version
     *            A <code>T</code>.
     * @param content
     *            The local content <code>U</code>.
     */
    public void setLocalContent(final T version, final U content);
}
