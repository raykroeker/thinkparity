/*
 * Generated On: Oct 15 06 12:36:36 PM
 */
package com.thinkparity.ophelia.model.script;

import java.io.InputStream;
import java.util.List;

import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.ophelia.model.AbstractModelImpl;
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
            EngineFactory factory;
            for (final Script script : scripts) {
                factory = EngineFactory.newInstance(
                        Framework.GROOVY, createEnvironment(script));
                factory.newEngine().execute(script);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Create the script engine <code>Environment</code>.
     * 
     * @param resourceLoader
     *            A resource loader <code>ClassLoader</code>.
     * @return A script engine <code>Environment</code>.
     */
    private com.thinkparity.ophelia.model.script.engine.Environment createEnvironment(
            final Script script) {
        final com.thinkparity.ophelia.model.script.engine.Environment environment = new com.thinkparity.ophelia.model.script.engine.Environment();
        environment.setVariable("builder",
                new ContainerBuilder(this.environment, workspace, new ScriptUtil() {
                    public InputStream openResource(final String name) {
                        return script.openResource(name);
                    }
                }));
        return environment;
    }
}
