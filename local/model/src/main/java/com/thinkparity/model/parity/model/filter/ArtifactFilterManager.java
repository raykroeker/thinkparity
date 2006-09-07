/*
 * Created On: Mar 28, 2006
 * $Id$
 */
package com.thinkparity.model.parity.model.filter;

import java.util.Iterator;
import java.util.List;

import com.thinkparity.model.artifact.Artifact;
import com.thinkparity.model.artifact.ArtifactVersion;

/**
 * <b>Title:</b>thinkParity Model Artifact Filter Manager<br>
 * <b>Description:</b>The artifact filter manager takes ordered lists of
 * artifacts and applies a chain of filters to the list.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class ArtifactFilterManager {

	/** A singleton implementation. */
	private static final ArtifactFilterManager SINGLETON;

	static { SINGLETON = new ArtifactFilterManager(); }

	/**
     * Filter a list of artifacts.
     * 
     * @param list
     *            The list of artifacts.
     * @param filter
     *            The artifact filter.
     */
	public static void filter(final List<? extends Artifact> list,
			final Filter<? super Artifact> filter) {
		SINGLETON.doFilter(list, filter);
	}

    /**
     * Filter a list of artifact versions.
     * 
     * @param list
     *            The list of artifact versions.
     * @param filter
     *            An artifact version filter.
     */
    public static void filterVersions(final List<? extends ArtifactVersion> list,
            final Filter<? super ArtifactVersion> filter) {
        SINGLETON.doFilterVersions(list, filter);
    }

	/** Create ArtifactFilterManager. */
	private ArtifactFilterManager() { super(); }

    /**
     * Filter a list of artifacts.
     * 
     * @param list
     *            The list of artifacts.
     * @param filter
     *            The artifact filter.
     */
    private void doFilter(final List<? extends Artifact> list,
            final Filter<? super Artifact> filter) {
        Artifact artifact;
        for(final Iterator<? extends Artifact> i = list.iterator(); i.hasNext();) {
            artifact = i.next();
            if(filter.doFilter(artifact)) { i.remove(); }
        }
    }

    /**
     * Filter a list of artifacts.
     * 
     * @param list
     *            The list of artifacts.
     * @param filter
     *            The artifact filter.
     */
    private void doFilterVersions(final List<? extends ArtifactVersion> list,
            final Filter<? super ArtifactVersion> filter) {
        ArtifactVersion artifactVersion;
        for(final Iterator<? extends ArtifactVersion> i = list.iterator(); i.hasNext();) {
            artifactVersion = i.next();
            if(filter.doFilter(artifactVersion)) { i.remove(); }
        }
    }
}
