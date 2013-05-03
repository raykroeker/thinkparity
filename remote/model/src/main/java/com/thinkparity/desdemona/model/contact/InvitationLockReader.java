/*
 * Created On:  17-Oct-07 9:10:43 AM
 */
package com.thinkparity.desdemona.model.contact;

import java.util.List;

import com.thinkparity.codebase.model.contact.ContactInvitation;

/**
 * <b>Title:</b>thinkParity Desdemona Model Contact Invitation Lock Reader<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface InvitationLockReader {

    /**
     * Read the invitations to be locked.
     * 
     * @return A <code>List<ContactInvitation></code>.
     */
    List<ContactInvitation> read();
}
