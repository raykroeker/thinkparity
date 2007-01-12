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
 * <b>Title:</b>thinkParity Internal Download Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.4
 */
@ThinkParityTransaction(TransactionType.NEVER)
public interface InternalDownloadModel extends DownloadModel {

    /**
     * Read the latest release that has been downloaded.
     * 
     * @return A release.
     */
    public Release read() throws ParityException;
}
