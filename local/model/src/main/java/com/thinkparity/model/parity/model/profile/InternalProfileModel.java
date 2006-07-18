/*
 * Generated On: Jul 17 06 11:52:34 AM
 */
package com.thinkparity.model.parity.model.profile;

import com.thinkparity.model.parity.model.Context;
import com.thinkparity.model.parity.model.InternalModel;
import com.thinkparity.model.parity.model.workspace.Workspace;

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
