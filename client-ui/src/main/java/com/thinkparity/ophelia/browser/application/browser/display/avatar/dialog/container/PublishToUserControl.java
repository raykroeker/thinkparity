/**
 * Created On: 12-Aug-07 9:36:22 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.container;

import java.util.List;

import com.thinkparity.codebase.email.EMail;

import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.user.TeamMember;

import com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.container.PublishContainerProvider;
import com.thinkparity.ophelia.browser.util.localization.Localization;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public interface PublishToUserControl {

    /**
     * Extract the list of contacts.
     * The list will not include contacts that are also team members.
     * 
     * @return A <code>List</code> of <code>Contact</code>.
     */
    public List<Contact> extractContacts();

    /**
     * Extract the list of emails.
     * The list will include only valid emails and it will
     * not include emails that are also contacts.
     * 
     * @return A <code>List</code> of <code>EMail</code>.
     */
    public List<EMail> extractEMails();

    /**
     * Extract the list of team members.
     * 
     * @return A <code>List</code> of <code>TeamMember</code>.
     */
    public List<TeamMember> extractTeamMembers();

    /**
     * Gets an input error localized message, or null if input is valid.
     * 
     * @return A <code>String</code> error.
     */
    public String getInputError();

    /**
     * Determine if the input is valid.
     * 
     * @return A <code>Boolean</code>, true if the input is valid.
     */
    public Boolean isInputValid();

    /**
     * Reload.
     */
    public void reload();

    /**
     * Set the container id.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     */
    public void setContainerId(final Long containerId);

    /**
     * Set the content provider.
     * 
     * @param contentProvider
     *            A <code>PublishContainerProvider</code>.
     */
    public void setContentProvider(final PublishContainerProvider contentProvider);

    /**
     * Set the localization.
     * 
     * @param localization
     *            A <code>Localization</code>.
     */
    public void setLocalization(final Localization localization);
}
