/*
 * Created On:  17-Jan-07 11:34:13 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.provider.tab.archive;

import com.thinkparity.codebase.filter.Filter;

import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.profile.Profile;

import com.thinkparity.ophelia.model.contact.ContactModel;
import com.thinkparity.ophelia.model.container.ContainerModel;
import com.thinkparity.ophelia.model.document.DocumentModel;
import com.thinkparity.ophelia.model.user.UserModel;

import com.thinkparity.ophelia.browser.application.browser.display.provider.tab.container.ContainerProvider;

/**
 * <b>Title:</b>thinkParity Archive Tab Provider<br>
 * <b>Description:</b>The archive tab provider is a carbon copy of the
 * container tab provider. It simply stipulates that the list of containers are
 * archived.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ArchiveTabProvider extends ContainerProvider {

    /**
     * Create ArchiveTabProvider.
     * 
     * @param profile
     *            The local user profile.
     * @param containerModel
     *            An instance of <code>ContainerModel</code>.
     * @param documentModel
     *            An instance of <code>DocumentModel</code>.
     * @param userModel
     *            An instance of <code>UserModel</code>.
     */
    public ArchiveTabProvider(final Profile profile,
            final ContactModel contactModel,
            final ContainerModel containerModel,
            final DocumentModel documentModel, final UserModel userModel) {
        super(new Filter<Artifact>() {
            public Boolean doFilter(final Artifact o) {
                return Boolean.valueOf(!o.isArchived().booleanValue());
            }
        }, profile, contactModel, containerModel, documentModel, userModel);
    }
}
