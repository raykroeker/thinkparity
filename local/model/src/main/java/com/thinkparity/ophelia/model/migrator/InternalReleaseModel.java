/*
 * Created On: Fri May 12 2006 09:11 PDT
 * $Id$
 */
package com.thinkparity.ophelia.model.migrator;


import com.thinkparity.ophelia.model.Context;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * The parity bootstrap's internal release model.
 *
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class InternalReleaseModel extends ReleaseModel {

    /** Create InternalReleaseModel. */
    InternalReleaseModel(final Context context, final Workspace workspace) {
        super(workspace);
    }
}
