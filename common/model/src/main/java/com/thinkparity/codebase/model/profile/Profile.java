/*
 * Generated On: Jul 17 06 11:52:34 AM
 */
package com.thinkparity.codebase.model.profile;

import com.thinkparity.codebase.model.user.User;

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

    /** The profile's vcard info. */
    private ProfileVCard vcard;

    /**
     * Create Profile.
     *
     */
	public Profile() {
        super();
    }

    /**
     * Obtain mobilePhone.
     *
     * @return A String.
     */
    public String getMobilePhone() {
        return "";
    }

    /**
     * Obtain organizationAddress.
     *
     * @return A String.
     */
    public String getOrganizationAddress() {
        return "";
    }

    /**
     * Obtain phone.
     *
     * @return A String.
     */
    public String getPhone() {
        return "";
    }

    /**
     * Obtain vcard.
     *
     * @return A <code>ProfileVCard</code>.
     */
    public ProfileVCard getVCard() {
        return vcard;
    }

    /**
     * Set mobilePhone.
     *
     * @param mobilePhone
     *		A String.
     */
    public void setMobilePhone(String mobilePhone) {
    }

    /**
     * Set organizationAddress.
     *
     * @param organizationAddress
     *		A String.
     */
    public void setOrganizationAddress(String organizationAddress) {
    }

    /**
     * Set phone.
     *
     * @param phone
     *		A String.
     */
    public void setPhone(String phone) {
    }

    /**
     * Set vcard.
     *
     * @param vcard
     *		A <code>ProfileVCard</code>.
     */
    public void setVCard(final ProfileVCard vcard) {
        this.vcard = vcard;
    }
}
