/**
 * Created On: 10-Nov-06 4:06:18 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.container;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.codebase.model.document.Document;

import com.thinkparity.ophelia.model.container.ContainerDraft;
import com.thinkparity.ophelia.model.container.ContainerModel;
import com.thinkparity.ophelia.model.profile.ProfileModel;

import com.thinkparity.ophelia.browser.application.browser.display.provider.FlatContentProvider;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class RenameDocumentProvider extends FlatContentProvider {
    
    /** A thinkParity container interface. */
    private final ContainerModel containerModel;
    
    /** Create RenameDocumentProvider. */
    public RenameDocumentProvider(final ProfileModel profileModel,
            final ContainerModel containerModel) {
        super(profileModel);
        this.containerModel = containerModel;
    }

    @Override
    public Object[] getElements(Object input) {
        throw Assert.createNotYetImplemented("RenameDocumentProvider#getElements");
    }

    /**
     * Read the list of documents associated with the draft.
     * 
     * @param containerId
     *            A container id <code>Long</code>.       
     * @return An array of <code>Document</code>.
     */
    public Document[] readDraftDocuments(final Long containerId) {
        final ContainerDraft draft = containerModel.readDraft(containerId);
        if (null == draft) {
            return null;
        } else {
            return draft.getDocuments().toArray(new Document[] {});
        }
    }
}
