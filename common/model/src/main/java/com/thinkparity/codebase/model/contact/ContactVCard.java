/*
 * Created On:  Sunday December 17, 2006 14:53
 */
package com.thinkparity.codebase.model.contact;

import com.thinkparity.codebase.model.user.UserVCard;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * <b>Title:</b>thinkParity Contact VCard<br>
 * <b>Description:</b>A contact's vcard.<br>
 *
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@XStreamAlias("v-card")
public class ContactVCard extends UserVCard {

    /**
     * Create ContactVCard.
     *
     */
    public ContactVCard() {
        super();
    }
}
