/*
 * Created On: Fri May 12 2006 09:11 PDT
 * $Id$
 */
package com.thinkparity.model.parity.model.release;

import com.thinkparity.model.parity.model.Context;
import com.thinkparity.model.parity.model.InternalModel;
import com.thinkparity.model.parity.model.workspace.Workspace;

/**
 * The parity bootstrap's internal release model.
 *
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class InternalReleaseModel extends ReleaseModel implements InternalModel {

    /** Create InternalReleaseModel. */
    InternalReleaseModel(final Context context, final Workspace workspace) {
        super(workspace);
        context.assertContextIsValid();
    }
}
