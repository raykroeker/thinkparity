/*
 * Created On: Oct 15, 2006 12:46:29 PM
 */
package com.thinkparity.ophelia.model.script.engine;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.ophelia.model.script.ScriptException;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class EngineFactory {

    /**
     * Create an instance of a script engine factory.
     * 
     * @param framework
     *            A script <code>EngineFactory.Framework</code>.
     * @param environment
     *            An engine <code>Environment</code>.
     * @return An <code>EngineFactory</code>.
     */
    public static EngineFactory newInstance(final Framework framework,
            final Environment environment) {
        return new EngineFactory(framework, environment);
    }

    /**
     * Create an instance of an engine.
     * 
     * @param clasz
     *            An engine <code>Class</code>.
     * @return An instance of an <code>Engine</code>.
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private static Engine newInstance(final Class<?> clasz)
            throws NoSuchMethodException, InvocationTargetException,
            IllegalAccessException, InstantiationException {
        final Constructor constructor = clasz.getConstructor(new Class<?>[] {});
        return (Engine) constructor.newInstance(new Object[] {});
    }

    /** The script engine <code>Class</code>. */
    private final Class engineClass;

    /** The script engine <code>Environment</code>. */
    private final Environment environment;

    /**
     * Create EngineFactory.
     * 
     */
    private EngineFactory(final Framework framework,
            final Environment environment) {
        super();
        switch (framework) {
        case GROOVY:
            this.engineClass = GroovyEngine.class;
            break;
        default:
            throw Assert.createUnreachable("UNKNOWN FRAMEWORK");
        }
        this.environment = environment;
    }

    /**
     * Create an instance of an engine.
     * 
     * @return An <code>Engine</code>.
     * @throws ScriptException
     */
    public Engine newEngine() throws ScriptException {
        try {
            final Engine engine = newInstance(engineClass);
            engine.initialize(environment);
            return engine;
        } catch (final Throwable t) {
            throw new ScriptException(t);
        }
    }

    /** A definition of all possible script engine frameworks. */
    public enum Framework { GROOVY }
}
