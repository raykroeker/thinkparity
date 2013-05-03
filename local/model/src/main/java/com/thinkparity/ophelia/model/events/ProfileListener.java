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
    void emailUpdated(ProfileEvent e);

    /**
     * An profile e-mail was verified.
     *
     * @param e
     *      A <code>ProfileEvent</code>.
     */
    void emailVerified(ProfileEvent e);

    /**
     * The profile password was updated.
     *
     * @param e
     *      A <code>ProfileEvent</code>.
     */
    void passwordUpdated(ProfileEvent e);

    /**
     * The profile was disabled.
     * 
     * @param e
     *            A <code>ProfileEvent</code>.
     */
    void profileActivated(ProfileEvent e);

    /**
     * The profile was passivated.
     *
     * @param e
     *      A <code>ProfileEvent</code>.
     */
    void profilePassivated(ProfileEvent e);

    /**
     * The profile was updated.
     * 
     * @param e
     *            A <code>ProfileEvent</code>.
     */
    void profileUpdated(ProfileEvent e);
}
