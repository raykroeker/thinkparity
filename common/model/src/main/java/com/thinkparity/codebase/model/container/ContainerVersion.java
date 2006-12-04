/*
 * Created On: Jun 29, 2006 11:30:39 AM
 * $Id$
 */
package com.thinkparity.codebase.model.container;

import com.thinkparity.codebase.StringUtil;

import com.thinkparity.codebase.model.artifact.ArtifactVersion;

/**
 * <b>Title:</b>thinkParity Container Version<br>
 * <b>Description:</b>
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class ContainerVersion extends ArtifactVersion {

	/**
     * Create Container.
     * 
     */
	public ContainerVersion() {
        super();
	}

    /**
     * @see java.lang.Object#toString()
     *
     */
    @Override
    public String toString() {
        return StringUtil.toString(getClass(),
                "getArtifactUniqueId()", getArtifactUniqueId(),
                "getArtifactId()", getArtifactId(),
                "getVersionId()", getVersionId());
    }
}
