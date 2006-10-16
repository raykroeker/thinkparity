/*
 * Generated On: Oct 15 06 12:36:36 PM
 */
package com.thinkparity.ophelia.model.script;

import java.util.List;

import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.ophelia.model.AbstractModel;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * <b>Title:</b>thinkParity Script Model<br>
 * <b>Description:</b><br>
 *
 * @author CreateModel.groovy
 * @version 1.1.2.1
 */
public class ScriptModel extends AbstractModel<ScriptModelImpl> {

	/**
	 * Create a thinkParity Script interface.
	 * 
	 * @param context
     *      A thinkParity model <code>Context</code>.
     * @param environment
     *      A thinkParity <code>Environment</code>.
     * @param workspace
     *      A thinkParity <code>Workspace</code>.
	 * @return A thinkParity Script interface.
	 */
	public static InternalScriptModel getInternalModel(final Context context,
            final Environment environment, final Workspace workspace) {
		return new InternalScriptModel(context, environment, workspace);
	}

	/**
	 * Create a thinkParity Script interface.
	 * 
     * @param environment
     *      A thinkParity <code>Environment</code>.
     * @param workspace
     *      A thinkParity <code>Workspace</code>.
	 * @return A thinkParity Script interface.
	 */
	public static ScriptModel getModel(final Environment environment,
        final Workspace workspace) {
		return new ScriptModel(environment, workspace);
	}

	/**
	 * Create ScriptModel.
	 *
     * @param environment
     *      A thinkParity <code>Environment</code>.
	 * @param workspace
	 *		The thinkParity <code>Workspace</code>.
	 */
	protected ScriptModel(final Environment environment, final Workspace workspace) {
		super(new ScriptModelImpl(environment, workspace));
	}

    /**
     * Execute some scripts.
     * 
     * @param scripts
     *            A <code>List&lt;Script&gt;</code>.
     */
    public void execute(final List<Script> scripts) {
        synchronized (getImplLock()) {
            getImpl().execute(scripts);
        }
    }

    /**
     * Execute a script.
     * 
     * @param script
     *            A <code>Script</code>.
     */
    public void execute(final Script script) {
        synchronized (getImplLock()) {
            getImpl().execute(script);
        }
    }
}
