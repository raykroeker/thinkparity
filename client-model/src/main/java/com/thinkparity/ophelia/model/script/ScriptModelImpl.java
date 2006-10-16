/*
 * Generated On: Oct 15 06 12:36:36 PM
 */
package com.thinkparity.ophelia.model.script;

import java.util.ArrayList;
import java.util.List;

import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.ophelia.model.AbstractModelImpl;
import com.thinkparity.ophelia.model.script.engine.Engine;
import com.thinkparity.ophelia.model.script.engine.EngineFactory;
import com.thinkparity.ophelia.model.script.engine.EngineFactory.Framework;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * <b>Title:</b>thinkParity Script Model Implementation</br>
 * <b>Description:</b><br>
 *
 * @author CreateModel.groovy
 * @version 1.1.2.1
 */
final class ScriptModelImpl extends AbstractModelImpl {

    /**
     * Create ScriptModelImpl.
     *
     * @param environment
     *      A thinkParity <code>Environment</code>.
     * @param workspace
     *		The thinkParity <code>Workspace</code>.
     */
    ScriptModelImpl(final Environment environment, final Workspace workspace) {
        super(environment, workspace);
    }

    /**
     * Execute a series of scripts.
     * 
     * @param scripts
     *            A <code>List&lt;Script&gt;</code>.
     */
    void execute(final List<Script> scripts) {
        logger.logApiId();
        logger.logVariable("scripts", scripts);
        try {
            final EngineFactory factory =
                EngineFactory.newInstance(Framework.GROOVY, createEnvironment());
            final Engine engine = factory.newEngine();
            engine.execute(scripts);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Execute a script.
     * 
     * @param script
     *            A <code>Script</code>.
     */
    void execute(final Script script) {
        logger.logApiId();
        logger.logVariable("script", script);
        final List<Script> scripts = new ArrayList<Script>(1);
        scripts.add(script);
        execute(scripts);
    }

    /**
     * Create the script engine <code>Environment</code>.
     * 
     * @return A script engine <code>Environment</code>.
     */
    private com.thinkparity.ophelia.model.script.engine.Environment createEnvironment() {
        final com.thinkparity.ophelia.model.script.engine.Environment environment = new com.thinkparity.ophelia.model.script.engine.Environment();
        environment.setVariable("builder", new ContainerBuilder(this.environment, workspace));
        return environment;
    }
}
