/**
 * Created On: 20-Jun-2006 2:38:09 PM
 * $Id$
 */
package com.thinkparity.browser.application.browser.display.provider.contact;

import com.thinkparity.browser.application.browser.display.provider.CompositeFlatSingleContentProvider;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.model.parity.model.artifact.ArtifactModel;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.message.system.SystemMessageModel;
import com.thinkparity.model.parity.model.session.SessionModel;
import com.thinkparity.model.xmpp.JabberId;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class ContactsProvider extends CompositeFlatSingleContentProvider {

    /**
     * Create a ContactsProvider.
     * 
     * @param aModel
     *            The parity artifact interface.
     * @param dModel
     *            The parity document interface.
     * @param sModel
     *            The parity session interface.
     * @param systemMessageModel
     *            The parity system message interface.
     */
    public ContactsProvider(final ArtifactModel artifactModel,
            final DocumentModel dModel, final SessionModel sModel,
            final SystemMessageModel systemMessageModel,
            final JabberId loggedInUserId) {
        super();
    }
    
    /**
     * @see com.thinkparity.browser.application.browser.display.provider.CompositeFlatSingleContentProvider#getElement(java.lang.Integer,
     *      java.lang.Object)
     * 
     */
    public Object getElement(final Integer index, final Object input) {
        Assert.assertNotNull("Index cannot be null.", index);
        /*
        Assert.assertTrue(
                "Index must lie within [0," + (singleProviders.length - 1) + "]",
                index >= 0 && index < singleProviders.length);
        return singleProviders[index].getElement(input);
        */
        Object object = new Object();
        return object;
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.provider.CompositeFlatContentProvider#getElements(java.lang.Integer,
     *      java.lang.Object)
     * 
     */
    public Object[] getElements(final Integer index, final Object input) {
        Assert.assertNotNull("Index cannot be null.", index);
        /*
        Assert.assertTrue(
                "Index must lie within [0," + (flatProviders.length - 1) + "]",
                index >= 0 && index < flatProviders.length);
        return flatProviders[index].getElements(input);
        */
        Object[] objects = new Object[4];
        return objects;
    }

}

