/*
 * Generated On: Jul 17 06 11:52:34 AM
 */
package com.thinkparity.model.parity.model.profile;

import com.thinkparity.codebase.VCard;

import com.thinkparity.model.xmpp.user.User;

/**
 * <b>Title:</b>thinkParity Profile<br>
 * <b>Description:</b>A thinkParity profile is the same as a thinkParity
 * {@link User} save it represents the local user. Additional functionality is
 * that the profile contains e-mail addresses as well as the user's v-card
 * information.
 * 
 * @author CreateModel.groovy
 * @version
 */
public class Profile extends User {

    /** A vCard. */
    private VCard vCard;

	/** Create Profile. */
	public Profile() {
        super();
    }

    /**
     * Obtain the vCard
     *
     * @return The VCard.
     */
    public VCard getVCard() {
        return vCard;
    }

    /**
     * Set vCard.
     *
     * @param card The VCard.
     */
    public void setVCard(final VCard vCard) {
        this.vCard = vCard;
    }
}
