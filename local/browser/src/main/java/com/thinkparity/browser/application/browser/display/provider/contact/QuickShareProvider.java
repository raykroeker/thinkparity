/*
 * Created On: Jun 8, 2006 11:37:58 AM
 * $Id$
 */
package com.thinkparity.browser.application.browser.display.provider.contact;

import com.thinkparity.model.parity.model.artifact.ArtifactModel;
import com.thinkparity.model.parity.model.session.SessionModel;

/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class QuickShareProvider extends ShareProvider {

    /**
     * Create QuickShareProvider.
     * 
     * @param aModel
     *            An artifact model.
     * @param sModel
     *            A session model.
     */
    public QuickShareProvider(final ArtifactModel aModel, final SessionModel sModel) {
        super(aModel, sModel);
    }

    /** @see com.thinkparity.browser.application.browser.display.provider.CompositeFlatContentProvider#getElements(java.lang.Integer, java.lang.Object) */
    public Object[] getElements(final Object input) {
        return getShareContacts(5, getInputArtifactId(input));
    }
}
