/*
 * Feb 18, 2006
 */
package com.thinkparity.model.smackx.packet;

import java.util.UUID;

import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.JabberIdBuilder;

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
