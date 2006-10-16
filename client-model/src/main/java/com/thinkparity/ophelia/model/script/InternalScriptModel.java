/*
 * Generated On: Oct 15 06 12:36:36 PM
 */
package com.thinkparity.ophelia.model.script;

import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.model.InternalModel;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * <b>Title:</b>thinkParity Script Internal Model<br>
 * <b>Description:</b><br>
 *
 * @author CreateModel.groovy
 * @version 1.1.2.1
 */
public class InternalScriptModel extends ScriptModel implements InternalModel {

    /**
     * Create InternalScriptModel
     *
     * @param context
     *		A thinkParity model <code>Context</code>.
     * @param environment
     *      A thinkParity <code>Environment</code>.
     * @param workspace
     *		A thinkParity <code>Workspace</code>.
     */
    InternalScriptModel(final Context context,
            final Environment environment, final Workspace workspace) {
        super(environment, workspace);
    }
}
