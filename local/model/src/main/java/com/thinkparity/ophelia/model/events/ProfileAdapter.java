/*
 * Created On 2007-01-27 09:22
 */
package com.thinkparity.ophelia.model.events;

/**
 * <b>Title:</b>thinkParity Profile Listener<br>
 * <b>Description:</b><br>
 *
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ProfileAdapter implements ProfileListener {

    /**
     * @see com.thinkparity.ophelia.model.events.ProfileListener#emailAdded(com.thinkparity.ophelia.model.events.ProfileEvent)
     *
     */
    public void emailAdded(final ProfileEvent e) {}

    /**
     * @see com.thinkparity.ophelia.model.events.ProfileListener#emailRemoved(com.thinkparity.ophelia.model.events.ProfileEvent)
     *
     */
    public void emailRemoved(final ProfileEvent e) {}

    /**
     * @see com.thinkparity.ophelia.model.events.ProfileListener#passwordReset(com.thinkparity.ophelia.model.events.ProfileEvent)
     *
     */
    public void passwordReset(final ProfileEvent e) {}

    /**
     * @see com.thinkparity.ophelia.model.events.ProfileListener#passwordUpdated(com.thinkparity.ophelia.model.events.ProfileEvent)
     *
     */
    public void passwordUpdated(final ProfileEvent e) {}

    /**
     * @see com.thinkparity.ophelia.model.events.ProfileListener#profileUpdated(com.thinkparity.ophelia.model.events.ProfileEvent)
     *
     */
    public void profileUpdated(final ProfileEvent e) {}

    /**
     * @see com.thinkparity.ophelia.model.events.ProfileListener#emailVerified(com.thinkparity.ophelia.model.events.ProfileEvent)
     *
     */
    public void emailVerified(final ProfileEvent e) {}
}
