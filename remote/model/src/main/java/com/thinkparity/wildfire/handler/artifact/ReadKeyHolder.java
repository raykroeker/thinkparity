/*
 * Feb 14, 2006
 */
package com.thinkparity.wildfire.handler.artifact;

import java.util.UUID;

import org.xmpp.packet.JID;

import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.jabber.JabberIdBuilder;

import com.thinkparity.model.Constants.Xml;
import com.thinkparity.model.artifact.Artifact;

import com.thinkparity.server.org.jivesoftware.messenger.JIDBuilder;
import com.thinkparity.wildfire.handler.AbstractHandler;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ReadKeyHolder extends AbstractHandler {

	/** Create ReadKeyHolder. */
	public ReadKeyHolder() { super("artifact:readkeyholder"); }

	/**
     * @see com.thinkparity.codebase.wildfire.handler.AbstractHandler#service()
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
        final Artifact artifact = getArtifactModel().get(uniqueId);
        final JID jid = null == artifact ?
                null : JIDBuilder.build(artifact.getArtifactKeyHolder());
        return null == jid ? null : JabberIdBuilder.parseJID(jid);
	}
}
