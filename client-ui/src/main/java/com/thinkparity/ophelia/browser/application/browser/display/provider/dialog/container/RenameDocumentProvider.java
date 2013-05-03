/**
 * Created On: 10-Nov-06 4:06:18 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.container;

import java.util.Collections;
import java.util.List;

import com.thinkparity.codebase.model.document.Document;

import com.thinkparity.ophelia.model.container.ContainerDraft;
import com.thinkparity.ophelia.model.container.ContainerModel;
import com.thinkparity.ophelia.model.profile.ProfileModel;

import com.thinkparity.ophelia.browser.application.browser.display.provider.ContentProvider;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class RenameDocumentProvider extends ContentProvider {

    /** A thinkParity container interface. */
    private final ContainerModel containerModel;

    /** Create RenameDocumentProvider. */
    public RenameDocumentProvider(final ProfileModel profileModel,
            final ContainerModel containerModel) {
        super(profileModel);
        this.containerModel = containerModel;
    }

    /**
     * Read the list of documents associated with the draft.
     * 
     * @param containerId
     *            A container id <code>Long</code>.       
     * @return An array of <code>Document</code>.
     */
    public List<Document> readDraftDocuments(final Long containerId) {
        final ContainerDraft draft = containerModel.readDraft(containerId);
        if (null == draft) {
            return Collections.emptyList();
        } else {
            return draft.getDocuments();
        }
    }
}
