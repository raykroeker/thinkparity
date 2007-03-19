/*
 * Generated On: Jun 27 06 12:13:12 PM
 */
package com.thinkparity.codebase.model.container;

import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.artifact.ArtifactType;

/**
 * <b>Title:</b>thinkParity Container<br>
 * <b>Description:</b>
 * 
 * @author CreateModel.groovy; raymond@thinkparity.com
 * @version 1.1.2.3
 */
public class Container extends Artifact {

    /**
     * Create Container.
     *
     */
	public Container() {
        super();
	}

    /**
     * @see com.thinkparity.codebase.model.artifact.Artifact#getType()
     * 
     */
    public ArtifactType getType() {
        return ArtifactType.CONTAINER;
    }
}
