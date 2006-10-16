/*
 * Created On: Oct 15, 2006 1:02:43 PM
 */
package com.thinkparity.ophelia.model.script.engine;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map.Entry;

import com.thinkparity.ophelia.model.script.Script;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class GroovyEngine implements Engine {

    /** The script engine <code>Environment</code>. */
    private Environment environment;

    /** Create GroovyEngine. */
    public GroovyEngine() {
        super();
    }

    /**
     * @see com.thinkparity.ophelia.model.script.engine.Engine#execute(java.util.List)
     */
    public void execute(final List<Script> scripts) throws IOException {
        final GroovyShell shell = newGroovyShell(environment);

        InputStream stream;
        for (final Script script : scripts) {
            stream = script.openStream();
            try {
                shell.evaluate(script.openStream(), script.getName());
            } finally {
                stream.close();
            }
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.script.engine.Engine#initialize(com.thinkparity.ophelia.model.script.engine.Environment)
     */
    public void initialize(final Environment environment) {
        this.environment = environment;
    }

    /**
     * Create the groovy bindings.
     * 
     * @param environment
     *            A script engine <code>Environment</code>.
     * @return An instance of <code>Binding</code>.
     */
    private Binding newBinding(final Environment environment) {
        final Binding binding = new Binding();
        for (final Entry<String, String> entry : environment.getProperties()) {
            binding.setProperty(entry.getKey(), entry.getValue());
        }
        for (final Entry<String, Object> entry : environment.getVariables()) {
            binding.setVariable(entry.getKey(), entry.getValue());
        }
        return binding;
    }

    /**
     * Create the groovy shell.
     * 
     * @param environment
     *            A script engine <code>Environment</code>.
     * @return An instance of <code>GroovyShell</code>.
     */
    private GroovyShell newGroovyShell(final Environment environment) {
        final GroovyShell shell = new GroovyShell(newBinding(environment));
        shell.resetLoadedClasses();
        return shell;
    }
}
