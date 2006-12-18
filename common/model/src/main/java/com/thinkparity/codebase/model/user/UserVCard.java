/*
 * Created On:  Sunday December 17, 2006 14:53
 */
package com.thinkparity.codebase.model.user;

import org.dom4j.Document;

/**
 * <b>Title:</b>thinkParity User VCard<br>
 * <b>Description:</b>An abstraction of a user object's interface into their
 * virtual card.<br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class UserVCard {

    /** The vcard <code>Document</code>. */
    private Document vcard;

    /**
     * Create UserVCard.
     *
     */
    protected UserVCard() {
        super();
    }

    public void setVCard(final Document vcard) {
        this.vcard = vcard;
    }

    public Document getVCard() {
        return vcard;
    }

    public String getPhone() {
        return "";
    }

    public Boolean isSetPhone() {
        return Boolean.FALSE;
    }

    public void setPhone(final String phone) {
    }

    public String getMobilePhone() {
        return "";
    }

    public Boolean isSetMobilePhone() {
        return Boolean.FALSE;
    }

    public void setMobilePhone(final String cell) {
    }

    public String getVCardAsXML() {
        return vcard.asXML();
    }

    public void setVCardXML(final String vcardXML) {
    }
}
