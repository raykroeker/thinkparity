/*
 * Created On: Jun 29, 2006 11:30:39 AM
 * $Id$
 */
package com.thinkparity.model.parity.model.container;

import com.thinkparity.model.parity.model.artifact.ArtifactVersion;

/**
 * <b>Title:</b>thinkParity Container Version<br>
 * <b>Description:</b>
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class ContainerVersion extends ArtifactVersion {

	/** Create Container. */
	public ContainerVersion() { super(); }

    /** @see java.lang.Object#equals(java.lang.Object) */
    @Override
    public boolean equals(final Object obj) {
        if(null != obj && obj instanceof ContainerVersion) {
            return ((ContainerVersion) obj).getArtifactId().equals(getArtifactId()) &&
                    ((ContainerVersion) obj).getVersionId().equals(getVersionId());
        }
        return false;
    }

    /** @see java.lang.Object#hashCode() */
    @Override
    public int hashCode() {
        return getArtifactId().hashCode() & getVersionId().hashCode();
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new StringBuffer(getClass().getName()).append("//")
            .append(getArtifactId())
            .append("/").append(getVersionId())
            .toString();
    }
}
