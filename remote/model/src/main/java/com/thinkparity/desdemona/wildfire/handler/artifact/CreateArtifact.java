/*
 * Dec 1, 2005
 */
package com.thinkparity.desdemona.wildfire.handler.artifact;

import java.util.Calendar;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.desdemona.wildfire.handler.AbstractHandler;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class CreateArtifact extends AbstractHandler {

	/**
	 * Create a CreateArtifact.
	 */
	public CreateArtifact() { super("artifact:create"); }

	/**
     * @see com.thinkparity.codebase.wildfire.handler.AbstractHandler#service()
     */
    @Override
    public void service() {
        logApiId();
        create(readJabberId("userId"), readUUID("uniqueId"), readCalendar("createdOn"));
    }

    /**
     * Create an artifact.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     */
    private void create(final JabberId userId, final UUID uniqueId,
            final Calendar createdOn) {
        getArtifactModel().create(userId, uniqueId, createdOn);
    }
}
