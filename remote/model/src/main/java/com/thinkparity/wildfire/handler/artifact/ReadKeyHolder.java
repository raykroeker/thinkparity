/*
 * Feb 14, 2006
 */
package com.thinkparity.wildfire.handler.artifact;

import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

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
        final JabberId keyHolder = readKeyHolder(
                readJabberId("userId"), readUUID("uniqueId"));
        if (null != keyHolder) {
            writeJabberId("keyHolder", keyHolder);
        }
    }

    /**
     * Read the key holder.
     * 
     * @param uniqueId
     *            An artifact unique id.
     * @return A key holder jabber id.
     */
	private JabberId readKeyHolder(final JabberId userId, final UUID uniqueId) {
	    return getArtifactModel().readKeyHolder(userId, uniqueId);
	}
}
