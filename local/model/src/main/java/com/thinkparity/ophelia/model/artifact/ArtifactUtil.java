/*
 * Nov 9, 2005
 */
package com.thinkparity.ophelia.model.artifact;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.List;

import com.thinkparity.codebase.FuzzyDateFormat;

import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.artifact.ArtifactVersion;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.user.User;

/**
 * <b>Title:</b>thinkParity OpheliaModel Artifact Util<br>
 * <b>Description:</b>The parity object helper provides common utility
 * functionality for artifacts.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ArtifactUtil {

    /** An instance of <code>ArtifactUtil</code>. */
	private static ArtifactUtil INSTANCE;

    /** A <code>FuzzyDateFormat</code>. */
    private final FuzzyDateFormat fuzzyDateFormat;

	/**
     * Obtain an artifact util.
     * 
     * @return An instance of <code>ArtifactUtil</code>.
     */
    public static ArtifactUtil getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new ArtifactUtil();
        }
        return INSTANCE;
    }

	/**
     * Create ArtifactUtil.
     *
	 */
	private ArtifactUtil() {
        super();
        fuzzyDateFormat = new FuzzyDateFormat();
	}

    /**
     * Determine whether or not the list of artifacts contains an artifact
     * version.
     * 
     * @param <T>
     *            An artifact type.
     * @param <U>
     *            An artifact version type.
     * @param list
     *            A <code>List</code> of <code>T</code>.
     * @param element
     *            An <code>U</code>.
     * @return True if the list contains the element.
     */
    public <T extends Artifact, U extends ArtifactVersion> boolean contains(
            final List<T> list, final U element) {
        return -1 < indexOf(list, element);
    }

	/**
     * Get the version name.
     * 
     * @param version
     *            A <code>ContainerVersion</code>.
     * @param publishedBy
     *            A published by <code>User</code>.
     * @return A version name <code>String</code>.
     */
    public String getVersionName(final ContainerVersion version,
            final User publishedBy) {
        if (version.isSetName()) {
            return version.getName();
        } else {
            return MessageFormat.format("{0}, {1}", format(version
                    .getUpdatedOn()), publishedBy.getName());
        }
    }

    /**
     * Format a calendar as a string.
     * 
     * @param calendar
     *            A <code>Calendar</code>.
     * @return A formatted <code>String</code>.
     */
    private String format(final Calendar calendar) {
        return fuzzyDateFormat.format(calendar, "Long");
    }

    /**
     * Determine the index of an artifact version within an artifact list. A
     * check is made for the artifact unique id, the artifact type and the
     * artifact id.
     * 
     * @param <T>
     *            The artifact list type.
     * @param <U>
     *            The artifact version type.
     * @param list
     *            A <code>List</code> of <code>T</code>s.
     * @param element
     *            A <code>U</code>.
     * @return The index of the element in the list; or -1 if no such element
     *         exists.
     */
	private <T extends Artifact, U extends ArtifactVersion> int indexOf(
            final List<T> list, final U element) {
	    for (int i = 0; i < list.size(); i++) {
	        if (list.get(i).getUniqueId().equals(element.getArtifactUniqueId())
                    && list.get(i).getType().equals(element.getArtifactType())
                    && list.get(i).getId().equals(element.getArtifactId()))
	            return i;
	    }
	    return -1;
	}
}
