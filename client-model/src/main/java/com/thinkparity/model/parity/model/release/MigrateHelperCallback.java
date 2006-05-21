/*
 * Created On: Fri May 12 2006 09:11 PDT
 * $Id$
 */
package com.thinkparity.model.parity.model.release;

import java.io.File;

/**
 * A callback interface for the migrate helper.
 *
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public interface MigrateHelperCallback {

    public File getDownloadRoot();

    public Byte[] readLibraryBytes(final Long libraryId);
}
