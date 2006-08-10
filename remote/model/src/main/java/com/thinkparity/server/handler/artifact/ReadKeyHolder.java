/*
 * Feb 14, 2006
 */
package com.thinkparity.server.handler.artifact;

import java.util.UUID;

import org.xmpp.packet.JID;

import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.jabber.JabberIdBuilder;

import com.thinkparity.server.ParityServerConstants.Xml;
import com.thinkparity.server.handler.AbstractController;
import com.thinkparity.server.org.jivesoftware.messenger.JIDBuilder;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ReadKeyHolder extends AbstractController {

	/** Create ReadKeyHolder. */
	public ReadKeyHolder() { super("artifact:readkeyholder"); }

	/**
     * @see com.thinkparity.codebase.controller.AbstractController#service()
     */
    @Override
    public void service() {
        logApiId();
        final JabberId keyHolder = readKeyHolder(readUUID(Xml.Artifact.UNIQUE_ID));
        if (null != keyHolder) {
            writeJabberId(Xml.User.JABBER_ID, keyHolder);
        }
    }

    /**
     * Read the key holder.
     * 
     * @param uniqueId
     *            An artifact unique id.
     * @return A key holder jabber id.
     */
	private JabberId readKeyHolder(final UUID uniqueId) {
	    final String keyHolder =
            getArtifactModel().get(uniqueId).getArtifactKeyHolder();
        final JID jid = null == keyHolder ? null : JIDBuilder.build(keyHolder);
        return null == jid ? null : JabberIdBuilder.parseJID(jid);
	}
}
