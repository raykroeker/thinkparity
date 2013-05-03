/*
 * Created On:  2007-07-21 14:14 -0700
 */
package com.thinkparity.ophelia.model.document;

import com.thinkparity.codebase.io.StreamOpener;

import com.thinkparity.codebase.model.document.DocumentVersion;

import com.thinkparity.ophelia.model.DefaultDelegate;
import com.thinkparity.ophelia.model.io.handler.DocumentIOHandler;

/**
 * <b>Title:</b>thinkParity OpheliaModel Document Delegate<br>
 * <b>Description:</b>The abstraction of all document delegate implemenations.
 * It uses the package scope implementation of the container model to share
 * implementation. It also contains the persistence io instances for the
 * delegates.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class DocumentDelegate extends
        DefaultDelegate<DocumentModelImpl> {

    /** An instance of a document persistence interface. */ 
    protected DocumentIOHandler documentIO;

    /**
     * Create DocumentDelegate.
     * 
     */
    protected DocumentDelegate() {
        super();
    }

    /**
     * @see com.thinkparity.ophelia.model.DefaultDelegate#initialize(com.thinkparity.ophelia.model.Model)
     * 
     */
    @Override
    public void initialize(final DocumentModelImpl modelImplementation) {
        super.initialize(modelImplementation);
        this.documentIO = modelImplementation.getDocumentIO();
    }

    /**
     * @see DocumentModelImpl#openVersion(Long, Long, StreamOpener)
     * 
     */
    protected final void openVersion(final DocumentVersion version,
            final StreamOpener opener) {
        modelImplementation.openVersion(version.getArtifactId(),
                version.getVersionId(), opener);
    }
}
