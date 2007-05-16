/**
 * Created On: 16-May-07 11:54:11 AM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.container;

import com.thinkparity.ophelia.model.container.ContainerModel;
import com.thinkparity.ophelia.model.profile.ProfileModel;

import com.thinkparity.ophelia.browser.application.browser.display.provider.ContentProvider;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class UpdateNoteProvider extends ContentProvider {

    /** A thinkParity container interface. */
    private final ContainerModel containerModel;

    /**
     * Create UpdateNoteProvider.
     * 
     * @param profileModel
     *            An instance of <code>ProfileModel</code>.
     * @param containerModel
     *            An instance of <code>ContainerModel</code>.
     * @param userModel
     *            An instance of <code>UserModel</code>.
     * @param contactModel
     *            An instance of <code>ContactModel</code>.
     */
    public UpdateNoteProvider(final ProfileModel profileModel,
            final ContainerModel containerModel) {
        super(profileModel);
        this.containerModel = containerModel;
    }

    /**
     * Read the container draft comment.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @return The comment <code>String</code>.  
     */
    public String readComment(final Long containerId) {
        return containerModel.readDraft(containerId).getComment();
    }
}
