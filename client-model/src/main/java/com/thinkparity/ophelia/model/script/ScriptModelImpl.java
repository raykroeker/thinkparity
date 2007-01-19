/*
 * Generated On: Oct 15 06 12:36:36 PM
 */
package com.thinkparity.ophelia.model.script;

import java.io.InputStream;
import java.util.List;

import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.model.Model;
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
public final class ScriptModelImpl extends Model implements
        ScriptModel, InternalScriptModel {

    /**
     * Create ScriptModelImpl.
     *
     */
    public ScriptModelImpl() {
        super();
    }

    /**
     * Execute a series of scripts.
     * 
     * @param scripts
     *            A <code>List&lt;Script&gt;</code>.
     */
    public void execute(final List<Script> scripts) {
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
     * @see com.thinkparity.ophelia.model.Model#initializeModel(com.thinkparity.codebase.model.session.Environment, com.thinkparity.ophelia.model.workspace.Workspace)
     *
     */
    @Override
    protected void initializeModel(final Environment environment,
            final Workspace workspace) {
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
