/*
 * Generated On: Jun 27 06 12:13:12 PM
 * $Id$
 */
package com.thinkparity.model.parity.model.container;

import com.thinkparity.model.parity.model.artifact.Artifact;
import com.thinkparity.model.parity.model.artifact.ArtifactType;

/**
 * <b>Title:</b>thinkParity Container<br>
 * <b>Description:</b>
 * 
 * @author CreateModel.groovy
 * @version $Revision$
 */
public class Container extends Artifact {

	/** Create Container. */
	public Container() { super(); }

    /**
     * @see com.thinkparity.model.parity.model.artifact.Artifact#getType()
     * 
     */
    public ArtifactType getType() { return ArtifactType.CONTAINER; }
}
