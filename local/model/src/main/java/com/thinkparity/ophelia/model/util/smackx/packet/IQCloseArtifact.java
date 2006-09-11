/*
 * Feb 18, 2006
 */
package com.thinkparity.ophelia.model.util.smackx.packet;

import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.jabber.JabberIdBuilder;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQCloseArtifact extends IQArtifact {

	/**
	 * Create a IQCloseArtifact.
	 * 
	 */
	public IQCloseArtifact(final UUID artifactUniqueId) {
		super(Action.CLOSEARTIFACT, artifactUniqueId);
	}

	public JabberId getClosedBy() {
		return JabberIdBuilder.parseQualifiedJabberId(getFrom());
	}
}
