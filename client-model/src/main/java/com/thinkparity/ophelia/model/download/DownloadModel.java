/*
 * Created On: 
 * $Id$
 */
package com.thinkparity.ophelia.model.download;


import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.util.jta.TransactionType;

import com.thinkparity.ophelia.model.ParityException;

/**
 * <b>Title:</b>thinkParity Download Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.5
 */
@ThinkParityTransaction(TransactionType.NEVER)
public interface DownloadModel {

    /**
     * Download a release.
     * 
     * @param release
     *            A release.
     * @throws ParityException
     */
    public void download(final Release release) throws ParityException;

    /**
     * Determine the download for release is complete.
     * 
     * @return True if the download for the latest version is complete; false
     *         otherwise.
     */
    public Boolean isComplete(final Release release) throws ParityException;
}
