/*
 * Generated On: Sep 01 06 10:06:21 AM
 */
package com.thinkparity.ophelia.model.archive;


import com.thinkparity.ophelia.model.Context;
import com.thinkparity.ophelia.model.InternalModel;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * <b>Title:</b>thinkParity Archive Internal Model<br>
 * <b>Description:</b><br>
 *
 * @author CreateModel.groovy
 * @version 1.1.2.1
 */
public class InternalArchiveModel extends ArchiveModel implements InternalModel {

    /**
     * Create InternalArchiveModel
     *
     * @param workspace
     *		A thinkParity workspace.
     * @param context
     *		A thinkParity model context.
     */
    InternalArchiveModel(final Workspace workspace, final Context context) {
        super(workspace);
        context.assertContextIsValid();
    }
}
