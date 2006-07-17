/*
 * Created On: Jun 8, 2006 11:37:58 AM
 * $Id$
 */
package com.thinkparity.browser.application.browser.display.provider.contact;

import com.thinkparity.model.parity.model.artifact.ArtifactModel;
import com.thinkparity.model.parity.model.contact.ContactModel;
import com.thinkparity.model.parity.model.profile.Profile;

/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class QuickShareProvider extends ShareProvider {

    /**
     * Create QuickShareProvider.
     * 
     * @param aModel
     *            A thinkParity artifact interface.
     * @param cModel
     *            A thinkParity contact interface.
     */
    public QuickShareProvider(final Profile profile,
            final ArtifactModel aModel, final ContactModel cModel) {
        super(profile, aModel, cModel);
    }

    /** @see com.thinkparity.browser.application.browser.display.provider.CompositeFlatContentProvider#getElements(java.lang.Integer, java.lang.Object) */
    public Object[] getElements(final Object input) {
        return getShareContacts(5, getInputArtifactId(input));
    }
}
