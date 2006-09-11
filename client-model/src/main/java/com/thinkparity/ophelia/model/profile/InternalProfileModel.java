/*
 * Generated On: Jul 17 06 11:52:34 AM
 */
package com.thinkparity.ophelia.model.profile;


import com.thinkparity.ophelia.model.Context;
import com.thinkparity.ophelia.model.InternalModel;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * <b>Title:</b>thinkParity Profile Internal Model<br>
 * <b>Description:</b>
 *
 * @author CreateModel.groovy
 * @version 
 */
public class InternalProfileModel extends ProfileModel implements InternalModel {

    /**
     * Create InternalProfileModel
     *
     * @param workspace
     *		A thinkParity workspace.
     * @param context
     *		A thinkParity internal context.
     */
    InternalProfileModel(final Workspace workspace, final Context context) {
        super(workspace);
        context.assertContextIsValid();
    }
}
