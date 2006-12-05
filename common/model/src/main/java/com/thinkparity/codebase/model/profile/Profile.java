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

    /** A mobile phone <code>String</code>. */
    private String mobilePhone;

    /** An organziation address <code>String</code>. */
    private String organizationAddress;

    /** A phone <code>String</code>. */
    private String phone;

	/** Create Profile. */
	public Profile() {
        super();
    }

    /**
     * Obtain mobilePhone.
     *
     * @return A String.
     */
    public String getMobilePhone() {
        return mobilePhone;
    }

    /**
     * Obtain organizationAddress.
     *
     * @return A String.
     */
    public String getOrganizationAddress() {
        return organizationAddress;
    }

    /**
     * Obtain phone.
     *
     * @return A String.
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Set mobilePhone.
     *
     * @param mobilePhone
     *		A String.
     */
    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    /**
     * Set organizationAddress.
     *
     * @param organizationAddress
     *		A String.
     */
    public void setOrganizationAddress(String organizationAddress) {
        this.organizationAddress = organizationAddress;
    }

    /**
     * Set phone.
     *
     * @param phone
     *		A String.
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }
}
