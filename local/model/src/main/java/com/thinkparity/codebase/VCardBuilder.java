/*
 * Created On: Jul 20, 2006 12:37:52 PM
 */
package com.thinkparity.codebase;

import org.jivesoftware.smackx.provider.VCardProvider;


/**
 * @author raymond@thinkparity.com
 * @version
 */
public class VCardBuilder {

    private static final VCardBuilder SINGLETON;

    static { SINGLETON = new VCardBuilder(); }

    /**
     * Create a thinkParity vCard from a jive vCard.
     * 
     * @param vCard
     *            A jive vCard.
     * @return A thinkParity vCard.
     */
    public static VCard createVCard(final org.jivesoftware.smackx.packet.VCard vCard) {
        return SINGLETON.doCreateVCard(vCard);
    }

    public static VCard createVCard(final String xml) {
        return SINGLETON.doCreateVCard(xml);
    }

    /** Create VCardBuilder. */
    private VCardBuilder() { super(); }

    /**
     * Create a thinkParity vCard from a jive vCard.
     * 
     * @param vCard
     *            A jive vCard.
     * @return A thinkParity vCard.
     */
    private VCard doCreateVCard(final org.jivesoftware.smackx.packet.VCard vCard) {
        return new VCard(vCard);
    }
    /**
     * Create a thinkParity vCard from a jive vCard.
     * 
     * @param vCard
     *            A jive vCard.
     * @return A thinkParity vCard.
     */
    private VCard doCreateVCard(final String xml) {
        return doCreateVCard(VCardProvider._createVCardFromXml(xml));
    }
}
