/*
 * Created On: 
 * $Id$
 */
package com.thinkparity.ophelia.model.migrator;

import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.migrator.LibraryBytes;
import com.thinkparity.codebase.model.util.jta.TransactionType;

/**
 * <b>Title:</b>thinkParity Internal Library Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.4
 */
@ThinkParityTransaction(TransactionType.REQUIRED)
public interface InternalLibraryModel extends LibraryModel {

    /**
     * Read the library bytes.
     * 
     * @param libraryId
     *            A library id <code>Long</code>.
     * @return A <code>LibraryBytes</code>.
     */
    public LibraryBytes readBytes(final Long libraryId);
}
