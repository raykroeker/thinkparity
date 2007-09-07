/*
 * Created On 2007-01-27 09:22
 */
package com.thinkparity.ophelia.model.events;

import com.thinkparity.codebase.event.EventListener;

/**
 * <b>Title:</b>thinkParity Profile Listener<br>
 * <b>Description:</b><br>
 *
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface ProfileListener extends EventListener {

    /**
     * The e-mail was updated within the profile.
     *
     * @param e
     *      A <code>ProfileEvent</code>.
     */
    public void emailUpdated(final ProfileEvent e);

    /**
     * An profile e-mail was verified.
     *
     * @param e
     *      A <code>ProfileEvent</code>.
     */
    public void emailVerified(final ProfileEvent e);

    /**
     * The profile password was updated.
     *
     * @param e
     *      A <code>ProfileEvent</code>.
     */
    public void passwordUpdated(final ProfileEvent e);

    /**
     * The profile was updated.
     *
     * @param e
     *      A <code>ProfileEvent</code>.
     */
    public void profileUpdated(final ProfileEvent e);
}
