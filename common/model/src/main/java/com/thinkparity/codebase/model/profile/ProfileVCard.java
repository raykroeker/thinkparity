/*
 * Created On:  Sunday December 17, 2006 14:53
 */
package com.thinkparity.codebase.model.profile;

import com.thinkparity.codebase.model.user.UserVCard;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * <b>Title:</b>thinkParity Profile VCard<br>
 * <b>Description:</b>A user's personal vcard.<br>
 *
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@XStreamAlias("v-card")
public class ProfileVCard extends UserVCard {

    /**
     * Create ProfileVCard.
     *
     */
    public ProfileVCard() {
        super();
    }
}
