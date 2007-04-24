/*
 * Mar 6, 2006
 */
package com.thinkparity.ophelia.model.index;

import com.thinkparity.codebase.Pair;

import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.util.jta.TransactionType;

/**
 * <b>Title:</b>thinkParity Internal Index Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@ThinkParityTransaction(TransactionType.REQUIRED)
public interface InternalIndexModel extends IndexModel {


    /**
     * Delete a container from the index.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     */
    public void deleteContainer(final Long containerId);

    /**
     * Delete a document from the index.
     * 
     * @param documentId
     *            A document id <code>Long</code>.
     */
    public void deleteDocument(final Long documentId);

    /**
     * Index a container.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     */
    public void indexContainer(final Long containerId);

    /**
     * Index a container version.
     * 
     * @param containerVersionId
     *            A <code>Pair</code>ed container and container version id
     *            <code>Long</code>.
     */
    public void indexContainerVersion(final Pair<Long, Long> containerVersionId);

    /**
     * Index a document.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param documentId
     *            A document id <code>Long</code>.
     */
	public void indexDocument(final Long containerId, final Long documentId);
}
