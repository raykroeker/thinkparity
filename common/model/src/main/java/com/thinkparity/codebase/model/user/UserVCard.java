/*
 * Created On:  Sunday December 17, 2006 14:53
 */
package com.thinkparity.codebase.model.user;

/**
 * <b>Title:</b>thinkParity User VCard<br>
 * <b>Description:</b>An abstraction of a user object's interface into their
 * virtual card.<br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class UserVCard {

    /**
     * Create UserVCard.
     *
     */
    protected UserVCard() {
        super();
    }

    public String getVCardXML() {
        return "";
    }

    public void setVCardXML(final String xml) {
    }
}
