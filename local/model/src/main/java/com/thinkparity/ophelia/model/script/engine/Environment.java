/*
 * Created On: Oct 15, 2006 2:18:11 PM
 */
package com.thinkparity.ophelia.model.script.engine;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Environment {

    private final Map<String, String> properties;

    private final Map<String, Object> variables;

    /** Create ScriptEnvironment. */
    public Environment() {
        this(new Hashtable<String, String>(7, 0.75F),
                new Hashtable<String, Object>(7, 0.75F));
    }

    /** Create ScriptEnvironment. */
    public Environment(final Map<String, String> properties,
            final Map<String, Object> variables) {
        super();
        this.variables = new Hashtable<String, Object>(variables);
        this.properties = new Hashtable<String, String>(properties);
    }

    public Set<Entry<String, String>> getProperties() {
        return properties.entrySet();
    }

    public String getProperty(final String name) {
        return properties.get(name);
    }

    public Object getVariable(final String name) {
        return variables.get(name);
    }

    public Set<Entry<String, Object>> getVariables() {
        return variables.entrySet();
    }

    public String setProperty(final String name, final String property) {
        return properties.put(name, property);
    }

    public Object setVariable(final String name, final Object variable) {
        return variables.put(name, variable);
    }
}
