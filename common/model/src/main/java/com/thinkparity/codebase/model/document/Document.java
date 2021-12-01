/*
 * Feb 27, 2005
 */
package com.thinkparity.codebase.model.document;

import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.artifact.ArtifactType;

/**
 * The document is the parity object that started the revolution. It is used as
 * the anchor for both the document content and the document versions. One
 * document has a single document content reference (or more aptly a single
 * document content has a single document referenc) and many document versions.
 * 
 * The document content is a wrapper around the document's bytes. The document
 * version contains a snapshot of the document at a single point in time.
 * 
 * @author raymond@raykroeker.com
 * @version 1.2.2.12
 * @see DocumentContent
 * @see DocumentVersion
 */
public class Document extends Artifact {

	/** Create Document. */
	public Document() { super(); }

	/**
     * Obtain the type of parity object.
     * 
     * @return The type of parity object.
     * @see Artifact#getType()
     * @see ArtifactType#DOCUMENT
     */
	public ArtifactType getType() { return ArtifactType.DOCUMENT; }
}
