/**
 * 
 */
package com.thinkparity.ophelia.browser.mode.demo;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class Demo {

    /** Demo configuration <code>Properties</code>. */
    private Properties configuration;

    /** Demo <code>Scenario</code>s. */
    private final List<Scenario> scenarios;

    /** Create Demo. */
    public Demo() {
        super();
        this.scenarios = new ArrayList<Scenario>();
    }

    /**
     * Obtain the demo configuration.
     * 
     * @return The configuration <code>Properties</code>.
     */
    public Properties getConfiguration() {
        return configuration;
    }

    /**
     * Set the demo configuration.
     * 
     * @param configuration
     *            The configuration <code>Properties</code>.
     */
    public void setConfiguration(final Properties configuration) {
        this.configuration = configuration;
    }

    /**
     * Add a demo scenario.
     * 
     * @param scenario
     *            A <code>Scenario</code>.
     * @return Whether or not the list of <code>Scenario</code> was modified.
     */
    boolean addScenario(final Scenario scenario) {
        return scenarios.add(scenario);
    }

    /**
     * Obtain a demo property.
     * 
     * @param key
     *            The property key.
     * @return The property value or null if the property is not set.
     */
    String getProperty(final String key) {
        return configuration.getProperty(MessageFormat.format("demo.{0}", key), null);
    }

    /**
     * Obtain a demo scenario property.
     * 
     * @param key
     *            The property key.
     * @return The property value or null if the property is not set.
     */
    String getScenarioProperty(final String scenario, final String key) {
        return configuration.getProperty(MessageFormat.format(
                "demo.scenario.{0}.{1}", scenario, key), null);
    }

    /**
     * Obtain a demo scenario script property.
     * 
     * @param key
     *            The property key.
     * @return The property value or null if the property is not set.
     */
    String getScriptProperty(final String scenario, final String script,
            final String key) {
        return configuration.getProperty(MessageFormat.format(
                "demo.scenario.{0}.{1}.{2}", scenario, script, key), null);
    }

    /**
     * Obtain a list of demo scenarios.
     * 
     * @return A list of <code>Scenario</code>s.
     */
    List<Scenario> getScenarios() {
        return Collections.unmodifiableList(scenarios);
    }

    /**
     * Remove a demo scenario.
     * 
     * @param scenario
     *            A <code>Scenario</code>.
     * @return Whether or not the list of <code>Scenario</code> was modified.
     */
    boolean removeScenario(final Scenario scenario) {
        return scenarios.remove(scenario);
    }
}
