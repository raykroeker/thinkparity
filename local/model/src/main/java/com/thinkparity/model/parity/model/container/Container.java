/*
 * Generated On: Jun 27 06 12:13:12 PM
 * $Id$
 */
package com.thinkparity.model.parity.model.container;

import java.util.Calendar;
import java.util.Collection;
import java.util.UUID;

import com.thinkparity.model.parity.model.artifact.Artifact;
import com.thinkparity.model.parity.model.artifact.ArtifactFlag;
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
     * Create a Container.
     * 
     * @param createdBy
     *            The creator.
     * @param createdOn
     *            The creation date.
     * @param description
     *            The description.
     * @param flags
     *            The object flags.
     * @param id
     *            The unique id.
     * @param name
     *            The name.
     * @param updatedBy
     *            The updator.
     * @param updatedOn
     *            The update date.
     */
    public Container(final String createdBy, final Calendar createdOn,
            final String description, final Collection<ArtifactFlag> flags,
            final UUID uniqueId, final String name, final String updatedBy,
            final Calendar updatedOn) {
        super(createdBy, createdOn, description, flags, uniqueId, name, null,
                updatedBy, updatedOn);
    }

    /**
     * @see com.thinkparity.model.parity.model.artifact.Artifact#getType()
     * 
     */
    public ArtifactType getType() { return ArtifactType.CONTAINER; }
}
